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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.device.BrowserType;
import io.wcm.qa.galenium.selenium.RunMode;

/**
 * Central place to integrate test run and environment configuration from Maven et al.
 */
public final class GaleniumConfiguration {

  private static final String DEFAULT_AUTHOR_PASS = "admin";
  private static final String DEFAULT_AUTHOR_USER = "admin";
  private static final String DEFAULT_BASE_URL = "http://localhost:4502";
  private static final String DEFAULT_BROWSER_LOG_LEVEL = "INFO";
  private static final String DEFAULT_EXPECTED_TEXTS_FILE = "./src/test/resources/galen/specs/expectedTexts.properties";
  private static final String DEFAULT_GRID_PORT = "4444";
  private static final String DEFAULT_MEDIA_QUERY_PATH = "./target/test-classes/mediaqueries.properties";
  private static final String DEFAULT_REPORT_DIR = "./target/galenium-reports";
  private static final String DEFAULT_SPEC_PATH = "./target/specs";

  private static final String SYSTEM_PROPERTY_NAME_AUTHOR_PASS = "io.wcm.qa.aem.author.pass";
  private static final String SYSTEM_PROPERTY_NAME_AUTHOR_USER = "io.wcm.qa.aem.author.user";
  private static final String SYSTEM_PROPERTY_NAME_BASE_URL = "io.wcm.qa.baseUrl";
  private static final String SYSTEM_PROPERTY_NAME_BROWSER_LOG_LEVEL = "galenium.webdriver.browser.loglevel";
  private static final String SYSTEM_PROPERTY_NAME_BROWSERMOB_PROXY = "galenium.browsermob.proxy";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_BINARY_PATH = "galenium.webdriver.chrome.binary";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS = "galenium.webdriver.chrome.headless";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_ADDITIONAL_WIDTH = "galenium.webdriver.chrome.headless.additionalWidth";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_WINDOWS_WORKAROUND = "galenium.webdriver.chrome.headless.windowsWorkaround";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_JS_TEST_PATH = "galenium.jsTestPath";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_SPEC_PATH = "galenium.specPath";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_SUPPRESS_AUTO_ADJUST_BROWSERSIZE = "galenium.suppressAutoAdjustBrowserSize";
  private static final String SYSTEM_PROPERTY_NAME_HTTP_PASS = "io.wcm.qa.http.pass";
  private static final String SYSTEM_PROPERTY_NAME_HTTP_USER = "io.wcm.qa.http.user";
  private static final String SYSTEM_PROPERTY_NAME_LAZY_DRIVER = "galenium.webdriver.lazy";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_PROPERTIES = "galenium.mediaquery.properties";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_HEIGHT = "galenium.mediaquery.height";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MIN = "galenium.mediaquery.width.min";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MAX = "galenium.mediaquery.width.max";
  private static final String SYSTEM_PROPERTY_NAME_NO_TESTNG = "galenium.noTestNG";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_CONFIG = "io.wcm.qa.extent.reportConfig";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY = "galenium.report.rootPath";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY = "galenium.report.galen.errorsOnly";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_SKIP_EXTENT = "galenium.report.extent.skip";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_BROWSER_INSTANTIATION_MAX = "galenium.webdriver.retryMax";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_MAX = "galenium.retryMax";
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
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_REFUSE = "galenium.webdriver.ssl.refuse";
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_TRUSTED_ONLY = "galenium.webdriver.ssl.trusted";

  private GaleniumConfiguration() {
    // do not instantiate
  }

