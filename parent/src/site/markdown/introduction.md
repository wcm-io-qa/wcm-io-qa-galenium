## Galenium Goals

* Running tests in parallel on multiple servers to save test execution time.
* Running tests on remote browser instances to spread testing load across several machines.
* Running tests for browsers on different platforms or operating systems.
* Running tests for responsive websites with different viewports and breakpoints.
* Getting text and images samples from existing web pages automatically and using samples as a target state for the next test run.

## Galenium's Added Value
 The added value that Galenium generates and why it is worth using Galenium:
* It provides a ready-to-use integration framework with Maven + TestNG + Selenium + Galen + Extent Reports.
* Galenium eliminates typical inhibitors to starting new test automation projects, such as the amount of time its takes setting up real browsers, others TODO:.
* It provides a single point of object definition for Selenium and Galen. Double definition effort is eliminated. The object definition created in Galenium can be used without alteration in standalone Galen. In this case Galen is used without Java and Selenium. This allows to use Galenium object definitions for automated Frontend sanity checks, e.g. for the transition from development test systems to production system.
* It includes its own HTTP proxy server (TODO: dedicated HTTP proxy server or generic proxy server for protocols, e.g. TODO:).
 If a connection is established between a client and a server, the addresses of the client and server are not simply exchanged, but the proxy server manages the communication itself. It can analyze transferred packets and change them if necessary. This allows to manipulate access data and credentials, e.g. in test cases that require user logins.
* Test cases in Galenium have a one-to-one relationship with a test case in Extent Reports. TODO: elaborate
## Galenium Integrated Framework
 Galenium uses the following tools and frameworks:
### Maven
 Galenium uses Maven as a build tool to enable automated build operations. Maven applies patterns to the steps of a project lifecycle such as build, test, package, document and deploy.
### TestNG
 Galenium uses TestNG’s parallelization features which allow multiple threads to handle test cases concurrently. TestNG allows defining test suites and running the tests of certain test suites with any number of threads at the same time.
### Selenium
 Galenium uses Selenium to automate browser related tasks. Selenium can be used as a software testing framework for web applications. Selenium includes the component Selenium Grid. Selenium Grid can run multiple tests in parallel on multiple servers. The Selenium client driver connects to the Selenium hub component instead of the Selenium server. 
 The hub has a list of servers that provide access to browser instances (WebDriver nodes). 
 Tests contact the hub to obtain access to browser instances. 
 Different browser versions and browser configurations can be managed centrally and are used by individual tests.
### Galen
Galenium uses Galen for image comparison. In Galen the page layout is defined by the position of test objects relative to each other on the page. For this purpose, a special syntax and rules are used to describe the layout of a page. 
Example: The position of a logo in relation to the navigation, the position of the navigation in relation to the content area.
Galen uses Selenium Grid to drive different browsers like Firefox, Chrome, Internet Explorer. Interactions with the webpage are written in JavaScript with an interface to Selenium. 
 The Galen API can be added as a dependency in Maven projects.
### Extent Reports
 Galenium uses Extent Reports which is a HTML reporting library for Selenium WebDriver for Java. Galenium uses the following Extent Reports features:
* Logs inside HTML.
* Interactive reports
  * For example, clicking on a failed test case will show the status of the test and the reason of the failure (exception details).
* Pie charts (or circle charts) based on test case status.
* Step summaries.
* Report filtering depending on status.
* Execution history.
* Fetching system details such as OS, Java version, memory etc.
* Attaching screenshots within reports.
* SLF4J appender
  * Galenium includes an appender for Simple Logging Facade for Java (SLF4J) which can be used in combination with Extent Reports. Appenders are responsible for writing event data and delivering log events to their target destination.

### BrowserMob Proxy
TODO: describe

## Galenium Overview
 Essentially, Galenium knows the following units:
* Objects
 Objects are accessed via CSS selectors.
* Specifications
 Specifications define constraints for objects.
* Suites
 Suites are used to summarize test cases based on specifications.

## Step-by-Step Procedure
* Define objects
* Write Selenium tests
* Add Galen specifications
* Generate sample data for target state if applicable
* Run tests
* Compare with sampled target state if applicable
* Check reports

## Information Sources
* Software configuration management (SCM)
  * Galenium tests
  * Galen specifications
  * Galen test suites
* Continous Integration (CI)/Jenkins
  * Jobs
  * Reports
* Selenium Grid
  * Browsers
  * OSs

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