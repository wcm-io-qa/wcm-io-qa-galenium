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
/* Copyright (c) wcm.io. All rights reserved. */
package io.wcm.qa.glnm.reporting;

import static io.wcm.qa.glnm.util.TestInfoUtil.getAlphanumericTestName;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.TestNgReportBuilder;
import com.google.common.html.HtmlEscapers;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.ReporterType;
import com.relevantcodes.extentreports.model.ITest;
import com.relevantcodes.extentreports.model.TestAttribute;

import freemarker.template.TemplateException;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;
import io.wcm.qa.glnm.util.TestInfoUtil;

/**
 * Utility class containing methods handling reporting.
 */
public final class GaleniumReportUtil {

  /** For all special ExtentReports events. */
  public static final Marker MARKER_EXTENT_REPORT = MarkerFactory.getMarker("EXTENT_REPORT");
  /** For special ERROR log status. */
  public static final Marker MARKER_ERROR = getMarker(LogStatus.ERROR);
  /** For special FAIL log status. */
  public static final Marker MARKER_FAIL = getMarker(LogStatus.FAIL);
  /** For special FATAL log status. */
  public static final Marker MARKER_FATAL = getMarker(LogStatus.FATAL);
  /** For special INFO log status. */
  public static final Marker MARKER_INFO = getMarker(LogStatus.INFO);
  /** For special PASS log status. */
  public static final Marker MARKER_PASS = getMarker(LogStatus.PASS);
  /** For special SKIP log status. */
  public static final Marker MARKER_SKIP = getMarker(LogStatus.SKIP);
  /** For special WARN log status. */
  public static final Marker MARKER_WARN = getMarker(LogStatus.WARNING);

  private static final String CONFIGURED_PATH_EXTENT_REPORTS_ROOT = GaleniumConfiguration.getReportDirectory() + "/extentreports";
  private static final String DEFAULT_FOR_NO_TEST_NAME = "no.test.name.set";
  private static final GaleniumExtentReports GLOBAL_EXTENT_REPORTS;
  private static final List<GalenTestInfo> GLOBAL_GALEN_RESULTS = new ArrayList<GalenTestInfo>();
  private static final Logger INTERNAL_LOGGER = LoggerFactory.getLogger(GaleniumReportUtil.class);
  private static final Marker MARKER_REPORT_UTIL_INTERNAL = getMarker("galenium.reporting.internal");
  private static final String PATH_EXTENT_REPORTS_DB = CONFIGURED_PATH_EXTENT_REPORTS_ROOT + "/extentGalen.db";
  private static final String PATH_EXTENT_REPORTS_REPORT = CONFIGURED_PATH_EXTENT_REPORTS_ROOT + "/extentGalen.html";
  private static final String PATH_GALEN_REPORT = GaleniumConfiguration.getReportDirectory() + "/galen";
  private static final String PATH_SCREENSHOTS_RELATIVE_ROOT = "../screenshots";
  private static final String PATH_SCREENSHOTS_ROOT = GaleniumConfiguration.getReportDirectory() + "/screenshots";
  private static final String PATH_TESTNG_REPORT_XML = GaleniumConfiguration.getReportDirectory() + "/testng.xml";

  static {
    INTERNAL_LOGGER.info("initializing GaleniumReportUtil");
    if (GaleniumConfiguration.isSkipExtentReports()) {
      INTERNAL_LOGGER.info("skipping ExtentReports initialization");
      GLOBAL_EXTENT_REPORTS = null;
    }
    else {
      INTERNAL_LOGGER.info("initializing ExtentReports: " + PATH_EXTENT_REPORTS_REPORT);
      GLOBAL_EXTENT_REPORTS = new GaleniumExtentReports(PATH_EXTENT_REPORTS_REPORT, NetworkMode.OFFLINE);

      File reportConfig = GaleniumConfiguration.getReportConfig();
      if (reportConfig != null) {
        GLOBAL_EXTENT_REPORTS.loadConfig(reportConfig);
      }

      INTERNAL_LOGGER.info("starting reporter: " + PATH_EXTENT_REPORTS_DB);
      GLOBAL_EXTENT_REPORTS.startReporter(ReporterType.DB, PATH_EXTENT_REPORTS_DB);

      Runtime.getRuntime().addShutdownHook(new ExtentReportShutdownHook());
    }
  }

