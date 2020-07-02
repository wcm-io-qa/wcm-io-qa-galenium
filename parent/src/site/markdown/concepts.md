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

### Test Names
 In Galenium the test name is central. The test name is used in reporting.

### Test Descriptions
 A more detailed description can be added to the test case and logged in the report. Descriptions can be used for important information that would overload the test name.
 Descriptions are optional and not used by default in Galenium.

### Reports
Allure reports integrates transparently with JUnit. Logs will be attached to the reports.

### Thread-Safe Context

Parallel execution is very important in UI tests. Setup using real browsers takes time. With responsive layouts we end up with a lot test cases quickly.

Galenium uses JUnit's parallelization features which allows multiple threads to handle the test cases concurrently. Configuration and data has to be available to one test case instance, but must not be visible to all other test case instances. A thread-safe context achieves this.

#### GaleniumContext

The _java.lang.ThreadLocal_ is used to store the context separate for each thread. Access is done via static methods to allow integration without having to worry about inheritance. There is always only one value per Thread.

##### Driver

The Selenium WebDriver is needed for interaction with the browser. Every test case needs its own dedicated browser instance. The driver represents and enables the connection to the browser.

### Configuration

All standard Galenium configuration can be accessed using the static methods in _GaleniumConfiguration_.

### WebDriver Handling

A standard problem in parallelizing Selenium tests is the handling of drivers and browsers. Galenium can handle this for you.

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

With Logback integration Galenium can log to standard out, files and attach these logs to
the Allure report.

#### Allure

Allure is a specialized reporting tool for test results. Test reports are separated on a per test case basis. Each report is made up of steps with each step being one log event.

Tagging test cases provide a very helpful overview of general health and specific problem patterns.

#### Galen

Galen is best at reporting on its own results. It is hard to integrate with other reports. Galen generates the file names internally without providing an easy way to fetch them programmatically to link to them from other reports.

To save disk space it is possible to tell Galenium to only write Galen reports for failed tests.
