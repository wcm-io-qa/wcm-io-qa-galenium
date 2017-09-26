Galenium Integration
====================

This is the main integration library. It integrates Selenium, Galen, and Reporting. The idea is that you only need to implement project specifics without having to worry too much about handling of WebDrivers over multiple threads, running tests in different browsers, or how to make Galen and Selenium share browser sessions.

## Configuration

| Name | Description | Default |
| ---- | ----------- |:-------:|
| *galenium.sampling.image.directory* | Directory to save new sample images to. | ./target/galenium-reports |
| *galenium.sampling.image.save* | Sets image comparisons done via spec factory to warning level and saves all anomalies using the paths of the expected images to make it easy to turn sampled images into expected images in the next run. | false |
| *galenium.report.rootPath* | Root path for reports written by Galenium. | ./target/galenium-reports |
| *galenium.retryMax* | How many times a failed test should be retried before considering it failed. | 2 |
| *galenium.screenshotOnSkipped* | Make a screenshot when skipping test. | false |
| *galenium.specPath* | Root path to use to look up relative spec paths. | ./target/specs |
| *galenium.report.galen.errorsOnly* | Only write Galen reports for specifications that failed validation. This can save a lot of disk space when running a lot of validations.  | false |
| *galenium.report.sparse* | Do not output debug statements to report. | false |
| *galenium.sampling.text.directory* | Directory to save new text samples to. | ./target/galenium-reports |
| *galenium.sampling.text.save* | Saves new text samples to property file to use them as expected values in future runs. | false |
| *io.wcm.qa.baseUrl* | Base URL to run tests against. | http://localhost:4502 |
| *io.wcm.qa.extent.reportConfig* | ExtentReports config file can be used to customize ExtentReport. | null |

## WebDriverManager

Always use *WebDriverManager* to get your *WebDriver* instances. It can handle different browsers, multiple threads, and running locally or on the grid. It also ensures that Selenium and Galen can share a browser session.

## Listeners

Listeners are an important concept in both TestNG and Galen. They handle test lifecycle and validation events.

### RetryAnalyzer

Will retry tests according to *galenium.retryMax* configuration setting. This is a very effective way to deal with flaky tests. If a test run succeeds before the maximum number of retries is reached, the test is regarded as successful. 

### DefaultGaleniumListener

Handles reporting and *WebDriver* in the Galenium way. Easiest way to integrate is to pass it to TestNG using the *listener* property via the POM.

## Abstract Test Base

A good starting point for implementing your test cases. It contains a lot of boiler plate code for handling *WebDriver*, reporting, and assertions.

## Assertions

Sometimes it is desirable to have tests continue running even if assertions fail. This can be achieved by using soft assertions. The basic test class has a method *getAssertion()* which can be overwritten to return a *GaleniumSoftAssertion* object.

## Selectors

*Selector* is a Galenium concept that uses a single type to represent both Galen's notion of *Locator* and Selenium's *By*. It is an interface that should be used within Galenium tests up to the final handing off to proper Galen or Selenium API methods. This helps sharing UI objects easily between Galen and Selenium based functionality.

## Image Comparison

Image comparison is a powerful way to verify correct rendering of pages or elements. One of the big caveats is that rendering can be very fragile and not every pixel difference is meaningful enough to fail a test. This is addressed quite nicely by [Galen's image comparison specification](http://galenframework.com/docs/reference-galen-spec-language-guide/#Image) which offers options to be lenient in a very controlled way. Another problem is the handling of sample data. It can be a lot of work to create and store all the example images for all the different use cases. To help with this, Galenium offers the concept of an _Image comparison specification factory_ which will create a Galen specification programmatically which will use sample images. 

## Reporting

Reporting is very important in all phases of automated testing. Different phases have different reporting requirements. During test creation you will want more information than when running trusted tests on CI. This is reflected in Galeniums reporting approach by using a common logger interface (SLF4J) to write to reports. This allows you to add debug or even trace level information to your tests and infrastructure, while restricting reports on CI to info level. 

Another factor is that with Galen reports a lot of disk space will be used for screenshots. This can be too much when keeping the data for a lot of builds on CI. You can configure Galenium to only write Galen reports for failed validations. Especially when using a lot of image comparison, this can significantly reduce disk space requirements.
