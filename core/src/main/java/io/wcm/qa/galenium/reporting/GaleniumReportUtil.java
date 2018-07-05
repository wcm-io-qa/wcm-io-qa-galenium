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
package io.wcm.qa.galenium.reporting;

import static io.wcm.qa.galenium.util.TestInfoUtil.getAlphanumericTestName;

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
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.ReporterType;
import com.relevantcodes.extentreports.model.ITest;
import com.relevantcodes.extentreports.model.TestAttribute;

import freemarker.template.TemplateException;
import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.util.TestInfoUtil;

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


  private static final GaleniumExtentReports EXTENT_REPORTS;

  private static final Marker MARKER_REPORT_UTIL_INTERNAL = getMarker("galenium.reporting.internal");

  private static final String NO_TEST_NAME_SET = "no.test.name.set";

  // Root folder for reports
  private static final String PATH_REPORT_ROOT = GaleniumConfiguration.getReportDirectory();

  // ExtentReports
  private static final String PATH_EXTENT_REPORTS_ROOT = PATH_REPORT_ROOT + "/extentreports";
  private static final String PATH_EXTENT_REPORTS_DB = PATH_EXTENT_REPORTS_ROOT + "/extentGalen.db";
  private static final String PATH_EXTENT_REPORTS_REPORT = PATH_EXTENT_REPORTS_ROOT + "/extentGalen.html";

  // Galen
  private static final List<GalenTestInfo> GALEN_RESULTS = new ArrayList<GalenTestInfo>();
  private static final String PATH_GALEN_REPORT = PATH_REPORT_ROOT + "/galen";

  // Screenshots
  private static final String PATH_SCREENSHOTS_ROOT = PATH_REPORT_ROOT + "/screenshots";
  private static final String PATH_SCREENSHOTS_RELATIVE_ROOT = "../screenshots";

  // TestNG
  private static final String PATH_TESTNG_REPORT_XML = PATH_REPORT_ROOT + "/testng.xml";

  static {
    if (GaleniumConfiguration.isSkipExtentReports()) {
      EXTENT_REPORTS = null;
    }
    else {
      EXTENT_REPORTS = new GaleniumExtentReports(PATH_EXTENT_REPORTS_REPORT, NetworkMode.OFFLINE);

      File reportConfig = GaleniumConfiguration.getReportConfig();
      if (reportConfig != null) {
        EXTENT_REPORTS.loadConfig(reportConfig);
      }

      EXTENT_REPORTS.startReporter(ReporterType.DB, PATH_EXTENT_REPORTS_DB);

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
      GALEN_RESULTS.add(galenTestInfo);
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
   * @param category to add
   */
  public static void assignCategory(String category) {
    if (StringUtils.isBlank(category)) {
      // do not tag blank categories
      return;
    }
    ExtentTest extentTest = getExtentTest();
    List<TestAttribute> categoryList = extentTest.getTest().getCategoryList();
    for (TestAttribute testAttribute : categoryList) {
      if (StringUtils.equals(testAttribute.getName(), category)) {
        return;
      }
    }
    extentTest.assignCategory(category);
  }

  /**
   * Write all test results to Galen report.
   * @param testInfos list to persist test information
   */
  public static void createGalenHtmlReport(List<GalenTestInfo> testInfos) {
    try {
      new HtmlReportBuilder().build(testInfos, PATH_GALEN_REPORT);
    }
    catch (IOException ex) {
      getLogger().error("could not generate Galen report.", ex);
    }
  }
  /**
   * Create reports from global list of GalenTestInfos.
   */
  public static void createGalenReports() {
    createGalenHtmlReport(GALEN_RESULTS);
    createGalenTestNgReport(GALEN_RESULTS);
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
    EXTENT_REPORTS.endTest(extentTest);
    getInternalLogger().trace("GaleniumReportUtilendExtentTest(): done");
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
    return EXTENT_REPORTS;
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
    if (currentReport == null
        || currentReport.getTest() == null
        || currentReport.getTest().getName() == null
        || !currentReport.getTest().getName().equals(name)) {
      currentReport = EXTENT_REPORTS.getExtentTest(name);
      setExtentTest(currentReport);
    }
    return currentReport;
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

    return LoggerFactory.getLogger(NO_TEST_NAME_SET);
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
    WebDriver driver = GaleniumContext.getDriver();
    return takeScreenshot(randomAlphanumeric, driver);
  }

  /**
   * Take screenshot of current browser window and add to reports.
   * @param result to generate filename from
   * @param driver to take screenshot from
   * @return log message including screenshot if everything was successful
   */
  public static String takeScreenshot(ITestResult result, WebDriver driver) {
    String resultName = getAlphanumericTestName(result);
    return takeScreenshot(resultName, driver);
  }

  /**
   * Take screenshot of current browser window and add to reports.
   * @param resultName to use in filename
   * @param driver to take screenshot from
   * @return log message including screenshot if everything was successful
   */
  public static String takeScreenshot(String resultName, WebDriver driver) {
    String destScreenshotFilePath = null;
    String filenameOnly = null;
    boolean screenshotSuccessful;
    if (driver instanceof TakesScreenshot) {
      getInternalLogger().debug("taking screenshot: " + driver);
      filenameOnly = System.currentTimeMillis() + "_" + resultName + ".png";
      File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
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
    }
    else {
      getInternalLogger().trace("driver cannot take screenshots: " + driver);
      screenshotSuccessful = false;
    }

    StringBuilder logMsg = new StringBuilder();
    if (screenshotSuccessful) {
      logMsg.append("Screenshot: ").append(PATH_SCREENSHOTS_ROOT).append(File.separator).append(filenameOnly).append(System.lineSeparator());
      if (destScreenshotFilePath != null) {
        String url = driver.getCurrentUrl();
        String title = driver.getTitle();
        Reporter.log("<a href=\"" + url + "\"><img src=\"" + destScreenshotFilePath + "\" alt=\"" + title + "\"/></a>", true);
      }
    }

    return logMsg.toString();
  }

  private static Logger getInternalLogger() {
    return getMarkedLogger(MARKER_REPORT_UTIL_INTERNAL);
  }

  private static Marker getMarker(LogStatus logStatus) {
    return getMarker(logStatus.name());
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

  private static void setExtentTest(ExtentTest extentTest) {
    GaleniumContext.getContext().setExtentTest(extentTest);
  }
}
