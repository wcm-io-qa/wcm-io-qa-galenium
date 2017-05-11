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
package io.wcm.qa.galenium.listeners;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_ERROR;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.TestDevice;
import io.wcm.qa.galenium.util.TestInfoUtil;
import io.wcm.qa.galenium.webdriver.WebDriverManager;


public class WebDriverListener implements ITestListener {

  private static final Marker MARKER_WEBDRIVER = MarkerFactory.getMarker("webdriver");

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
    try {

    TestDevice testDevice = TestInfoUtil.getTestDevice(result);
    if (testDevice != null) {
      getLogger().debug("new driver for: " + testDevice);
      WebDriverManager.getDriver(testDevice);
      }
      else {
        getLogger().debug("no test device set for: " + result.getTestName());
      }
    }
    finally {
      if (getDriver() == null) {
        getLogger().warn(MARKER_ERROR, "driver not instantiated");
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

  /**
   * @param context of the current test case
   * @return true if any of the parallel execution modes is used
   */
  private boolean isRunningTestsInSeparateThreads(ITestContext context) {

    // getParallel() will return "methods", "classes", "tests" or "false" (which is the default)
    return !"false".equals(context.getSuite().getParallel());
  }

  protected void closeDriver() {
    WebDriverManager.closeDriver();
  }

  protected Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER_WEBDRIVER);
  }

}
