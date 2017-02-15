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

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * This listener is also responsible for closing the WebDriver instances. If
 * tests are run in parallel threads, it will close the WebDriver after each
 * test method. If they are run sequentially in the main thread, the same
 * WebDriver will be re-used and close after the last test case.
 */
public abstract class AbstractGaleniumListener extends TestListenerAdapter {

  protected abstract void closeDriver();

  protected abstract TestDevice getTestDevice();

  protected abstract WebDriver getDriver();

  @Override
  public void onTestFailure(ITestResult result) {
    String logMsgHtml = "Error when dealing with test failure.";
    try {

      StringBuilder logMsg = new StringBuilder().append(System.lineSeparator()).append(System.lineSeparator());
      logMsg.append("+++++++++++++++Failed Test++++++++++++++++").append(System.lineSeparator());
      logMsg.append("Testcase: ").append(getTestName(result)).append(System.lineSeparator());
      Throwable throwable = result.getThrowable();
      logMsg.append("Location: ").append(getLineThatThrew(throwable)).append(System.lineSeparator());
      logMsg.append("Error: ").append(throwable.getMessage()).append(System.lineSeparator());

      WebDriver driver = getDriver();
      if (driver != null) {
        logMsg.append(GaleniumReportUtil.takeScreenshot(result, driver));
        logMsg.append("URL: ").append(driver.getCurrentUrl()).append(System.lineSeparator());
      }
      if (getTestDevice() != null) {
        logMsg.append("Browser: ").append(getTestDevice().toString()).append(System.lineSeparator());
      }
      logMsg.append("Duration: ").append(getTestDuration(result)).append(System.lineSeparator());

      GaleniumReportUtil.getLogger().debug(GaleniumReportUtil.MARKER_FAIL, "Full stacktrace", throwable);

      logMsg.append("++++++++++++++++++++++++++++++++++++++++++").append(System.lineSeparator());

      logMsgHtml = logMsg.toString().replace(System.lineSeparator(), "<br />");
      Reporter.log(logMsgHtml, false);
      getLogger().error(logMsg.toString());

      closeDriverIfRunningInParallel(result);

    }
    catch (Throwable ex) {
      GaleniumReportUtil.getLogger().error("Error during failure handling", ex);
      throw ex;
    }
    finally {
      super.onTestFailure(result);
      GaleniumReportUtil.endExtentTest(result, Status.FAIL, logMsgHtml);
    }
  }


  private String getLineThatThrew(Throwable t) {
    StackTraceElement[] stackTraceLines = t.getStackTrace();
    for (StackTraceElement stackTraceLine : stackTraceLines) {
      String className = stackTraceLine.getClassName();
      if (!className.startsWith("sun.") && !className.startsWith("java.") && !className.startsWith("org.openqa.selenium.")) {
        return stackTraceLine.toString();
      }
    }
    return "??? (cannot find throwing line)";
  }

  @Override
  public void onTestStart(ITestResult result) {
    getLogger().debug(getTestName(result) + ": Start in thread " + Thread.currentThread().getName());
    GaleniumReportUtil.getExtentTest(result).log(Status.INFO, "Start in thread " + Thread.currentThread().getName());
    super.onTestStart(result);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    try {
      takeScreenshot(result);

      String msg = getSuccessMessage(result);
      Reporter.log(msg + "<br />", false);
      getLogger().info(msg);
      closeDriverIfRunningInParallel(result);
    }
    finally {
      GaleniumReportUtil.endExtentTest(result, Status.PASS, "SUCCESS");
      super.onTestSuccess(result);
    }
  }

  protected String getSuccessMessage(ITestResult result) {
    return getTestName(result) + ": Success (" + getTestDuration(result) + ")";
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

  @Override
  public void onTestSkipped(ITestResult result) {
    getLogger().info("Skipped test: " + getTestName(result));
    super.onTestSkipped(result);
    if (GaleniumConfiguration.isTakeScreenshotOnSkippedTest()) {
      takeScreenshot(result);
    }
    GaleniumReportUtil.getLogger().info(GaleniumReportUtil.MARKER_SKIP, "SKIPPED");
    closeDriverIfRunningInParallel(result);
  }

  protected void takeScreenshot(ITestResult result) {
    WebDriver driver = getDriver();
    if (driver != null) {
      GaleniumReportUtil.takeScreenshot(result, driver);
    }
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    super.onTestFailedButWithinSuccessPercentage(result);
    closeDriverIfRunningInParallel(result);
  }

  @Override
  public void onStart(ITestContext context) {
    super.onStart(context);
    // always executed in the main thread, so we can't initialize WebDrivers right here
    GalenConfig.getConfig().setProperty(GalenProperty.GALEN_BROWSER_VIEWPORT_ADJUSTSIZE, Boolean.TRUE.toString());
  }

  @Override
  public void onFinish(ITestContext context) {
    super.onFinish(context);

    GaleniumReportUtil.createGalenReports();

    // if not running multiple tests/method/classes in parallel, then the same WebDriver instance is
    // used for all tests methods, and we have to make sure to close the driver after all tests have finished
    if (!isRunningTestsInSeparateThreads(context)) {
      getLogger().debug("Closing the WebDriver driver that was used for all tests...");
      closeDriver();
    }

    ExtentReports extentReport = GaleniumReportUtil.getExtentReports();
    extentReport.setTestRunnerOutput(StringUtils.join(Reporter.getOutput(), "</br>"));
    extentReport.flush();
  }


  /**
   * @param context of the current test case
   * @return true if any of the parallel execution modes is used
   */
  private boolean isRunningTestsInSeparateThreads(ITestContext context) {

    // getParallel() will return "methods", "classes", "tests" or "false" (which is the default)
    return !"false".equals(context.getSuite().getParallel());
  }

  private String getTestName(ITestResult result) {
    return result.getTestClass().getRealClass().getSimpleName() + "." + result.getName() + "(" + getAdditionalInfo() + ") ";
  }

  protected String getAdditionalInfo() {
    String additionalInfo = "no additional info";
    TestDevice testDevice = getTestDevice();
    if (testDevice != null) {
      additionalInfo = " [" + testDevice.toString() + "] ";
    }
    return additionalInfo;
  }

  private String getTestDuration(ITestResult result) {
    return Long.toString(result.getEndMillis() - result.getStartMillis()) + "ms";
  }

}
