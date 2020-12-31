# Katalon TestOps TestNG

This is the TestNG Reporter for submitting test results to Katalon TestOps.

## Usage

It is recommended to follow instructions in Katalon TestOps Project page.

### Installation

Add the dependency to `pom.xml`:

```xml
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Configuration

Configurations will be read from environment variable, Java system properties, and properties file in this order.

* `testops.serverUrl` (environment variable: `TESTOPS_SERVERURL`)
Katalon TestOps endpoint (default: `https://testops.katalon.io`).

* `testops.apiKey` (environment variable: `TESTOPS_APIKEY`)
Your Katalon TestOps API Key.

* `testops.projectId` (environment variable: `TESTOPS_PROJECTID`)
The Katalon TestOps project that will receive the test results.

#### Via environment variable

* Linux

```
export TESTOPS_SERVERURL=https://testops.katalon.io
export TESTOPS_APIKEY=<your API key>
export TESTOPS_PROJECTID=<your project ID>
mvn test
```

* Windows

```
set TESTOPS_SERVERURL=https://testops.katalon.io
set TESTOPS_APIKEY=<your API key>
set TESTOPS_PROJECTID=<your project ID>
mvn test
```

#### Via Java system properties

```
mvn test -Dtestops.serverUrl=https://testops.katalon.io -Dtestops.apiKey=<your API key> -Dtestops.projectId=<your project ID>
```

#### Via properties file

Create a `testops.properties` file in the `resources` directory

```
testops.serverUrl=htts://testops.katalon.io
testops.apiKey=
testops.projectId=
```

Run the command:

```
mvn test
```

## Sample

https://github.com/katalon-studio-samples/testops-testng-sample