  /**
   * The root directory for storing sample images of current run.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.image.directory.actual
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./target/galenium-reports"
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to root folder
   */
  public static String getActualImagesDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_ACTUAL, DEFAULT_REPORT_DIR);
  }

  /**
   * Additional width when starting Chrome headless. Galen cannot resize headless browsers, so the size is not the
   * viewport, but the whole window including potential scroll bars.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.chrome.headless.additionalWidth
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 0
   * </pre>
   *
   * </li>
   * </ul>
   * @return amount of pixel to add to width
   */
  public static int getAdditionalChromeHeadlessWidth() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_ADDITIONAL_WIDTH, 0);
  }

  /**
   * Password to use when logging into author.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * io.wcm.qa.aem.author.pass
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "admin"
   * </pre>
   *
   * </li>
   * </ul>
   * @return password to author instance
   */
  public static String getAuthorPass() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_AUTHOR_PASS, DEFAULT_AUTHOR_PASS);
  }

  /**
   * Username to use when logging into author.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * io.wcm.qa.aem.author.user
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "admin"
   * </pre>
   *
   * </li>
   * </ul>
   * @return username for author instance
   */
  public static String getAuthorUser() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_AUTHOR_USER, DEFAULT_AUTHOR_USER);
  }

  /**
   * The base URL to use when running tests. This is the URL called from the browsers when controlled by Selenium. You
   * can add basic HTTP auth if needed for your SUTs.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * io.wcm.qa.baseUrl
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "http://localhost:4502"
   * </pre>
   *
   * </li>
   * </ul>
   * @return base URL with HTTP basic auth, if configured
   */
  public static String getBaseUrl() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_BASE_URL, DEFAULT_BASE_URL);
  }

  /**
   * Log level for browser log output. Default INFO.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.browser.loglevel
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * {@link Level#INFO}
   * </pre>
   *
   * </li>
   * </ul>
   * @return level to use for logs from browser
   */
  public static Level getBrowserLogLevel() {
    try {
      return Level.parse(System.getProperty(SYSTEM_PROPERTY_NAME_BROWSER_LOG_LEVEL, DEFAULT_BROWSER_LOG_LEVEL));
    }
    catch (IllegalArgumentException ex) {
      getLogger().info("could not parse browser log level, using INFO level.", ex);
      return Level.INFO;
    }
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
   * Path to Chrome binary which is passed to driver.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.chrome.binary
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * null
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to chrome binary or null
   */
  public static String getChromeBinaryPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_CHROME_BINARY_PATH);
  }

  /**
   * Path to root folder of expected image samples to verify against.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.image.directory.expected
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./target/specs"
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to root folder of expected image samples
   */
  public static String getExpectedImagesDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_EXPECTED, DEFAULT_SPEC_PATH);
  }

  /**
   * Path to root folder containing Galen JS tests.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.jsTestPath
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * {@link #getGalenSpecPath()}
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to root folder of Galen JS tests
   */
  public static String getGalenJsTestPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_GALEN_JS_TEST_PATH, getGalenSpecPath());
  }

  /**
   * Path to root folder containing Galen specs.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.specPath
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./target/specs"
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to root folder of Galen specs
   */
  public static String getGalenSpecPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_GALEN_SPEC_PATH, DEFAULT_SPEC_PATH);
  }

  /**
   * Selenium Grid hostname or address.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * selenium.host
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * null
   * </pre>
   *
   * </li>
   * </ul>
   * @return Selenium Grid host
   */
  public static String getGridHost() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_HOST);
  }

  /**
   * Selenium Grid port number.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * selenium.port
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 4444
   * </pre>
   *
   * </li>
   * </ul>
   * @return Selenium Grid port number
   */
  public static int getGridPort() {
    return Integer.parseInt(System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_PORT, DEFAULT_GRID_PORT));
  }

  /**
   * HTTP password to use in HTTP basic auth.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * io.wcm.qa.http.pass
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * null
   * </pre>
   *
   * </li>
   * </ul>
   * @return HTTP password
   */
  public static String getHttpPass() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_HTTP_PASS);
  }

  /**
   * HTTP username to use in HTTP basic auth.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * io.wcm.qa.http.user
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * null
   * </pre>
   *
   * </li>
   * </ul>
   * @return HTTP username
   */
  public static String getHttpUser() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_HTTP_USER);
  }

  /**
   * Path to media query definitions.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.mediaquery.properties
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * ./target/test-classes/mediaqueries.properties
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to {@link Properties} file containing media query definitions
   */
  public static String getMediaQueryPropertiesPath() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_PROPERTIES, DEFAULT_MEDIA_QUERY_PATH);
  }

  /**
   * Height to use when instantiating devices using media queries.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.mediaquery.height
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 800
   * </pre>
   *
   * </li>
   * </ul>
   * @return height for use with device creation
   */
  public static Integer getMediaQueryHeight() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_HEIGHT, 800);
  }

  /**
   * Height to use when instantiating devices using media queries.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.mediaquery.width.max
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 2000
   * </pre>
   *
   * </li>
   * </ul>
   * @return minimal width to use with media query instantiation
   */
  public static Integer getMediaQueryMaximalWidth() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MAX, 2000);
  }

  /**
   * Height to use when instantiating devices using media queries.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.mediaquery.width.min
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 320
   * </pre>
   *
   * </li>
   * </ul>
   * @return minimal width to use with media query instantiation
   */
  public static Integer getMediaQueryMinimalWidth() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MIN, 320);
  }

  /**
   * Number of retries when instantiating browsers. Use this to handle flaky browser initializations.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.retryMax
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 0
   * </pre>
   *
   * </li>
   * </ul>
   * @return HTTP username
   */
  public static int getNumberOfBrowserInstantiationRetries() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_RETRY_BROWSER_INSTANTIATION_MAX, 0);
  }

  /**
   * Number of retries for failed tests. Use this to handle flaky test results.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.retryMax
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 2
   * </pre>
   *
   * </li>
   * </ul>
   * @return maximum number of retries
   */
  public static int getNumberOfRetries() {
    return Integer.getInteger(SYSTEM_PROPERTY_NAME_RETRY_MAX, 2);
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

  /**
   * Report root folder.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.report.rootPath
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./target/galenium-reports"
   * </pre>
   *
   * </li>
   * </ul>
   * @return root folder for storing report artefacts
   */
  public static String getReportDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  /**
   * Selenium run mode.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * selenium.runmode
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * throw {@link NullPointerException}
   * </pre>
   *
   * </li>
   * </ul>
   * @return {@link RunMode} used
   */
  public static RunMode getRunMode() {
    return RunMode.valueOf(System.getProperty(SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE).toUpperCase());
  }

  /**
   * Directory to store the text samples in.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.text.directory
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./target/galenium-reports"
   * </pre>
   *
   * </li>
   * </ul>
   * @return {@link RunMode} used
   */
  public static String getTextComparisonDirectory() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  /**
   * Properties file to retrieve expected text samples from.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.text.file
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./src/test/resources/galen/specs/expectedTexts.properties"
   * </pre>
   *
   * </li>
   * </ul>
   * @return path to expected texts file
   */
  public static String getTextComparisonFile() {
    return System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_FILE, DEFAULT_EXPECTED_TEXTS_FILE);
  }

  /**
   * BrowserMob Proxy flag.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.browsermob.proxy
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to use BrowserMob Proxy for drivers
   */
  public static boolean isUseBrowserMobProxy() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_BROWSERMOB_PROXY);
  }

  /**
   * Headless Chrome flag.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.chrome.headless
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether Chrome is running in headless mode
   */
  public static boolean isChromeHeadless() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS);
  }

  /**
   * Headless Chrome Windows workaround flag.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.chrome.headless.windowsWorkaround
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether headless Chrome should be position off screen on Windows machines
   */
  public static boolean isChromeHeadlessWindowsWorkaround() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_WINDOWS_WORKAROUND);
  }

  /**
   * Headless Chrome Windows workaround flag.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.image.chromefix
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * true
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to use workaround to fix Chrome's image comparison behavior
   */
  public static boolean isFixChromeImageComparison() {
    if (System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX) == null) {
      // default is to fix chrome behavior
      return true;
    }
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX);
  }

  /**
   * Lazy web driver intialization takes control away from WebDriverListener and lets the test itself decide
   * when or whether to initialize a webdriver.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.lazy
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to use lazy web driver initialization
   */
  public static boolean isLazyWebDriverInitialization() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_LAZY_DRIVER);
  }

  /**
   * Usually Galenium is used for testing with TestNG. If not this property should reflect that.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.noTestNG
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether TestNG is used
   */
  public static boolean isNoTestNg() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_NO_TESTNG);
  }

  /**
   * Only report Galen errors and leave successful tests out of report. This drastically reduces the size of reports in
   * the target folder. Useful to save disk space on CI.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.report.galen.errorsOnly
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to skip reporting of successful tests
   */
  public static boolean isOnlyReportGalenErrors() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY);
  }

  /**
   * Ignore sampling verification errors and continue running test. This is useful when generating initial samples.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.verification.ignoreErrors
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * {@link #isSaveSampledTexts()}
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to ignore errors when validating samples
   */
  public static boolean isSamplingVerificationIgnore() {
    if (System.getProperty(SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS) == null) {
      return isSaveSampledTexts();
    }
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS);
  }

  /**
   * Store image samples to disk.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.image.save
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to save sampled images to disk
   */
  public static boolean isSaveSampledImages() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_SAVE);
  }

  /**
   * Store text samples to properties file on disk.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.text.save
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to save sampled texts to disk
   */
  public static boolean isSaveSampledTexts() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_SAVE);
  }

  /**
   * Usually Galenium will log to ExtentReports, but it can be skipped.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.report.extent.skip
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether TestNG is used
   */
  public static boolean isSkipExtentReports() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_REPORT_SKIP_EXTENT);
  }

  /**
   * Sparse reporting is a different set of logging configuration. This allows to configure to different kinds of
   * reporting for local development with more debugging needs and for CI which should give you a quick view of the
   * important test steps and their results.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.report.sparse
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to use sparse reporting on this run
   */
  public static boolean isSparseReporting() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SPARSE_REPORTING);
  }

  /**
   * Adjusting the browser size is important to ensure the correct viewport size, but it can lead to problems with
   * certain browser/driver/OS combinations.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.suppressAutoAdjustBrowserSize
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to skip automatic adjusting of browser size
   */
  public static boolean isSuppressAutoAdjustBrowserSize() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_GALEN_SUPPRESS_AUTO_ADJUST_BROWSERSIZE);
  }

  /**
   * Take screenshots for skipped tests.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.screenshotOnSkipped
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to take and store screenshots of skipped tests for reporting
   */
  public static boolean isTakeScreenshotOnSkippedTest() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED);
  }

  /**
   * Take screenshots for successful tests.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.screenshotOnSuccess
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to take and store screenshots of successful tests for reporting
   */
  public static boolean isTakeScreenshotOnSuccessfulTest() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SUCCESS);
  }

  /**
   * Control whether to only accept secure SSL certificates
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.ssl.trusted
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether browser accepts secure SSL certificates only
   */
  public static boolean isWebDriverAcceptTrustedSslCertificatesOnly() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_TRUSTED_ONLY);
  }

  /**
   * Control whether to attempt reuse of drivers.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.alwaysNew
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether to always instantiate a new webdriver for each test
   */
  public static boolean isWebDriverAlwaysNew() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_ALWAYS_NEW);
  }

  /**
   * Control whether to refuse SSL certificates
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.ssl.refuse
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * false
   * </pre>
   *
   * </li>
   * </ul>
   * @return whether browser refuses SSL certificates
   */
  public static boolean isWebDriverRefuseSslCertificates() {
    return Boolean.getBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_REFUSE);
  }
}
