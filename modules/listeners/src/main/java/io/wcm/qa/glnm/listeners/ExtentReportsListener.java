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

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;

import org.apache.commons.text.StringEscapeUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Handles {@link ExtentReports} life cycle.
 */
public class ExtentReportsListener implements ITestListener, IConfigurationListener {

  @Override
  public void beforeConfiguration(ITestResult tr) {
    GaleniumReportUtil.getExtentTest(tr);
  }

  @Override
  public void onConfigurationFailure(ITestResult itr) {
    // nothing to do
  }

  @Override
  public void onConfigurationSkip(ITestResult itr) {
    // nothing to do
  }

  @Override
  public void onConfigurationSuccess(ITestResult itr) {
    // nothing to do
  }

  @Override
  public void onFinish(ITestContext context) {
    GaleniumReportUtil.finishExtentReports();
  }

  @Override
  public void onStart(ITestContext context) {
    // nothing to do
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    // nothing to do
  }

  @Override
  public void onTestFailure(ITestResult result) {
    String logMsgHtml = "Error when dealing with test failure.";
    try {

      StringBuilder logMsg = new StringBuilder().append(System.lineSeparator()).append(System.lineSeparator());
      logMsg.append("+++++++++++++++Failed Test++++++++++++++++").append(System.lineSeparator());
      logMsg.append("Testcase: ").append(getTestName(result)).append(System.lineSeparator());
      Throwable throwable = result.getThrowable();
      logMsg.append("Location: ").append(getLineThatThrew(throwable)).append(System.lineSeparator());
      String errorMessage = getHtmlEscapedMessage(throwable);
      logMsg.append("Error: ").append(errorMessage).append(System.lineSeparator());

      WebDriver driver = getDriver();
      if (driver != null) {
        logMsg.append(GaleniumReportUtil.takeScreenshot(result, driver));
        logMsg.append("URL: ").append(driver.getCurrentUrl()).append(System.lineSeparator());
      }
      if (getTestDevice() != null) {
        logMsg.append("Browser: ").append(getTestDevice().toString()).append(System.lineSeparator());
      }
      logMsg.append("Duration: ").append(getTestDuration(result)).append(System.lineSeparator());

      logStacktrace(throwable);

      logMsg.append("++++++++++++++++++++++++++++++++++++++++++").append(System.lineSeparator());

      logMsgHtml = logMsg.toString().replace(System.lineSeparator(), "<br />");
      Reporter.log(logMsgHtml, false);
      getLogger().error(logMsg.toString());
    }
    catch (WebDriverException | NullPointerException ex) {
      String msg = "Error during failure handling";
      GaleniumReportUtil.getLogger().error(msg, ex);
      throw new GaleniumException(msg, ex);
    }
    finally {
      GaleniumReportUtil.endExtentTest(result, LogStatus.FAIL, logMsgHtml);
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    GaleniumReportUtil.endExtentTest(result, LogStatus.SKIP, "SKIPPED");
  }

  @Override
  public void onTestStart(ITestResult result) {
    GaleniumReportUtil.getExtentTest(result);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    try {
      if (GaleniumConfiguration.isTakeScreenshotOnSuccessfulTest()) {
        takeScreenshot(result);
      }

      String msg = getTestName(result) + ": Success (" + getTestDuration(result) + ")";
      Reporter.log(msg + "<br />", false);
      getLogger().info(msg);
    }
    finally {
      getLogger().trace("ending extent test now.");
      GaleniumReportUtil.endExtentTest(result, LogStatus.PASS, "SUCCESS");
      getLogger().trace("ended extent test.");
    }
  }

  private WebDriver getDriver() {
    return GaleniumContext.getDriver();
  }

  private String getHtmlEscapedMessage(Throwable throwable) {
    String escapedMessage = StringEscapeUtils.escapeHtml4(throwable.getMessage());
    return escapedMessage;
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

  private String getTestDuration(ITestResult result) {
    return Long.toString(result.getEndMillis() - result.getStartMillis()) + "ms";
  }

  private void logStacktrace(Throwable throwable) {
    if (getLogger().isDebugEnabled()) {
      GaleniumReportUtil.getLogger().debug(GaleniumReportUtil.MARKER_FAIL, "Full stacktrace", throwable);
      Throwable cause = throwable.getCause();
      if (cause != null) {
        logStacktrace(cause);
      }
    }
  }

  private void takeScreenshot(ITestResult result) {
    WebDriver driver = getDriver();
    if (driver != null && ((RemoteWebDriver)driver).getSessionId() != null) {
      GaleniumReportUtil.takeScreenshot(result, driver);
    }
  }

  protected String getAdditionalInfo() {
    String additionalInfo = "no additional info";
    TestDevice testDevice = getTestDevice();
    if (testDevice != null) {
      additionalInfo = " [" + testDevice.toString() + "] ";
    }
    return additionalInfo;
  }

  protected String getTestName(ITestResult result) {
    return result.getTestClass().getRealClass().getSimpleName() + "." + result.getName() + "(" + getAdditionalInfo() + ") ";
  }

}
