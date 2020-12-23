package com.katalon.testops.testng.reporter;

import com.katalon.testops.commons.ReportLifecycle;
import com.katalon.testops.commons.model.Status;
import com.katalon.testops.commons.model.TestResult;
import com.katalon.testops.commons.model.TestSuite;
import com.katalon.testops.testng.helper.LogHelper;
import org.slf4j.Logger;
import org.testng.*;

import static com.katalon.testops.testng.helper.ReportHelper.*;

public class ReportListener implements ITestListener, IExecutionListener, ISuiteListener {

    private static final Logger logger = LogHelper.getLogger();

    private final ReportLifecycle reportLifecycle;

    public ReportListener() {
        reportLifecycle = new ReportLifecycle();
    }

    @Override
    public void onExecutionStart() {
        reportLifecycle.startExecution();
        reportLifecycle.writeMetadata(createMetadata());
    }

    @Override
    public void onStart(ISuite suite) {
        logger.info("onStart: " + suite.getName());

        String uuid = generateAndRegisterUuid(suite);

        TestSuite testSuite = new TestSuite();
        testSuite.setName(suite.getName());
        reportLifecycle.startSuite(testSuite, uuid);
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("onTestStart: " + result.getMethod().getMethodName());

    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info("onTestFailure: " + result.getMethod().getMethodName());
        stopTestCaseWithResult(result, Status.FAILED);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info("onTestSkipped: " + result.getMethod().getMethodName());
        stopTestCaseWithResult(result, Status.SKIPPED);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("onTestFailedButWithinSuccessPercentage: " + result.getMethod().getMethodName());
        stopTestCaseWithResult(result, Status.PASSED);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        logger.info("onTestFailedWithTimeout: " + result.getMethod().getMethodName());
        stopTestCaseWithResult(result, Status.ERROR);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("onTestSuccess: " + result.getMethod().getMethodName());
        stopTestCaseWithResult(result, Status.PASSED);
    }

    @Override
    public void onFinish(ISuite suite) {
        logger.info("onFinish ISuite: " + suite.getName());
        String uuid = getOrCreateUuid(suite);
        reportLifecycle.stopTestSuite(uuid);
    }

    @Override
    public void onExecutionFinish() {
        reportLifecycle.writeTestResultsReport();
        reportLifecycle.writeTestSuitesReport();
        reportLifecycle.stopExecution();
        reportLifecycle.writeExecutionReport();
        reportLifecycle.upload();
        reportLifecycle.reset();
    }

    private void stopTestCaseWithResult(ITestResult result, Status status) {
        TestResult testResult = createTestResult(result, status);
        testResult.setParentUuid(getParentUuid(result));
        reportLifecycle.stopTestCase(testResult);
    }
}
