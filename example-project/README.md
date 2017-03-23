Galenium Example Project
========================

This project contains some examples for how to integrate Galenium into your project.

Run Example Project
-------------------

[wcm-io-sample](http://wcm.io/samples/) has to be deployed to an AEM instance running on *localhost* on port *4502* and you need to have Chrome installed.

If you have that, you just run the Maven command:

`mvn clean install -Pexample`

This will download ChromeDriver and run the test examples sequentially in the Chrome browser.

Once the run is finished can find the reports at:
1. `target/galenium-reports/extentreports/extentGalen.html`
2. `target/galenium-reports/galen/report.html`

Project Specific Non-Test Things
--------------------------------

### Abstract Base Class

*WebDriver* handling and some common functionality needed by multiple test classes.

### Test Device Provider

[TestDeviceProvider](src/main/java/io/wcm/qa/galenium/example/TestDeviceProvider.java) is a TestNG data provider supplying the tests with browsers and sizes to be used in tests. 


Test Examples
-------------

### Running a Galen Specification

[GalenSpecIT](src/test/java/io/wcm/qa/galenium/example/GalenSpecIT.java) is a simple test demonstrating how to run Galen specifications defined in gspec files. It is as easy as a call to *checkLayout()*.

### Using Image Comparison Specification Factory

[ImageComparisonExampleIT](src/test/java/io/wcm/qa/galenium/example/ImageComparisonExampleIT.java) shows how to use a factory to programmatically create Galen specifications doing image comparison on individual web page elements. The *galenium.sampling.image.save* can be set to *true* to have deviations from expected images or missing expected images trigger saving of actual sampled image. This is useful for initially seeding the test data, but also for quickly establishing a new expected state.  

### Selenium Only Navigation

[NavigationExampleIT](src/test/java/io/wcm/qa/galenium/example/NavigationExampleIT.java) is just pure Selenium based testing of a web page. Navigates using a link and verifies the URL after clicking.
