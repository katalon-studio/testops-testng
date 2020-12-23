package com.katalon.testops.testng.helper;

import com.katalon.testops.commons.helper.GeneratorHelper;
import com.katalon.testops.commons.helper.StringHelper;
import com.katalon.testops.commons.model.Metadata;
import com.katalon.testops.commons.model.Status;
import com.katalon.testops.commons.model.TestResult;
import org.testng.*;
import org.testng.annotations.Parameters;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Stream;

import static com.katalon.testops.commons.core.Constants.TESTOPS_UUID_ATTR;
import static com.katalon.testops.commons.helper.StringHelper.getErrorMessage;
import static com.katalon.testops.commons.helper.StringHelper.getStackTraceAsString;

public class ReportHelper {

    private static final List<Class<?>> INJECTED_TYPES = Arrays.asList(
            ITestContext.class, ITestResult.class, XmlTest.class, Method.class, Object[].class
    );

    public static Map<String, String> getParameters(ITestNGMethod method, Object... parameters) {
        Map<String, String> result = new HashMap<>();

        Method reflectionMethod = method.getConstructorOrMethod().getMethod();
        Class<?>[] parameterTypes = reflectionMethod.getParameterTypes();

        if (parameterTypes.length != parameters.length) {
            return Collections.emptyMap();
        }

        String[] providedNames = Optional.ofNullable(reflectionMethod.getAnnotation(Parameters.class))
                .map(Parameters::value)
                .orElse(new String[]{});

        String[] reflectionNames = Stream.of(reflectionMethod.getParameters())
                .map(Parameter::getName)
                .toArray(String[]::new);

        int skippedCount = 0;
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            if (INJECTED_TYPES.contains(parameterType)) {
                skippedCount++;
                continue;
            }

            final int indexFromAnnotation = i - skippedCount;
            if (indexFromAnnotation < providedNames.length) {
                result.put(providedNames[indexFromAnnotation], StringHelper.toString(parameters[i]));
                continue;
            }

            if (i < reflectionNames.length) {
                result.put(reflectionNames[i], StringHelper.toString(parameters[i]));
            }
        }

        return result;
    }

    public static TestResult createTestResult(ITestResult result, Status status) {
        String uuid = GeneratorHelper.generateUniqueValue();

        TestResult testResult = new TestResult();
        testResult.setStatus(status);
        testResult.setUuid(uuid);
        testResult.setName(result.getMethod().getQualifiedName());
        testResult.setSuiteName(result.getTestContext().getSuite().getName());

        if (status.equals(Status.FAILED) || status.equals(Status.ERROR) || status.equals(Status.SKIPPED)) {
            final Throwable throwable = result.getThrowable();
            testResult.setErrorMessage(getErrorMessage(throwable));
            testResult.setStackTrace(getStackTraceAsString(throwable));
        }
        testResult.setParameters(getParameters(result.getMethod(), result.getParameters()));

        testResult.setStart(result.getStartMillis());
        testResult.setStop(result.getEndMillis());
        testResult.setDuration(result.getEndMillis() - result.getStartMillis());
        return testResult;
    }

    public static Metadata createMetadata() {
        Metadata metadata = new Metadata();
        metadata.setFramework("testng");
        metadata.setLanguage("java");
        metadata.setVersion(ReportHelper.class.getPackage().getImplementationVersion());
        return metadata;
    }

    public static String generateAndRegisterUuid(IAttributes iAttributes) {
        String uuid = GeneratorHelper.generateUniqueValue();
        iAttributes.setAttribute(TESTOPS_UUID_ATTR, uuid);
        return uuid;
    }

    public static String getOrCreateUuid(IAttributes iAttributes) {
        Object uuid = iAttributes.getAttribute(TESTOPS_UUID_ATTR);
        if (uuid == null) {
            return generateAndRegisterUuid(iAttributes);
        }
        return Objects.toString(uuid);
    }

    public static String getParentUuid(ITestResult result) {
        ISuite currentSuite = result.getTestContext().getSuite();
        return getOrCreateUuid(currentSuite);
    }
}
