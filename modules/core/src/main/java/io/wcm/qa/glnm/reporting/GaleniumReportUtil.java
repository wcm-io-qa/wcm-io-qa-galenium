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

import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.reports.TestNgReportBuilder;
import com.galenframework.utils.GalenUtils;
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

  private static final List<GalenTestInfo> GLOBAL_GALEN_RESULTS = new ArrayList<GalenTestInfo>();

  private static final Logger LOG = LoggerFactory.getLogger(GaleniumReportUtil.class);
  private static final String PATH_GALEN_REPORT = GaleniumConfiguration.getReportDirectory() + "/galen";
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
   * Uses {@link com.google.common.html.HtmlEscapers} to escape text for use in reporting.
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
   * uses Galen functionality to get a full page screenshot by scrolling and
   * stitching.
   *
   * @since 4.0.0
   */
  public static void takeFullScreenshot() {
    GalenConfig galenConfig = GalenConfig.getConfig();
    boolean fullPageScreenshotActivatedInGalen = galenConfig.getBooleanProperty(GalenProperty.SCREENSHOT_FULLPAGE);
    if (!fullPageScreenshotActivatedInGalen) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("activate full page screenshot in Galen before screenshot");
      }
      galenConfig.setProperty(GalenProperty.SCREENSHOT_FULLPAGE, "true");
    }
    try {
      File screenshotFile = GalenUtils.makeFullScreenshot(GaleniumContext.getDriver());
      handleScreenshotFile(screenshotFile);
    }
    catch (IOException | InterruptedException ex) {
      LOG.error("Could not take full screenshot.", ex);
    }
    finally {
      if (!fullPageScreenshotActivatedInGalen) {
        if (LOG.isTraceEnabled()) {
          LOG.trace("deactivate full page screenshot in Galen after screenshot");
        }
        galenConfig.setProperty(GalenProperty.SCREENSHOT_FULLPAGE, "false");
      }

    }
  }

  /**
   * Take screenshot of current browser window and add to reports. Uses random filename.
   *
   * @since 4.0.0
   */
  public static void takeScreenshot() {
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
    takeScreenshot(randomAlphanumeric, getTakesScreenshot());
  }

  /**
   * Take screenshot of current browser window and add to reports.
   *
   * @param result to generate filename from
   * @param driver to take screenshot from
   * @since 4.0.0
   */
  public static void takeScreenshot(ITestResult result, WebDriver driver) {
    String resultName = getAlphanumericTestName(result);
    takeScreenshot(resultName, getTakesScreenshot(driver));
  }

  /**
   * Take screenshot of current browser window and add to reports.
   *
   * @param resultName to use in filename
   * @param takesScreenshot to take screenshot from
   * @since 4.0.0
   */
  public static void takeScreenshot(String resultName, TakesScreenshot takesScreenshot) {
    LOG.debug("taking screenshot: " + takesScreenshot);
    File screenshotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
    handleScreenshotFile(resultName, screenshotFile);
  }

  /**
   * Captures image from screenshot capable instance which can be the driver or a single element in page.
   *
   * @param takesScreenshot to capture
   * @since 4.0.0
   */
  public static void takeScreenshot(TakesScreenshot takesScreenshot) {
    String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(12);
    takeScreenshot(randomAlphanumeric, takesScreenshot);
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

  private static void handleScreenshotFile(File screenshotFile) {
    handleScreenshotFile(screenshotFile.getName(), screenshotFile);
  }

  private static void handleScreenshotFile(String resultName, File screenshotFile) {
    if (screenshotFile != null) {
      LOG.trace("screenshot taken: " + screenshotFile.getPath());
      try {
        String destFilename = System.currentTimeMillis() + "_" + resultName + ".png";
        File destFile = new File(PATH_SCREENSHOTS_ROOT, destFilename);
        FileUtils.copyFile(screenshotFile, destFile);
        LOG.trace("copied screenshot: " + destFile.getPath());
        Allure.addAttachment("Screenshot: " + resultName, "image/png", new FileInputStream(destFile), ".png");
        if (FileUtils.deleteQuietly(screenshotFile)) {
          LOG.trace("deleted screenshot file: " + screenshotFile.getPath());
        }
        else {
          LOG.trace("could not delete screenshot file: " + screenshotFile.getPath());
        }
      }
      catch (IOException ex) {
        LOG.error("Cannot copy screenshot.", ex);
      }
    }
    else {
      LOG.debug("screenshot file is null.");
    }
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
