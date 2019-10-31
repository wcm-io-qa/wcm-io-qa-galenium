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
import java.io.FileInputStream;
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
import org.testng.ITestResult;
import org.testng.Reporter;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.TestNgReportBuilder;
import com.google.common.html.HtmlEscapers;

import freemarker.template.TemplateException;
import io.qameta.allure.Allure;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Utility class containing methods handling reporting.
 *
 * @since 1.0.0
 */
public final class GaleniumReportUtil {

  private static final Logger LOG = LoggerFactory.getLogger(GaleniumReportUtil.class);

  private static final List<GalenTestInfo> GLOBAL_GALEN_RESULTS = new ArrayList<GalenTestInfo>();
  private static final String PATH_GALEN_REPORT = GaleniumConfiguration.getReportDirectory() + "/galen";
  private static final String PATH_SCREENSHOTS_RELATIVE_ROOT = "../screenshots";
  private static final String PATH_SCREENSHOTS_ROOT = GaleniumConfiguration.getReportDirectory() + "/screenshots";
  private static final String PATH_TESTNG_REPORT_XML = GaleniumConfiguration.getReportDirectory() + "/testng.xml";

  private GaleniumReportUtil() {
    // do not instantiate
  }

  /**
   * Add GalenTestInfo to global list for generating reports.
   *
   * @param galenTestInfo Galen test info to add to result set
   * @since 3.0.0
   */
  public static void addGalenResult(GalenTestInfo galenTestInfo) {
    if (isAddResult(galenTestInfo)) {
      GLOBAL_GALEN_RESULTS.add(galenTestInfo);
    }
  }

  /**
   * Write all test results to Galen report.
   *
   * @param testInfos list to persist test information
   * @since 3.0.0
   */
  public static void createGalenHtmlReport(List<GalenTestInfo> testInfos) {
    try {
      new HtmlReportBuilder().build(testInfos, PATH_GALEN_REPORT);
    }
    catch (IOException | NullPointerException ex) {
      LOG.error("could not generate Galen report.", ex);
    }
  }

  /**
   * Create reports from global list of GalenTestInfos.
   *
   * @since 3.0.0
   */
  public static void createGalenReports() {
    createGalenHtmlReport(GLOBAL_GALEN_RESULTS);
    createGalenTestNgReport(GLOBAL_GALEN_RESULTS);
  }

  /**
   * Write all test results to TestNG report.
   *
   * @param testInfos list to persist test information
   * @since 3.0.0
   */
  public static void createGalenTestNgReport(List<GalenTestInfo> testInfos) {
    try {
      new TestNgReportBuilder().build(testInfos, PATH_TESTNG_REPORT_XML);
    }
    catch (IOException | TemplateException ex) {
      LOG.error("could not generate TestNG report.", ex);
    }
  }

  /**
   * Uses {@link com.google.common.html.HtmlEscapers} to escape text for use in logging.
   *
   * @param string potentially includes unescaped HTML
   * @return best effort HTML escaping
   * @since 3.0.0
   */
  public static String escapeHtml(String string) {
    String escapedString = HtmlEscapers.htmlEscaper().escape(StringUtils.stripToEmpty(string));
    return StringUtils.replace(escapedString, "\n", "</br>");
  }

  /**
   * Take screenshot of current browser window and add to reports. Uses random filename.
   *
   * @return log message including screenshot if everything was successful
   * @since 3.0.0
   */
  public static String takeScreenshot() {
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
    return takeScreenshot(randomAlphanumeric, getTakesScreenshot());
  }

  /**
   * Captures image of single element in page.
   *
   * @param takesScreenshot to capture
   * @return message to log screenshot to report
   * @since 3.0.0
   */
  public static String takeScreenshot(TakesScreenshot takesScreenshot) {
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
    return takeScreenshot(randomAlphanumeric, takesScreenshot);
  }

  /**
   * Take screenshot of current browser window and add to reports.
   *
   * @param result to generate filename from
   * @param driver to take screenshot from
   * @return log message including screenshot if everything was successful
   * @since 3.0.0
   */
  public static String takeScreenshot(ITestResult result, WebDriver driver) {
    String resultName = getAlphanumericTestName(result);
    return takeScreenshot(resultName, getTakesScreenshot(driver));
  }

  /**
   * Take screenshot of current browser window and add to reports.
   *
   * @param resultName to use in filename
   * @param takesScreenshot to take screenshot from
   * @return log message including screenshot if everything was successful
   * @since 3.0.0
   */
  public static String takeScreenshot(String resultName, TakesScreenshot takesScreenshot) {
    String destScreenshotFilePath = null;
    String filenameOnly = null;
    boolean screenshotSuccessful;
    LOG.debug("taking screenshot: " + takesScreenshot);
    filenameOnly = System.currentTimeMillis() + "_" + resultName + ".png";
    File screenshotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
    if (screenshotFile != null) {
      LOG.trace("screenshot taken: " + screenshotFile.getPath());
      try {
        File destFile = new File(PATH_SCREENSHOTS_ROOT, filenameOnly);
        FileUtils.copyFile(screenshotFile, destFile);
        LOG.trace("copied screenshot: " + destFile.getPath());
        destScreenshotFilePath = PATH_SCREENSHOTS_RELATIVE_ROOT + File.separator + filenameOnly;
        Allure.addAttachment("Screenshot: " + resultName, "image/png", new FileInputStream(screenshotFile), ".png");
        screenshotSuccessful = true;
        if (FileUtils.deleteQuietly(screenshotFile)) {
          LOG.trace("deleted screenshot file: " + screenshotFile.getPath());
        }
        else {
          LOG.trace("could not delete screenshot file: " + screenshotFile.getPath());
        }
      }
      catch (IOException ex) {
        LOG.error("Cannot copy screenshot.", ex);
        screenshotSuccessful = false;
      }
    }
    else {
      LOG.debug("screenshot file is null.");
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
