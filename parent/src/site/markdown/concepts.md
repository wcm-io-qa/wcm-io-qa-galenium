# Galenium Concepts

There are a few concepts in Galenium that are important to know. Some should be familiar, others might be new.

## Galenium Basic Units

 Essentially, Galenium knows the following units:

* Objects
  * Objects are accessed via CSS selectors.
* Specifications
  * Specifications define constraints for objects.
* Suites
  * Suites are used to summarize test cases based on specifications.


## How Galenium Works

### Thread-Safe Contexts
 TestNG uses multiple threads to handle test cases concurrently. 
 A thread-safe context is created that makes configuration and data available to only one test case instance whereas it is not visible to all other test case instances. 
 Galenium uses the Java class java.lang.ThreadLocal to store the context separate for each thread. 
 Synchronization is not required because the object is not shared which improves scalability and performance.
 Access is done via static methods to allow integration without having to worry about inheritance. 
 There is always only one value per thread.

### Drivers
 The Selenium WebDriver is needed for interaction with the browser. Every test case needs its own dedicated browser instance. The driver represents and enables the connection to the browser.

### Test Devices
 The test device is a Galenium specific concept. It encapsulates the browser type (e.g. Chrome) and the viewport size (e.g. 1024x800). With this information Galenium can instantiate browsers using Selenium.

### Test Names
 In Galenium the test name is central. The test name is used to aggregate log statements and results for reporting purposes in Extent Report. Different names for test cases must be used to get separate test cases in the Extent Report.

### Test Descriptions
 A more detailed description can be added to the test case and logged in the report. Descriptions can be used for important information that would overload the test name. 
 Descriptions are optional and not used by default in Galenium.

### Reports
 This is used to keep multiple runs of the same test case logging to the same Extent Report test case.
 Log management is done transparently by Galenium. 
 The GaleniumReportUtil.getLogger() method is used to retrieve a logger. 
 Simple Logging Facade for Java (SLF4J) logging calls are used to write to the test case report. 
 The GaleniumReportUtil.getMarkedLogger(Marker) method is used to create marked log entries which can be filtered using a SLF4J configuration.

### Thread-Safe Context

Parallel execution is very important in UI tests. Setup using real browsers takes time. With responsive layouts we end up with a lot test cases quickly.

Galenium uses TestNG's parallelization features which allows multiple threads to handle the test cases concurrently. Configuration and data has to be available to one test case instance, but must not be visible to all other test case instances. A thread-safe context achieves this.

#### GaleniumContext

The _java.lang.ThreadLocal_ is used to store the context separate for each thread. Access is done via static methods to allow integration without having to worry about inheritance. There is always only one value per Thread.

##### Driver

The Selenium WebDriver is needed for interaction with the browser. Every test case needs its own dedicated browser instance. The driver represents and enables the connection to the browser. 

##### TestDevice

The test device is a Galenium concept. It encapsulates the browser type (e.g. Chrome) and the viewport size (e.g. 1024x800). This is all the information Galenium needs to instantiate browsers using Selenium.

##### Test Name

The test name is very important in most frameworks. In Galenium it is more central than usual. For reporting purposes the test name is used to aggregate log statements and results in the _ExtentReport_. You need to use different test names for your cases to get separate test cases in the _ExtentReport_.

##### Test Description

A more detailed description of the test than just the name. This is optional and not used by default in Galenium. When you have important information that would overload the test name, put it in the description and log it to the report.

##### Report

Test cases in Galenium have a one-to-one relationship with a test case in _ExtentReports_. This is used to keep multiple runs of the same   test case logging to the same _ExtentReport_ test case.

The management is done transparently by Galenium. All you need to do is use the _GaleniumReportUtil.getLogger()_ method to retrieve a logger and then use _SLF4J_ logging calls to write to the test case report. Combined with the _GaleniumReportUtil.getMarkedLogger(Marker)_ method you can easily create marked log entries that can be filtered using _SLF4J_ configuration. 

### Configuration

All standard Galenium configuration can be accessed using the static methods in _GaleniumConfiguration_.

### WebDriver Handling

A standard problem in parallelizing Selenium tests is the handling of drivers and browsers. Galenium can handle this for you.

#### Listener Based Handling

By default Galenium will use the _WebDriverListener_ to initialize and clean up drivers to be used in tests. This means that even if you do not use a driver in your test, it will still be instantiated.

#### Lazy Handling

If you want more control over when drivers are instantiated, you can configure Galenium to use lazy driver initialization. Drivers will only be initialized when you explicitely request a driver.

### Verifications

### Differences

When inspecting pages we are looking for unexpected differences. Galenium allows you to explicitly declare expected differences.

Differences can be expected for different components, URLs, browsers, view port sizes, element counts, or countless other dimensions. By explicitly introducing these expected differences it is possible to use a single verification across all those dimensions without duplicating any code. 

### Sampling

As we expect many test cases it would be cumbersome to manually create and manage all the test data. When using Galenium's verifications it is easy to sample the data in one run and use it as expected data in the next run. 

The two basic sampling methods are image based and text based. Images are saved as files in a folder structure based on expected differences, while text based samples are stored in a Java Properties file. The image file paths and property keys are generated from expected differences.

### Logging

Reporting is important in automated testing. Galenium uses a combination of several approaches.

#### SLF4J/Logback

A standard logging framework should flatten the learning curve while providing powerful features and configuration.

With Logback integration Galenium can log to standard out, files and to _ExtentReports_.

#### ExtentReports

ExtentReports is a specialized reporting tool for test results. Test reports are separated on a per test case basis. Each report is made up of steps with each step being one log event.

Tagging test cases provide a very helpful overview of general health and specific problem patterns. 

#### Galen

Galen is best at reporting on its own results. It is hard to integrate with other reports. Galen generates the file names internally without providing an easy way to fetch them programmatically to link to them from other reports.

To save disk space it is possible to tell Galenium to only write Galen reports for failed tests. 

#### TestNG

TestNG generates its own reports. The TestNG HTML report is basically useless when using _DataProviders_, but the XML results are the way Galenium integrates results with Maven and Jenkins.