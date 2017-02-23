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
package io.wcm.qa.galenium.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Central place to integrate test run and environment configuration from Maven et al.
 */
public final class GaleniumConfiguration {

  private static final String DEFAULT_GRID_PORT = "4444";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_PORT = "selenium.port";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE = "selenium.runmode";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_HOST = "selenium.host";
  private static final String SYSTEM_PROPERTY_NAME_SAVE_IMAGE_COMPARISON = "galenium.imageComparison.save";
  private static final String SYSTEM_PROPERTY_NAME_SAVE_IMAGE_COMPARISON_DIRECTORY = "galenium.imageComparison.directory";
  private static final String SYSTEM_PROPERTY_NAME_SAVE_TEXT_COMPARISON = "galenium.textComparison.save";
  private static final String SYSTEM_PROPERTY_NAME_SAVE_TEXT_COMPARISON_DIRECTORY = "galenium.textComparison.directory";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_MAX = "galenium.retryMax";
  private static final String SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED = "galenium.screenshotOnSkipped";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY = "galenium.report.rootPath";
  private static final String SYSTEM_PROPERTY_NAME_SPEC_PATH = "galenium.specPath";
  private static final String SYSTEM_PROPERTY_NAME_BASE_URL = "io.wcm.qa.baseUrl";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_CONFIG = "io.wcm.qa.extent.reportConfig";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY = "galenium.report.galen.errorsOnly";
  private static final String SYSTEM_PROPERTY_NAME_SPARSE_REPORTING = "galenium.report.sparse";

  private static final String DEFAULT_BASE_URL = "http://localhost:4502";
  private static final String DEFAULT_SPEC_PATH = "./target/specs";
  private static final String DEFAULT_REPORT_DIR = "./target/galenium-reports";

  private GaleniumConfiguration() {
    // do not instantiate
  }

  public static String getBaseUrl() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_BASE_URL, DEFAULT_BASE_URL);
  }

  public static String getGalenSpecPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SPEC_PATH, DEFAULT_SPEC_PATH);
  }

  public static String getReportDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  public static String getImageComparisonDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAVE_IMAGE_COMPARISON_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  public static String getTextComparisonDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAVE_TEXT_COMPARISON_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  public static boolean isTakeScreenshotOnSkippedTest() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED);
  }

  public static int getNumberOfRetries() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_RETRY_MAX, 2);
  }

  public static boolean isSaveSampledImages() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAVE_IMAGE_COMPARISON);
  }

  public static boolean isSaveSampledTexts() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAVE_TEXT_COMPARISON);
  }

  public static RunMode getRunMode() {
    return RunMode.valueOf(System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE).toUpperCase());
  }

  public static String getGridHost() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_HOST);
  }

  public static int getGridPort() {
    return Integer.parseInt(System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_PORT, DEFAULT_GRID_PORT));
  }

  /**
   * @return browsers configured via 'selenium.browser' system property.
   */
  public static List<BrowserType> getBrowserTypes() {
    ArrayList<BrowserType> list = new ArrayList<BrowserType>();

    String browsers = System.getProperty("selenium.browser");
    if (StringUtils.isNotBlank(browsers)) {
      for (String browserTypeString : browsers.split(",")) {
        try {
          list.add(BrowserType.valueOf(browserTypeString.toUpperCase()));
        }
        catch (IllegalArgumentException iaex) {
          throw new RuntimeException("Illegal BrowserType: " + browsers, iaex);
        }
      }
      return Collections.unmodifiableList(list);
    }

    return Arrays.asList(BrowserType.CHROME);
  }

  /**
   * @return config file for ExtentReports
   */
  public static File getReportConfig() {
    String configPath = System.getProperty(SYSTEM_PROPERTY_NAME_REPORT_CONFIG);
    if (StringUtils.isNotBlank(configPath)) {
      File configFile = new File(configPath);
      if (configFile.isFile()) {
        return configFile;
      }
    }
    return null;
  }

  public static boolean isOnlyReportGalenErrors() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY);
  }

  public static boolean isSparseReporting() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SPARSE_REPORTING);
  }
}