  private GaleniumReportUtil() {
    // do not instantiate
  }

  /**
   * Add GalenTestInfo to global list for generating reports.
   * @param galenTestInfo Galen test info to add to result set
   */
  public static void addGalenResult(GalenTestInfo galenTestInfo) {
    if (isAddResult(galenTestInfo)) {
      GLOBAL_GALEN_RESULTS.add(galenTestInfo);
    }
  }

  /**
   * Assigns categories to {@link ExtentTest}.
   * @param categories to add
   */
  public static void assignCategories(String... categories) {
    for (String category : categories) {
      assignCategory(category);
    }
  }

  /**
   * Assigns a single category to {@link ExtentTest}.
   * @param extentTest to add to
   * @param category to add
   */
  public static void assignCategory(ExtentTest extentTest, String category) {
    if (StringUtils.isBlank(category)) {
      // do not tag blank categories
      return;
    }
    List<TestAttribute> categoryList = extentTest.getTest().getCategoryList();
    for (TestAttribute testAttribute : categoryList) {
      if (StringUtils.equals(testAttribute.getName(), category)) {
        return;
      }
    }
    extentTest.assignCategory(category);
  }

  /**
   * Assigns a single category to {@link ExtentTest}.
   * @param category to add
   */
  public static void assignCategory(String category) {
    ExtentTest extentTest = getExtentTest();
    assignCategory(extentTest, category);
  }

  /**
   * Write all test results to Galen report.
   * @param testInfos list to persist test information
   */
  public static void createGalenHtmlReport(List<GalenTestInfo> testInfos) {
    try {
      new HtmlReportBuilder().build(testInfos, PATH_GALEN_REPORT);
    }
    catch (IOException | NullPointerException ex) {
      getLogger().error("could not generate Galen report.", ex);
    }
  }

  /**
   * Create reports from global list of GalenTestInfos.
   */
  public static void createGalenReports() {
    createGalenHtmlReport(GLOBAL_GALEN_RESULTS);
    createGalenTestNgReport(GLOBAL_GALEN_RESULTS);
  }

  /**
   * Write all test results to TestNG report.
   * @param testInfos list to persist test information
   */
  public static void createGalenTestNgReport(List<GalenTestInfo> testInfos) {
    try {
      new TestNgReportBuilder().build(testInfos, PATH_TESTNG_REPORT_XML);
    }
    catch (IOException | TemplateException ex) {
      getLogger().error("could not generate TestNG report.", ex);
    }
  }

  /**
   * @param result current test result
   * @param status status to use for final message
   * @param details final message
   */
  public static void endExtentTest(ITestResult result, LogStatus status, String details) {
    getInternalLogger().trace("GaleniumReportUtilendExtentTest(): getting extent test.");
    ExtentTest extentTest = getExtentTest(result);
    getInternalLogger().trace("GaleniumReportUtilendExtentTest(): logging details.");
    extentTest.log(status, details);
    getInternalLogger().trace("GaleniumReportUtilendExtentTest(): assigning categories.");
    TestInfoUtil.assignCategories(extentTest, result);
    getInternalLogger().trace("GaleniumReportUtilendExtentTest(): ending extent report test");
    GLOBAL_EXTENT_REPORTS.endTest(extentTest);
    getInternalLogger().trace("GaleniumReportUtilendExtentTest(): done");
  }

  /**
   * Uses {@link HtmlEscapers} to escape text for use in logging.
   * @param string potentially includes unescaped HTML
   * @return best effort HTML escaping
   */
  public static String escapeHtml(String string) {
    String escapedString = HtmlEscapers.htmlEscaper().escape(StringUtils.stripToEmpty(string));
    return StringUtils.replace(escapedString, "\n", "</br>");
  }

