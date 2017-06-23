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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Central place to integrate test run and environment configuration from Maven et al.
 */
public final class GaleniumConfiguration {

  private static final String DEFAULT_AUTHOR_PASS = "admin";
  private static final String DEFAULT_AUTHOR_USER = "admin";
  private static final String DEFAULT_BASE_URL = "http://localhost:4502";
  private static final String DEFAULT_EXPECTED_TEXTS_FILE = "./src/test/resources/galen/specs/expectedTexts.properties";
  private static final String DEFAULT_GRID_PORT = "4444";
  private static final String DEFAULT_REPORT_DIR = "./target/galenium-reports";
  private static final String DEFAULT_SPEC_PATH = "./target/specs";

  private static final String SYSTEM_PROPERTY_NAME_AUTHOR_PASS = "io.wcm.qa.aem.author.pass";
  private static final String SYSTEM_PROPERTY_NAME_AUTHOR_USER = "io.wcm.qa.aem.author.user";
  private static final String SYSTEM_PROPERTY_NAME_HTTP_PASS = "io.wcm.qa.http.pass";
  private static final String SYSTEM_PROPERTY_NAME_HTTP_USER = "io.wcm.qa.http.user";
  private static final String SYSTEM_PROPERTY_NAME_BASE_URL = "io.wcm.qa.baseUrl";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_BINARY_PATH = "galenium.webdriver.chrome.binary";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS = "galenium.webdriver.chrome.headless";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_WINDOWS_WORKAROUND = "galenium.webdriver.chrome.headless.windowsWorkaround";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_JS_TEST_PATH = "galenium.jsTestPath";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_SPEC_PATH = "galenium.specPath";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_SUPPRESS_AUTO_ADJUST_BROWSERSIZE = "galenium.suppressAutoAdjustBrowserSize";
  private static final String SYSTEM_PROPERTY_NAME_LAZY_DRIVER = "galenium.webdriver.lazy";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_CONFIG = "io.wcm.qa.extent.reportConfig";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY = "galenium.report.rootPath";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY = "galenium.report.galen.errorsOnly";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_MAX = "galenium.retryMax";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_BROWSER_INSTANTIATION_MAX = "galenium.webdriver.retryMax";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX = "galenium.sampling.image.chromefix";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_ACTUAL = "galenium.sampling.image.directory.actual";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_EXPECTED = "galenium.sampling.image.directory.expected";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_SAVE = "galenium.sampling.image.save";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_DIRECTORY = "galenium.sampling.text.directory";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_FILE = "galenium.sampling.text.file";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_SAVE = "galenium.sampling.text.save";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS = "galenium.sampling.verification.ignoreErrors";
  private static final String SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED = "galenium.screenshotOnSkipped";
  private static final String SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SUCCESS = "galenium.screenshotOnSuccess";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_HOST = "selenium.host";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_PORT = "selenium.port";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE = "selenium.runmode";
  private static final String SYSTEM_PROPERTY_NAME_SPARSE_REPORTING = "galenium.report.sparse";
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_ALWAYS_NEW = "galenium.webdriver.alwaysNew";

  private GaleniumConfiguration() {
    // do not instantiate
  }

  public static String getActualImagesDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_ACTUAL, DEFAULT_REPORT_DIR);
  }

  public static String getAuthorPass() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_AUTHOR_PASS, DEFAULT_AUTHOR_PASS);
  }

  public static String getAuthorUser() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_AUTHOR_USER, DEFAULT_AUTHOR_USER);
  }

  public static String getHttpPass() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_HTTP_PASS);
  }

  public static String getHttpUser() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_HTTP_USER);
  }

  public static String getBaseUrl() {
    String baseUrl = System.getProperty(SYSTEM_PROPERTY_NAME_BASE_URL, DEFAULT_BASE_URL);
    String httpUser = getHttpUser();
    String httpPass = getHttpPass();
    if (isNotBlank(httpUser) && isNotBlank(httpPass)) {
      getLogger().trace("adding basic http auth information");
      httpUser = attemptUrlEncoding("HTTP User", httpUser);
      httpPass = attemptUrlEncoding("HTTP Pass", httpPass);
      baseUrl = baseUrl.replace("//", "//" + httpUser + ":" + httpPass + "@");
    }
    return baseUrl;
  }

  private static String attemptUrlEncoding(String name, String valueToEncode) {
    String encodedValue;
    try {
      encodedValue = URLEncoder.encode(valueToEncode, "UTF-8");
    }
    catch (UnsupportedEncodingException ex) {
      GaleniumReportUtil.getLogger().debug("when URL encoding: " + name, ex);
      encodedValue = valueToEncode;
    }
    return encodedValue;
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

  public static String getChromeBinaryPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_CHROME_BINARY_PATH);
  }

  public static String getExpectedImagesDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_EXPECTED, DEFAULT_SPEC_PATH);
  }

  public static String getGalenJsTestPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_GALEN_JS_TEST_PATH, getGalenSpecPath());
  }

  public static String getGalenSpecPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_GALEN_SPEC_PATH, DEFAULT_SPEC_PATH);
  }

  public static String getGridHost() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_HOST);
  }

  public static int getGridPort() {
    return Integer.parseInt(System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_PORT, DEFAULT_GRID_PORT));
  }

  public static int getNumberOfRetries() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_RETRY_MAX, 2);
  }

  public static int getNumberOfBrowserInstantiationRetries() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_RETRY_BROWSER_INSTANTIATION_MAX, 0);
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

  public static String getReportDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  public static RunMode getRunMode() {
    return RunMode.valueOf(System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE).toUpperCase());
  }

  public static String getTextComparisonDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  public static String getTextComparisonFile() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_FILE, DEFAULT_EXPECTED_TEXTS_FILE);
  }

  public static boolean isChromeHeadless() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS);
  }

  public static boolean isChromeHeadlessWindowsWorkaround() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_WINDOWS_WORKAROUND);
  }

  public static boolean isFixChromeImageComparison() {
    if (System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX) == null) {
      // default is to fix chrome behavior
      return true;
    }
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX);
  }

  public static boolean isLazyWebDriverInitialization() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_LAZY_DRIVER);
  }

  public static boolean isOnlyReportGalenErrors() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY);
  }

  public static boolean isSamplingVerificationIgnore() {
    if (System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS) == null) {
      return isSaveSampledTexts();
    }
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS);
  }

  public static boolean isSaveSampledImages() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_SAVE);
  }

  public static boolean isSaveSampledTexts() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_SAVE);
  }

  public static boolean isSparseReporting() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SPARSE_REPORTING);
  }

  public static boolean isSuppressAutoAdjustBrowserSize() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_GALEN_SUPPRESS_AUTO_ADJUST_BROWSERSIZE);
  }

  public static boolean isTakeScreenshotOnSkippedTest() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED);
  }

  public static boolean isTakeScreenshotOnSuccessfulTest() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SUCCESS);
  }

  public static boolean isWebDriverAlwaysNew() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_ALWAYS_NEW);
  }
}
