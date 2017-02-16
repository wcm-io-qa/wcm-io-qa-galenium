/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.qa.galenium.listeners;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.Status;
import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.TestDevice;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * This listener is also responsible for closing the WebDriver instances. If
 * tests are run in parallel threads, it will close the WebDriver after each
 * test method. If they are run sequentially in the main thread, the same
 * WebDriver will be re-used and close after the last test case.
 */
public class LoggingListener extends TestListenerAdapter {

  @Override
  public void onTestStart(ITestResult result) {
    getLogger().debug(getTestName(result) + ": Start in thread " + Thread.currentThread().getName());
    GaleniumReportUtil.getExtentTest(result).log(Status.INFO, "Start in thread " + Thread.currentThread().getName());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    getLogger().info("Skipped test: " + getTestName(result));
    if (GaleniumConfiguration.isTakeScreenshotOnSkippedTest()) {
      takeScreenshot(result);
    }
    GaleniumReportUtil.getLogger().info(GaleniumReportUtil.MARKER_SKIP, "SKIPPED");
  }

  protected void takeScreenshot(ITestResult result) {
    WebDriver driver = WebDriverManager.getCurrentDriver();
    if (driver != null) {
      GaleniumReportUtil.takeScreenshot(result, driver);
    }
  }

  @Override
  public void onStart(ITestContext context) {
    // always executed in the main thread, so we can't initialize WebDrivers right here
    GalenConfig.getConfig().setProperty(GalenProperty.GALEN_BROWSER_VIEWPORT_ADJUSTSIZE, Boolean.TRUE.toString());
  }

  @Override
  public void onFinish(ITestContext context) {
    GaleniumReportUtil.createGalenReports();
  }

  private String getTestName(ITestResult result) {
    return result.getTestClass().getRealClass().getSimpleName() + "." + result.getName() + "(" + getAdditionalInfo() + ") ";
  }

  protected String getAdditionalInfo() {
    String additionalInfo = "no additional info";
    TestDevice testDevice = WebDriverManager.get().getTestDevice();
    if (testDevice != null) {
      additionalInfo = " [" + testDevice.toString() + "] ";
    }
    return additionalInfo;
  }

}
