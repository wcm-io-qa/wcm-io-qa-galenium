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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import io.wcm.qa.galenium.exceptions.GalenLayoutException;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.util.TestInfoUtil;

/**
 * Utility class containing methods handling reporting.
 */
public final class GaleniumReportUtil {

  /** For all special ExtentReports events. */
  public static final Marker MARKER_EXTENT_REPORT = MarkerFactory.getMarker("EXTENT_REPORT");
  /** For special FATAL log status. */
  public static final Marker MARKER_FATAL = getMarker(LogStatus.FATAL);
  /** For special FAIL log status. */
  public static final Marker MARKER_FAIL = getMarker(LogStatus.FAIL);
  /** For special PASS log status. */
  public static final Marker MARKER_PASS = getMarker(LogStatus.PASS);
  /** For special SKIP log status. */
  public static final Marker MARKER_SKIP = getMarker(LogStatus.SKIP);
  /** For special INFO log status. */
  public static final Marker MARKER_INFO = getMarker(LogStatus.INFO);
  /** For special ERROR log status. */
  public static final Marker MARKER_ERROR = getMarker(LogStatus.ERROR);
  /** For special WARN log status. */
  public static final Marker MARKER_WARN = getMarker(LogStatus.WARNING);

  // Logger
  private static final Logger log = LoggerFactory.getLogger(GaleniumReportUtil.class);

  // Root folder for reports
  private static final String PATH_REPORT_ROOT = GaleniumConfiguration.getReportDirectory();

  private static final String PATH_GALEN_REPORT = PATH_REPORT_ROOT + "/galen";

  // Screenshots
  private static final String PATH_SCREENSHOTS_RELATIVE_ROOT = "../screenshots";
  private static final String PATH_SCREENSHOTS_ROOT = PATH_REPORT_ROOT + "/screenshots";

  // TestNG
  private static final String PATH_TESTNG_REPORT_XML = PATH_REPORT_ROOT + "/testng.xml";

  // ExtentReports
  private static final String PATH_EXTENT_REPORTS_ROOT = PATH_REPORT_ROOT + "/extentreports";
  private static final String PATH_EXTENT_REPORTS_DB = PATH_EXTENT_REPORTS_ROOT + "/extentGalen.db";
  private static final String PATH_EXTENT_REPORTS_REPORT = PATH_EXTENT_REPORTS_ROOT + "/extentGalen.html";
  private static final GaleniumExtentReports EXTENT_REPORTS;
  static {
    EXTENT_REPORTS = new GaleniumExtentReports(PATH_EXTENT_REPORTS_REPORT, NetworkMode.OFFLINE);

    File reportConfig = GaleniumConfiguration.getReportConfig();
    if (reportConfig != null) {
      EXTENT_REPORTS.loadConfig(reportConfig);
    }

    EXTENT_REPORTS.startReporter(ReporterType.DB, PATH_EXTENT_REPORTS_DB);
  }

  // Galen
  private static final List<GalenTestInfo> GALEN_RESULTS = new ArrayList<GalenTestInfo>();

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

  private static boolean isAddResult(GalenTestInfo galenTestInfo) {
    if (GaleniumConfiguration.isOnlyReportGalenErrors()) {
      if ((!galenTestInfo.isFailed()) && (galenTestInfo.getReport().fetchStatistic().getWarnings() == 0)) {
        return false;
      }
    }
    return true;
  }

  public static ExtentReports getExtentReports() {
    return EXTENT_REPORTS;
  }

  /**
   * Create reports from global list of GalenTestInfos.
   */
  public static void createGalenReports() {
    try {
      createGalenHtmlReport(GALEN_RESULTS);
      createGalenTestNgReport(GALEN_RESULTS);
    }
    catch (IOException ex) {
      log.error("IOException writing report", ex);
      throw new GalenLayoutException("IOException writing report", ex);
    }
    catch (TemplateException ex) {
      log.error("TemplateException writing report", ex);
      throw new GalenLayoutException("TemplateException writing report", ex);
    }
  }

