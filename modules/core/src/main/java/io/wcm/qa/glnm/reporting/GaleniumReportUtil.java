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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.config.GalenConfig;
import com.galenframework.config.GalenProperty;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.HtmlReportBuilder;
import com.galenframework.utils.GalenUtils;
import com.google.common.html.HtmlEscapers;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Attachment;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.exceptions.GaleniumException;

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
   * <p>
   * Adds image comparison result for use with Allure's screen-diff-plugin.
   * </p>
   *
   * @param actual a {@link java.io.File} object.
   * @param expected a {@link java.io.File} object.
   * @param diff a {@link java.io.File} object.
   */
  public static void addImageComparisonResult(File actual, File expected, File diff) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("attaching screendiff results.");
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace("actual: " + actual);
      LOG.trace("expected: " + actual);
      LOG.trace("diff: " + actual);
    }

    // Add label [key='testType', value='screenshotDiff'] to testcase
    addLabel("testType", "screenshotDiff");

    // Attach to testcase three screenshots:
    //  * diff.png
    addPngAttachment("diff", diff, true);
    //  * actual.png
    addPngAttachment("actual", actual, true);
    //  * expected.png
    addPngAttachment("expected", expected, true);

  }

  /**
   * Add label to test case.
   *
   * @param key label key
   * @param value label value
   */
  public static void addLabel(String key, String value) {
    Allure.label(key, value);
  }

  /**
   * Add attachment with "image/png" and ".png".
   *
   * @param name base name
   * @param file file to create input stream from
   */
  public static void addPngAttachment(String name, File file) {
    addPngAttachment(name, file, false);
  }

  /**
   * Add attachment with "image/png" and ".png".
   *
   * @param name base name
   * @param file file to create input stream from
   * @param attachToTestCase whether to attach at test case level instead of inside step
   */
  public static void addPngAttachment(String name, File file, boolean attachToTestCase) {
    FileInputStream inputStream;
    try {
      inputStream = new FileInputStream(file);
      addAttachment(
          name,
          "image/png",
          inputStream,
          ".png", attachToTestCase);
      inputStream.close();
    }
    catch (IOException ex) {
      throw new GaleniumException("When adding PNG attachment from: " + file, ex);
    }
  }

  /**
   * Write all test results to Galen report.
   *
   * @param testInfos list to persist test information
   * @since 3.0.0
   */
  @SuppressWarnings("PMD.AvoidCatchingNPE")
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
   * <p>failStep.</p>
   *
   * @param step a {@link java.lang.String} object.
   * @since 5.0.0
   */
  public static void failStep(String step) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("fail step: " + step);
    }
    Allure.getLifecycle().updateStep(step, new FailStep());
  }

  /**
   * <p>passStep.</p>
   *
   * @param step a {@link java.lang.String} object.
   * @since 5.0.0
   */
  public static void passStep(String step) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("pass step: " + step);
    }
    Allure.getLifecycle().updateStep(step, new PassStep());
  }

  /**
   * <p>startStep.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   * @since 5.0.0
   */
  public static String startStep(String name) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("start step: " + name);
    }
    String uuid = UUID.randomUUID().toString();
    StepResult result = new StepResult().setName(name);
    Allure.getLifecycle().startStep(uuid, result);
    return uuid;
  }

  /**
   * <p>startStep.</p>
   *
   * @param parentStep a {@link java.lang.String} object.
   * @param stepName a {@link java.lang.String} object.
   * @return a {@link java.lang.String} object.
   * @since 5.0.0
   */
  public static String startStep(String parentStep, String stepName) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("start step: " + stepName);
    }
    String uuid = UUID.randomUUID().toString();
    StepResult result = new StepResult().setName(stepName);
    Allure.getLifecycle().startStep(parentStep, uuid, result);
    return uuid;
  }

  /**
   * Adds a step for current test case to the report.
   *
   * @param stepName to use in report
   * @since 5.0.0
   */
  public static void step(String stepName) {
    if (LOG.isInfoEnabled()) {
      LOG.info("STEP(" + stepName + ")");
    }
    Allure.step(stepName);
  }

  /**
   * Adds a failed step for current test case to the report.
   *
   * @param stepName to use in report
   * @since 5.0.0
   */
  public static void stepFailed(String stepName) {
    if (LOG.isInfoEnabled()) {
      LOG.info("FAILED_STEP(" + stepName + ")");
    }
    Allure.step(stepName, Status.FAILED);
  }

  /**
   * <p>stopStep.</p>
   *
   * @since 5.0.0
   */
  public static void stopStep() {
    if (LOG.isTraceEnabled()) {
      LOG.trace("stop step");
    }
    Allure.getLifecycle().stopStep();
  }

  /**
   * uses Galen functionality to get a full page screenshot by scrolling and
   * stitching.
   *
   * @since 5.0.0
   */
  public static void takeFullScreenshot() {
    String step = startStep("taking full page screenshot");
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
      attachScreenshotFile(screenshotFile);
      passStep(step);
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
      stopStep();
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
   * Take screenshot of current browser window and add to report in a dedicated step.
   *
   * @param resultName to use in filename
   * @param takesScreenshot to take screenshot from
   * @since 4.0.0
   */
  public static void takeScreenshot(String resultName, TakesScreenshot takesScreenshot) {
    takeScreenshot(resultName, takesScreenshot, true);
  }

  /**
   * Take screenshot of current browser window and add to report in a dedicated step.
   *
   * @param resultName to use in filename
   * @param takesScreenshot to take screenshot from
   * @param dedicatedStep whether to create dedicated step for attaching screenshot
   * @since 5.0.0
   */
  public static void takeScreenshot(String resultName, TakesScreenshot takesScreenshot, boolean dedicatedStep) {
    String step = null;
    if (dedicatedStep) {
      step = startStep("taking screenshot: " + takesScreenshot);
    }
    File screenshotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
    attachScreenshotFile(resultName, screenshotFile);
    if (dedicatedStep) {
      passStep(step);
      stopStep();
    }
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

  /**
   * <p>
   * updateStep.
   * </p>
   *
   * @param step step UUID
   * @param update step result consumer to do the update
   * @since 5.0.0
   */
  public static void updateStep(String step, Consumer<StepResult> update) {
    Allure.getLifecycle().updateStep(step, update);
  }

  /**
   * Sets a new name for a step.
   *
   * @param step step UUID
   * @param updatedStepName name to set on step
   * @since 5.0.0
   */
  public static void updateStepName(String step, String updatedStepName) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("update step name: " + step + " -> " + updatedStepName);
    }
    updateStep(step, new Consumer<StepResult>() {

      @Override
      public void accept(StepResult result) {
        result.setName(updatedStepName);
      }
    });
  }

  @SuppressWarnings("deprecation")
  private static void addAttachment(String name, String type, FileInputStream inputStream, String extension, boolean attachToTestCase) {
    if (attachToTestCase) {
      attachToTestCase(name, type, inputStream, extension);
      return;
    }
    attachToCurrentStep(name, type, inputStream, extension);
  }

  private static void attachScreenshotFile(File screenshotFile) {
    attachScreenshotFile(screenshotFile.getName(), screenshotFile);
  }

  private static void attachScreenshotFile(String resultName, File screenshotFile) {
    if (screenshotFile != null) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("screenshot taken: " + screenshotFile.getPath());
      }
      try {
        String destFilename = System.currentTimeMillis() + "_" + resultName + ".png";
        File destFile = new File(PATH_SCREENSHOTS_ROOT, destFilename);
        FileUtils.copyFile(screenshotFile, destFile);
        if (LOG.isTraceEnabled()) {
          LOG.trace("copied screenshot: " + destFile.getPath());
        }
        addPngAttachment("Screenshot: " + resultName, destFile);
        if (FileUtils.deleteQuietly(screenshotFile)) {
          if (LOG.isTraceEnabled()) {
            LOG.trace("deleted screenshot file: " + screenshotFile.getPath());
          }
        }
        else if (LOG.isTraceEnabled()) {
          LOG.trace("could not delete screenshot file: " + screenshotFile.getPath());
        }
      }
      catch (IOException ex) {
        LOG.error("Cannot copy screenshot.", ex);
      }
    }
    else if (LOG.isDebugEnabled()) {
      LOG.debug("screenshot file is null.");
    }
  }

  private static void attachToCurrentStep(String name, String type, FileInputStream inputStream, String extension) {
    Allure.addAttachment(name, type, inputStream, extension);
  }

  private static void attachToTestCase(String name, String type, FileInputStream inputStream, String extension) {
    AllureLifecycle lifecycle = Allure.getLifecycle();
    String source = lifecycle.prepareAttachment(name, type, extension);
    lifecycle.writeAttachment(source, inputStream);
    Attachment attachment = new Attachment();
    attachment.setName(name);
    attachment.setSource(source);
    lifecycle.updateTestCase(result -> result.getAttachments().add(attachment));
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

  private static final class FailStep implements Consumer<StepResult> {

    @Override
    public void accept(StepResult t) {
      t.setStatus(Status.FAILED);
    }
  }

  private static final class PassStep implements Consumer<StepResult> {

    @SuppressWarnings("deprecation")
    @Override
    public void accept(StepResult t) {
      if (t.getStatus() == null) {
        t.setStatus(Status.PASSED);
      }
    }
  }
}
