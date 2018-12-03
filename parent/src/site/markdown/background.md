#  Galenium Integrated Framework
 Galenium uses the following tools and frameworks:
 
## Maven

 Galenium uses Maven as a build tool to enable automated build operations. Maven applies patterns to the steps of a project lifecycle such as build, test, package, document and deploy.
 
 [Read more about Maven](https://maven.apache.org/)

## TestNG

 Galenium uses TestNG’s parallelization features which allow multiple threads to handle test cases concurrently. TestNG allows defining test suites and running the tests of certain test suites with any number of threads at the same time.
 
 [Read more about TestNG](https://testng.org/doc/index.html)

## Selenium

 Galenium uses Selenium to automate browser related tasks. Selenium can be used as a software testing framework for web applications. Selenium includes the component Selenium Grid. Selenium Grid can run multiple tests in parallel on multiple servers. The Selenium client driver connects to the Selenium hub component instead of the Selenium server. 
 
 * The hub has a list of servers that provide access to browser instances (WebDriver nodes). 
 * Tests contact the hub to obtain access to browser instances. 
 * Different browser versions and browser configurations can be managed centrally and are used by individual tests.

 [Read more about Selenium](https://www.seleniumhq.org/docs/)

## Galen

Galenium uses Galen for image comparison. In Galen the page layout is defined by the position of test objects relative to each other on the page. For this purpose, a special syntax and rules are used to describe the layout of a page.
 
Example: The position of a logo in relation to the navigation, the position of the navigation in relation to the content area.
Galen uses Selenium Grid to drive different browsers like Firefox, Chrome, Internet Explorer. Interactions with the webpage are written in JavaScript with an interface to Selenium.

 [Read more about Galen](http://galenframework.com/docs/all/)

## ExtentReports

 Galenium uses ExtentReports which is a HTML reporting library for Java. Galenium uses the following ExtentReports features:
 
* Logs in HTML format.
* Interactive reports
  * For example, clicking on a failed test case will show the status of the test and the reason of the failure (exception details).
* Pie charts (or circle charts) based on test case status.
* Step summaries.
* Report filtering depending on status.
* Fetching system details such as OS, Java version, memory etc.
* Attaching screenshots within reports.
* Logback/SLF4J appender
  * Galenium includes an appender for Simple Logging Facade for Java (SLF4J) which can be used in combination with ExtentReports. Appenders are responsible for writing event data and delivering log events to their target destination.

 [Read more about ExtentReports](http://extentreports.com/docs/versions/2/java/)

## BrowserMob Proxy

 BrowserMob Proxy (BMP) is a Java HTTP proxy which allows manipulation and recording of HTTP communication. Galenium uses BMP to add basic auth which is commonly used to protect SUTs. The recording capabilities are used to wait for network traffic and to check response codes within tests.

 [Read more about BrowserMob Proxy](https://github.com/lightbody/browsermob-proxy)

