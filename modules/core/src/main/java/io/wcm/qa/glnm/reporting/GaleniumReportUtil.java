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

import freemarker.template.TemplateException;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Utility class containing methods handling reporting.
 */
public final class GaleniumReportUtil {

  private static final String DEFAULT_FOR_NO_TEST_NAME = "no.test.name.set";
  private static final List<GalenTestInfo> GLOBAL_GALEN_RESULTS = new ArrayList<GalenTestInfo>();
  private static final Marker MARKER_REPORT_UTIL_INTERNAL = getMarker("galenium.reporting.internal");
  private static final String PATH_GALEN_REPORT = GaleniumConfiguration.getReportDirectory() + "/galen";
  private static final String PATH_SCREENSHOTS_RELATIVE_ROOT = "../screenshots";
  private static final String PATH_SCREENSHOTS_ROOT = GaleniumConfiguration.getReportDirectory() + "/screenshots";
  private static final String PATH_TESTNG_REPORT_XML = GaleniumConfiguration.getReportDirectory() + "/testng.xml";

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
   * Assigns categories to test in report.
   * @param categories to add
   */
  public static void assignCategories(String... categories) {
    for (String category : categories) {
      assignCategory(category);
    }
  }

  /**
   * Assigns a single category to test in report.
   * @param category to add
   */
  public static void assignCategory(String category) {
    //    TODO: possibly add Allure category
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
   * Uses {@link HtmlEscapers} to escape text for use in logging.
   * @param string potentially includes unescaped HTML
   * @return best effort HTML escaping
   */
  public static String escapeHtml(String string) {
    String escapedString = HtmlEscapers.htmlEscaper().escape(StringUtils.stripToEmpty(string));
    return StringUtils.replace(escapedString, "\n", "</br>");
  }

  /**
   * @return the logger used for the current test, if no test is set, it will use "no.test.name.set" as the test name
   */
  public static Logger getLogger() {
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
    return marker;
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
        // TODO: add Allure attachment
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
}