  /**
   * Write all test results to Galen report.
   * @param testInfos list to persist test information
   * @throws IOException when unable to write report
   */
  public static void createGalenHtmlReport(List<GalenTestInfo> testInfos) throws IOException {
    new HtmlReportBuilder().build(testInfos, PATH_GALEN_REPORT);
  }

  /**
   * Write all test results to TestNG report.
   * @param testInfos list to persist test information
   * @throws IOException when unable to write report
   * @throws TemplateException in case of FreeParser error
   */
  public static void createGalenTestNgReport(List<GalenTestInfo> testInfos) throws IOException, TemplateException {
    new TestNgReportBuilder().build(testInfos, PATH_TESTNG_REPORT_XML);
  }

  /**
   * Take screenshot of current browser window and add to reports.
   * @param result
   * @param driver to take screenshot from
   * @return log message including screenshot if everything was successful
   */
  public static String takeScreenshot(ITestResult result, WebDriver driver) {
    String destScreenshotFilePath = null;
    String filenameOnly = null;
    if (driver instanceof TakesScreenshot) {
      filenameOnly = System.currentTimeMillis() + "_" + result.getName() + ".png";
      File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
      try {
        File destFile = new File(PATH_SCREENSHOTS_ROOT, filenameOnly);
        FileUtils.copyFile(screenshotFile, destFile);
        destScreenshotFilePath = PATH_SCREENSHOTS_RELATIVE_ROOT + File.separator + filenameOnly;
        String screenCapture = getExtentTest().addScreenCapture(destScreenshotFilePath);
        getLogger().info(screenCapture);
      }
      catch (IOException ex) {
        log.error("Cannot copy screenshot.", ex);
      }
    }

    StringBuilder logMsg = new StringBuilder();
    if (filenameOnly != null) {
      logMsg.append("Screenshot: ").append(PATH_SCREENSHOTS_ROOT).append(File.separator).append(filenameOnly).append(System.lineSeparator());
      if (destScreenshotFilePath != null) {
        String url = driver.getCurrentUrl();
        String title = driver.getTitle();
        Reporter.log("<a href=\"" + url + "\"><img src=\"" + destScreenshotFilePath + "\" alt=\"" + title + "\"/></a>", true);
      }
    }

    return logMsg.toString();
  }

  /**
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

  private static void setExtentTest(ExtentTest extentTest) {
    GaleniumContext.getContext().setExtentTest(extentTest);
  }

  public static ExtentTest getExtentTest() {
    return GaleniumContext.getExtentTest();
  }

  public static void assignCategories(String... categories) {
    for (String category : categories) {
      assignCategory(category);
    }
  }

  public static void assignCategory(String category) {
    ExtentTest extentTest = getExtentTest();
    List<TestAttribute> categoryList = extentTest.getTest().getCategoryList();
    for (TestAttribute testAttribute : categoryList) {
      if (StringUtils.equals(testAttribute.getName(), category)) {
        return;
      }
    }
    extentTest.assignCategory(category);
  }

  public static Logger getLogger() {
    String name = "NO_TEST_NAME_SET";
    ExtentTest extentTest = getExtentTest();
    if (extentTest != null) {
      ITest test = extentTest.getTest();
      if (test != null) {
        name = test.getName();
      }
    }
    return LoggerFactory.getLogger(name);
  }

  /**
   * @param status status to use for final message
   * @param details final message
   */
  public static void endExtentTest(ITestResult result, LogStatus status, String details) {
    ExtentTest extentTest = getExtentTest(result);
    extentTest.log(status, details);
    TestInfoUtil.assignCategories(extentTest, result);
    EXTENT_REPORTS.endTest(extentTest);
  }

  private static Marker getMarker(LogStatus logStatus) {
    Marker marker = MarkerFactory.getMarker(logStatus.name());
    marker.add(MARKER_EXTENT_REPORT);
    return marker;
  }
}
