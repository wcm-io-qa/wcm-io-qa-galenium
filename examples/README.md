Galenium Example Project
========================

This project contains some examples for how to integrate Galenium into your project.

Run Example Project
-------------------

[wcm-io-sample](https://wcm.io/samples/) has to be deployed to an AEM instance running on *localhost* on port *4502* and you need to have Chrome installed.

If you have that, you just run the Maven command:

`mvn clean install -Pexample`

This will download ChromeDriver and run the test examples sequentially in the Chrome browser.

Once the run is finished you can find the reports at:
1. `tests/target/galenium-reports/extentreports/extentGalen.html`
2. `tests/target/galenium-reports/galen/report.html`
