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

import io.wcm.qa.galenium.reporting.GalenReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.TestDevice;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

/**
 * This listener is also responsible for closing the WebDriver instances. If
 * tests are run in parallel threads, it will close the WebDriver after each
 * test method. If they are run sequentially in the main thread, the same
 * WebDriver will be re-used and close after the last test case.
 */
public abstract class AbstractGaleniumListener extends TestListenerAdapter {

  private static final Logger log = LoggerFactory.getLogger(AbstractGaleniumListener.class);

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
      logMsg.append("Location: ").append(getLineThatThrew(result.getThrowable())).append(System.lineSeparator());
      logMsg.append("Error: ").append(result.getThrowable().getMessage()).append(System.lineSeparator());

      WebDriver driver = getDriver();
      if (driver != null) {
        logMsg.append(GalenReportUtil.takeScreenshot(result, driver));
        logMsg.append("URL: ").append(driver.getCurrentUrl()).append(System.lineSeparator());
      }
      if (getTestDevice() != null) {
        logMsg.append("Browser: ").append(getTestDevice().toString()).append(System.lineSeparator());
      }
      logMsg.append("Duration: ").append(getTestDuration(result)).append(System.lineSeparator());

      if (!GaleniumConfiguration.isSparseReporting()) {
        GalenReportUtil.getExtentTest(result).log(LogStatus.FAIL, "Full stacktrace", result.getThrowable());
      }

      logMsg.append("++++++++++++++++++++++++++++++++++++++++++").append(System.lineSeparator());

      logMsgHtml = logMsg.toString().replace(System.lineSeparator(), "<br />");
      Reporter.log(logMsgHtml, false);
      log.error(logMsg.toString());

      closeDriverIfRunningInParallel(result);

    }
    catch (Throwable ex) {
      GalenReportUtil.getExtentTest(result).log(LogStatus.ERROR, "Error during failure handling", ex);
      throw ex;
    }
    finally {
      super.onTestFailure(result);
      GalenReportUtil.endExtentTest(result, LogStatus.FAIL, logMsgHtml);
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
    log.debug(getTestName(result) + ": Start in thread " + Thread.currentThread().getName());

    GalenReportUtil.getExtentTest(result).log(LogStatus.INFO, "Start in thread " + Thread.currentThread().getName());
    super.onTestStart(result);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    try {
      takeScreenshot(result);

      String msg = getSuccessMessage(result);
      Reporter.log(msg + "<br />", false);
      log.info(msg);
      closeDriverIfRunningInParallel(result);
    }
    finally {
      GalenReportUtil.endExtentTest(result, LogStatus.PASS, "SUCCESS");
      super.onTestSuccess(result);
    }
  }

  protected String getSuccessMessage(ITestResult result) {
    return getTestName(result) + ": Success (" + getTestDuration(result) + ")";
  }

  private void closeDriverIfRunningInParallel(ITestResult result) {
    ITestContext testContext = result.getTestContext();
    if (getDriver() == null) {
      log.debug("No WebDriver to close for thread " + Thread.currentThread().getName());
    }
    else if (isRunningTestsInSeparateThreads(testContext)) {
      log.info("Closing WebDriver for thread " + Thread.currentThread().getName() + " on host '" + testContext.getSuite().getHost() + "'");
      closeDriver();
    }
    else {
      log.debug("Reusing WebDriver for thread " + Thread.currentThread().getName());
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    log.info("Skipped test: " + getTestName(result));
    super.onTestSkipped(result);
    if (GaleniumConfiguration.isTakeScreenshotOnSkippedTest()) {
      takeScreenshot(result);
    }
    GalenReportUtil.getExtentTest(result).log(LogStatus.SKIP, "SKIPPED");
    closeDriverIfRunningInParallel(result);
  }

  protected void takeScreenshot(ITestResult result) {
    WebDriver driver = getDriver();
    if (driver != null) {
      GalenReportUtil.takeScreenshot(result, driver);
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

    GalenReportUtil.createGalenReports();

    // if not running multiple tests/method/classes in parallel, then the same WebDriver instance is
    // used for all tests methods, and we have to make sure to close the driver after all tests have finished
    if (!isRunningTestsInSeparateThreads(context)) {
      log.debug("Closing the WebDriver driver that was used for all tests...");
      closeDriver();
    }

    ExtentReports extentReport = GalenReportUtil.getExtentReports();
    extentReport.setTestRunnerOutput(StringUtils.join(Reporter.getOutput(), "</br>"));
    extentReport.flush();
    extentReport.close();

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
      additionalInfo = " [" + testDevice.getFullInfo() + "] ";
    }
    return additionalInfo;
  }

  private String getTestDuration(ITestResult result) {
    return Long.toString(result.getEndMillis() - result.getStartMillis()) + "ms";
  }

}
