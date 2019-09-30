/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.glnm.listeners;


import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getNumberOfBrowserInstantiationRetries;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.isSuppressAutoAdjustBrowserSize;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_ERROR;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;
import io.wcm.qa.glnm.util.TestInfoUtil;
import io.wcm.qa.glnm.webdriver.HasDevice;
import io.wcm.qa.glnm.webdriver.WebDriverManagement;

/**
 * Handles {@link WebDriver} life cycle in single and multi threaded scenarios.
 */
public class WebDriverListener implements ITestListener {

  private static final Marker MARKER_WEBDRIVER = MarkerFactory.getMarker("galenium.webdriver");
  private static final String CATEGORY_WEB_DRIVER_NOT_INSTANTIATED = "WEBDRIVER_NOT_INSTANTIATED";

  @Override
  public void onFinish(ITestContext context) {
    // if not running multiple tests/method/classes in parallel, then the same WebDriver instance is
    // used for all tests methods, and we have to make sure to close the driver after all tests have finished
    if (!isRunningTestsInSeparateThreads(context)) {
      getLogger().debug("Closing the WebDriver driver that was used for all tests...");
      closeDriver();
    }
  }

  @Override
  public void onStart(ITestContext context) {
    getLogger().trace("WebDriverListener active.");
    // always executed in the main thread, so we can't initialize WebDrivers right here
    setAdjustViewportInGalen(!isSuppressAutoAdjustBrowserSize());
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    closeDriverIfRunningInParallel(result);
  }

  @Override
  public void onTestFailure(ITestResult result) {
    closeDriverIfRunningInParallel(result);
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    closeDriverIfRunningInParallel(result);
  }

  @Override
  public void onTestStart(ITestResult result) {
    // do nothing for lazy initialization
    if (GaleniumConfiguration.isLazyWebDriverInitialization()) {
      getLogger().debug(MARKER_INFO, "Driver will be initialized on first use.");
      return;
    }

    // create driver for test
    int retries = 0;
    TestDevice testDevice = getTestDevice(result);
    if (!WebDriverManagement.isBrowser(testDevice)) {
      return;
    }

    while (retries <= getNumberOfBrowserInstantiationRetries()) {
      try {
        if (testDevice != null) {
          getLogger().debug("new driver for: " + testDevice);
          WebDriverManagement.getDriver(testDevice);
        }
        else {
          getLogger().debug("no test device set for: " + result.getTestName());
        }
      }
      finally {
        if (getDriver() == null) {
          Marker severity;
          if (result.getInstance() instanceof HasDevice) {
            // there should be a device and driver should be instantiated
            severity = MARKER_ERROR;
          }
          else {
            // tests without devices are allowed, so we should not put errors in the report
            severity = MARKER_INFO;
          }
          getLogger().warn(severity, "driver not instantiated");
          GaleniumReportUtil.assignCategory(CATEGORY_WEB_DRIVER_NOT_INSTANTIATED);
          retries++;
        }
        else {
          // we have a driver and can move on
          break;
        }
      }
    }
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    closeDriverIfRunningInParallel(result);
  }

  private void closeDriverIfRunningInParallel(ITestResult result) {
    ITestContext testContext = result.getTestContext();
    if (getDriver() == null) {
      getLogger().debug("No WebDriver to close for thread " + Thread.currentThread().getName());
    }
    else if (isRunningTestsInSeparateThreads(testContext)) {
      getLogger().info("Closing WebDriver for thread " + Thread.currentThread().getName() + " on host '" + testContext.getSuite().getHost() + "'");
      closeDriver();
    }
    else {
      getLogger().debug("Reusing WebDriver for thread " + Thread.currentThread().getName());
    }
  }

  private TestDevice getTestDevice(ITestResult result) {
    getLogger().debug("fetch test device from result.");
    TestDevice testDevice = TestInfoUtil.getTestDevice(result);
    if (testDevice == null) {
      getLogger().debug("no test device found in result.");
      testDevice = GaleniumContext.getTestDevice();
    }
    return testDevice;
  }

  /**
   * @param context of the current test case
   * @return true if any of the parallel execution modes is used
   */
  private boolean isRunningTestsInSeparateThreads(ITestContext context) {

    // getParallel() will return "methods", "classes", "tests" or "false" (which is the default)
    return !"false".equals(context.getSuite().getParallel());
  }

  private void setAdjustViewportInGalen(boolean adjustBrowserViewportSize) {
    GalenConfig.getConfig().setProperty(
        GalenProperty.GALEN_BROWSER_VIEWPORT_ADJUSTSIZE,
        new Boolean(adjustBrowserViewportSize).toString());
  }

  protected void closeDriver() {
    WebDriverManagement.closeDriver();
  }

  protected Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER_WEBDRIVER);
  }

}