  /**
   * Finish and flush ExtentReports.
   */
  public static void finishExtentReports() {
    ExtentReports extentReport = getExtentReports();
    if (!GaleniumConfiguration.isNoTestNg()) {
      // if using TestNG, append reporter output
      extentReport.setTestRunnerOutput(StringUtils.join(Reporter.getOutput(), "</br>"));
    }
    extentReport.flush();
  }

  public static ExtentReports getExtentReports() {
    return GLOBAL_EXTENT_REPORTS;
  }

  public static ExtentTest getExtentTest() {
    return GaleniumContext.getExtentTest();
  }

  /**
   * @param result current test result
   * @return test report associated with result
   */
  public static ExtentTest getExtentTest(ITestResult result) {
    return getExtentTest(result.getTestName());
  }

  /**
   * @param name test name to retrieve test
   * @return test report associated with result
   */
  public static ExtentTest getExtentTest(String name) {
    ExtentTest currentReport = getExtentTest();
    if (reportFitsName(name, currentReport)) {
      return currentReport;
    }
    ExtentTest newReport = GLOBAL_EXTENT_REPORTS.getExtentTest(name);
    setExtentTest(newReport);
    return newReport;
  }

  /**
   * @return the logger used for the current test, if no test is set, it will use "no.test.name.set" as the test name
   */
  public static Logger getLogger() {
    ExtentTest extentTest = getExtentTest();
    if (extentTest != null) {
      ITest test = extentTest.getTest();
      if (test != null) {
        return LoggerFactory.getLogger(test.getName());
      }
    }

    return LoggerFactory.getLogger(DEFAULT_FOR_NO_TEST_NAME);
  }

  /**
   * Gets a logger which marks every entry with the passed {@link Marker}.
   * @param marker to use with this logger
   * @return a {@link MarkedLogger} using the marker
   */
  public static Logger getMarkedLogger(Marker marker) {
    return new MarkedLogger(getLogger(), marker);
  }

  /**
   * @param name marker name
   * @return marker for use with marked logger
   */
  public static Marker getMarker(String name) {
    Marker marker = MarkerFactory.getMarker(name);
    marker.add(MARKER_EXTENT_REPORT);
    return marker;
  }

  /**
   * Null safe check whether {@link ExtentTest} and {@link ITestResult} have the same name.
   * @param result
   * @param extentTest
   * @return whether both have a test name and it is equal
   */
  public static boolean haveMatchingName(ITestResult result, ExtentTest extentTest) {
    if (result == null || extentTest == null) {
      return false;
    }
    ITest test = extentTest.getTest();
    if (test == null) {
      return false;
    }
    String testNameFromResult = result.getTestName();
    if (testNameFromResult == null) {
      return false;
    }
    String testNameFromExtentTest = test.getName();
    if (testNameFromExtentTest == null) {
      return false;
    }
    return StringUtils.equals(testNameFromExtentTest, testNameFromResult);
  }


  /**
   * Take screenshot of current browser window and add to reports. Uses random filename.
   * @return log message including screenshot if everything was successful
   */
  public static String takeScreenshot() {
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
    return takeScreenshot(randomAlphanumeric, getTakesScreenshot());
  }

  /**
   * Take screenshot of current browser window and add to reports.
   * @param result to generate filename from
   * @param driver to take screenshot from
   * @return log message including screenshot if everything was successful
   */
  public static String takeScreenshot(ITestResult result, WebDriver driver) {
    String resultName = getAlphanumericTestName(result);
    return takeScreenshot(resultName, getTakesScreenshot(driver));
  }

  /**
   * Take screenshot of current browser window and add to reports.
   * @param resultName to use in filename
   * @param takesScreenshot to take screenshot from
   * @return log message including screenshot if everything was successful
   */
  public static String takeScreenshot(String resultName, TakesScreenshot takesScreenshot) {
    String destScreenshotFilePath = null;
    String filenameOnly = null;
    boolean screenshotSuccessful;
    getInternalLogger().debug("taking screenshot: " + takesScreenshot);
      filenameOnly = System.currentTimeMillis() + "_" + resultName + ".png";
    File screenshotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
      if (screenshotFile != null) {
        getInternalLogger().trace("screenshot taken: " + screenshotFile.getPath());
        try {
          File destFile = new File(PATH_SCREENSHOTS_ROOT, filenameOnly);
          FileUtils.copyFile(screenshotFile, destFile);
          getInternalLogger().trace("copied screenshot: " + destFile.getPath());
          destScreenshotFilePath = PATH_SCREENSHOTS_RELATIVE_ROOT + File.separator + filenameOnly;
          String screenCapture = getExtentTest().addScreenCapture(destScreenshotFilePath);
          getLogger().info(screenCapture);
          screenshotSuccessful = true;
          if (FileUtils.deleteQuietly(screenshotFile)) {
            getInternalLogger().trace("deleted screenshot file: " + screenshotFile.getPath());
          }
          else {
            getInternalLogger().trace("could not delete screenshot file: " + screenshotFile.getPath());
          }
        }
        catch (IOException ex) {
          getLogger().error("Cannot copy screenshot.", ex);
          screenshotSuccessful = false;
        }
      }
      else {
        getInternalLogger().debug("screenshot file is null.");
        screenshotSuccessful = false;
      }

    StringBuilder logMsg = new StringBuilder();
    if (screenshotSuccessful) {
      logMsg.append("Screenshot: ").append(PATH_SCREENSHOTS_ROOT).append(File.separator).append(filenameOnly).append(System.lineSeparator());
      if (destScreenshotFilePath != null) {
        WebDriver driver;
        if (takesScreenshot instanceof WebDriver) {
          driver = (WebDriver)takesScreenshot;
        }
        else {
          driver = GaleniumContext.getDriver();
        }
        if (driver != null) {
          String url = driver.getCurrentUrl();
          String title = driver.getTitle();
          Reporter.log("<a href=\"" + url + "\"><img src=\"" + destScreenshotFilePath + "\" alt=\"" + title + "\"/></a>", true);
        }
      }
    }

    return logMsg.toString();
  }

  /**
   * Captures image of single element in page.
   * @param takesScreenshot to capture
   * @return message to log screenshot to report
   */
  public static String takeScreenshot(TakesScreenshot takesScreenshot) {
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
    return takeScreenshot(randomAlphanumeric, takesScreenshot);
  }

  private static Logger getInternalLogger() {
    return getMarkedLogger(MARKER_REPORT_UTIL_INTERNAL);
  }

  private static Marker getMarker(LogStatus logStatus) {
    return getMarker(logStatus.name());
  }

  private static TakesScreenshot getTakesScreenshot() {
    WebDriver driver = GaleniumContext.getDriver();
    TakesScreenshot takesScreenshot = getTakesScreenshot(driver);
    return takesScreenshot;
  }

  private static TakesScreenshot getTakesScreenshot(WebDriver driver) {
    TakesScreenshot takesScreenshot;
    if (driver instanceof TakesScreenshot) {
      takesScreenshot = (TakesScreenshot)driver;
    }
    else {
      throw new GaleniumException("driver cannot take screenshot");
    }
    return takesScreenshot;
  }

  private static boolean isAddResult(GalenTestInfo galenTestInfo) {
    if (galenTestInfo == null) {
      return false;
    }
    if (GaleniumConfiguration.isOnlyReportGalenErrors()) {
      if ((!galenTestInfo.isFailed()) && (galenTestInfo.getReport().fetchStatistic().getWarnings() == 0)) {
        return false;
      }
    }
    return true;
  }

  private static boolean reportFitsName(String name, ExtentTest currentReport) {
    return currentReport != null
        && currentReport.getTest() != null
        && currentReport.getTest().getName() != null
        && currentReport.getTest().getName().equals(name);
  }

  private static void setExtentTest(ExtentTest extentTest) {
    GaleniumContext.getContext().setExtentTest(extentTest);
  }
}
