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
package io.wcm.qa.glnm.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.device.BrowserType;
import io.wcm.qa.glnm.selenium.RunMode;

/**
 * Central place to integrate test run and environment configuration from Maven et al.
 *
 * @since 1.0.0
 */
public final class GaleniumConfiguration {

  private static final String DEFAULT_AUTHOR_PASS = "admin";

  private static final String DEFAULT_AUTHOR_USER = "admin";
  private static final String DEFAULT_BASE_URL = "http://localhost:4502";
  private static final String DEFAULT_BROWSER_LOG_LEVEL = "INFO";
  private static final String DEFAULT_DEVICE_CSV = "./target/test-classes/devices.csv";
  private static final String DEFAULT_EXPECTED_TEXTS_FILE = "text/expectedTexts.properties";
  private static final int DEFAULT_GRID_PORT = 4444;
  private static final String DEFAULT_MEDIA_QUERY_PATH = "./target/test-classes/mediaqueries.properties";
  private static final String DEFAULT_REPORT_DIR = "./target/galenium-reports";
  private static final String DEFAULT_SPEC_PATH = "./target/classes/galen/specs";
  private static final int DEFAULT_WEBDRIVER_TIMEOUT = 10;

  private static final Logger LOG = LoggerFactory.getLogger(GaleniumConfiguration.class);

  private static final String SYSTEM_PROPERTY_NAME_AUTHOR_PASS = "io.wcm.qa.aem.author.pass";
  private static final String SYSTEM_PROPERTY_NAME_AUTHOR_USER = "io.wcm.qa.aem.author.user";
  private static final String SYSTEM_PROPERTY_NAME_BASE_URL = "io.wcm.qa.baseUrl";
  private static final String SYSTEM_PROPERTY_NAME_BROWSER_LOG_LEVEL = "galenium.webdriver.browser.loglevel";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_BINARY_PATH = "galenium.webdriver.chrome.binary";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_ADDITIONAL_WIDTH = "galenium.webdriver.chrome.headless.additionalWidth";
  private static final String SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_WINDOWS_WORKAROUND = "galenium.webdriver.chrome.headless.windowsWorkaround";
  private static final String SYSTEM_PROPERTY_NAME_DEVICE_CSV = "galenium.devices.csv";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_JS_TEST_PATH = "galenium.jsTestPath";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_SPEC_PATH = "galenium.specPath";
  private static final String SYSTEM_PROPERTY_NAME_GALEN_SUPPRESS_AUTO_ADJUST_BROWSERSIZE = "galenium.suppressAutoAdjustBrowserSize";
  private static final String SYSTEM_PROPERTY_NAME_HEADLESS = "galenium.headless";
  private static final String SYSTEM_PROPERTY_NAME_HTTP_PASS = "io.wcm.qa.http.pass";
  private static final String SYSTEM_PROPERTY_NAME_HTTP_USER = "io.wcm.qa.http.user";
  private static final String SYSTEM_PROPERTY_NAME_LAZY_DRIVER = "galenium.webdriver.lazy";
  private static final String SYSTEM_PROPERTY_NAME_LOG_DIR = "galenium.logging.testlogs";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_HEIGHT = "galenium.mediaquery.height";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_PROPERTIES = "galenium.mediaquery.properties";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MAX = "galenium.mediaquery.width.max";
  private static final String SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MIN = "galenium.mediaquery.width.min";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY = "galenium.report.rootPath";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY = "galenium.report.galen.errorsOnly";
  private static final String SYSTEM_PROPERTY_NAME_REPORT_SKIP_EXTENT = "galenium.report.extent.skip";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_BROWSER_INSTANTIATION_MAX = "galenium.webdriver.retryMax";
  private static final String SYSTEM_PROPERTY_NAME_RETRY_MAX = "galenium.retryMax";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX = "galenium.sampling.image.chromefix";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_ACTUAL = "galenium.sampling.image.directory.actual";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_EXPECTED = "galenium.sampling.image.directory.expected";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_FILE = "galenium.sampling.text.file";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_INPUT_DIRECTORY = "galenium.sampling.text.directory.expected";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_OUTPUT_DIRECTORY = "galenium.sampling.text.directory.actual";
  private static final String SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS = "galenium.sampling.verification.ignoreErrors";
  private static final String SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED = "galenium.screenshotOnSkipped";
  private static final String SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SUCCESS = "galenium.screenshotOnSuccess";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_BROWSER = "selenium.browser";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_HOST = "selenium.host";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_PORT = "selenium.port";
  private static final String SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE = "selenium.runmode";
  private static final String SYSTEM_PROPERTY_NAME_SPARSE_REPORTING = "galenium.report.sparse";
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_ALWAYS_NEW = "galenium.webdriver.alwaysNew";
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_REFUSE = "galenium.webdriver.ssl.refuse";
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_TRUSTED_ONLY = "galenium.webdriver.ssl.trusted";
  private static final String SYSTEM_PROPERTY_NAME_WEB_DRIVER_TIMEOUT = "galenium.webdriver.timeout";

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
   *
   * @return path to root folder
   * @since 3.0.0
   */
  public static String getActualImagesDirectory() {
    return asString(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_ACTUAL, DEFAULT_REPORT_DIR);
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
   *
   * @return amount of pixel to add to width
   * @since 3.0.0
   */
  public static int getAdditionalChromeHeadlessWidth() {
    return asInteger(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_ADDITIONAL_WIDTH, 0);
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
   *
   * @return password to author instance
   * @since 3.0.0
   */
  public static String getAuthorPass() {
    return asString(SYSTEM_PROPERTY_NAME_AUTHOR_PASS, DEFAULT_AUTHOR_PASS);
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
   *
   * @return username for author instance
   * @since 3.0.0
   */
  public static String getAuthorUser() {
    return asString(SYSTEM_PROPERTY_NAME_AUTHOR_USER, DEFAULT_AUTHOR_USER);
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
   *
   * @return base URL with HTTP basic auth, if configured
   * @since 3.0.0
   */
  public static String getBaseUrl() {
    return asString(SYSTEM_PROPERTY_NAME_BASE_URL, DEFAULT_BASE_URL);
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
   * {@link java.util.logging.Level#INFO}
   * </pre>
   *
   * </li>
   * </ul>
   *
   * @return level to use for logs from browser
   * @since 3.0.0
   */
  public static Level getBrowserLogLevel() {
    try {
      return Level.parse(asString(SYSTEM_PROPERTY_NAME_BROWSER_LOG_LEVEL, DEFAULT_BROWSER_LOG_LEVEL));
    }
    catch (IllegalArgumentException ex) {
      LOG.info("could not parse browser log level, using INFO level.", ex);
      return Level.INFO;
    }
  }

  /**
   * <p>getBrowserTypes.</p>
   *
   * @return browsers configured via 'selenium.browser' system property.
   * @since 3.0.0
   */
  public static List<BrowserType> getBrowserTypes() {
    ArrayList<BrowserType> list = new ArrayList<BrowserType>();

    String browsers = asString(SYSTEM_PROPERTY_NAME_SELENIUM_BROWSER);
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

    LOG.warn("No browser configured: using Chrome");
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
   *
   * @return path to chrome binary or null
   * @since 3.0.0
   */
  public static String getChromeBinaryPath() {
    return asString(SYSTEM_PROPERTY_NAME_CHROME_BINARY_PATH);
  }

  /**
   * Default timeout for webdriver in seconds.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.webdriver.timeout
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * 10
   * </pre>
   *
   * </li>
   * </ul>
   *
   * @return default timeout used by Galenium
   * @since 3.0.0
   */
  public static int getDefaultWebdriverTimeout() {
    return asInteger(SYSTEM_PROPERTY_NAME_WEB_DRIVER_TIMEOUT, DEFAULT_WEBDRIVER_TIMEOUT);
  }

  /**
   * Path to device profile definitions.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.devices.csv
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * ./target/test-classes/device.csv
   * </pre>
   *
   * </li>
   * </ul>
   *
   * @return path to {@link java.util.Properties} file containing media query definitions
   * @since 3.0.0
   */
  public static String getDeviceCsvFilePath() {
    return asString(SYSTEM_PROPERTY_NAME_DEVICE_CSV, DEFAULT_DEVICE_CSV);
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
   *
   * @return path to root folder of expected image samples
   * @since 3.0.0
   */
  public static String getExpectedImagesDirectory() {
    return asString(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_DIRECTORY_EXPECTED, DEFAULT_SPEC_PATH);
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
   *
   * @return path to root folder of Galen JS tests
   * @since 3.0.0
   */
  public static String getGalenJsTestPath() {
    return asString(SYSTEM_PROPERTY_NAME_GALEN_JS_TEST_PATH, getGalenSpecPath());
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
   *
   * @return path to root folder of Galen specs
   * @since 3.0.0
   */
  public static String getGalenSpecPath() {
    return asString(SYSTEM_PROPERTY_NAME_GALEN_SPEC_PATH, DEFAULT_SPEC_PATH);
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
   *
   * @return Selenium Grid host
   * @since 3.0.0
   */
  public static String getGridHost() {
    return asString(SYSTEM_PROPERTY_NAME_SELENIUM_HOST);
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
   *
   * @return Selenium Grid port number
   * @since 3.0.0
   */
  public static int getGridPort() {
    return asInteger(SYSTEM_PROPERTY_NAME_SELENIUM_PORT, DEFAULT_GRID_PORT);
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
   *
   * @return HTTP password
   * @since 3.0.0
   */
  public static String getHttpPass() {
    return asString(SYSTEM_PROPERTY_NAME_HTTP_PASS);
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
   *
   * @return HTTP username
   * @since 3.0.0
   */
  public static String getHttpUser() {
    return asString(SYSTEM_PROPERTY_NAME_HTTP_USER);
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
   *
   * @return height for use with device creation
   * @since 3.0.0
   */
  public static Integer getMediaQueryHeight() {
    return asInteger(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_HEIGHT, 800);
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
   *
   * @return minimal width to use with media query instantiation
   * @since 3.0.0
   */
  public static Integer getMediaQueryMaximalWidth() {
    return asInteger(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MAX, 2000);
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
   *
   * @return minimal width to use with media query instantiation
   * @since 3.0.0
   */
  public static Integer getMediaQueryMinimalWidth() {
    return asInteger(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_WIDTH_MIN, 320);
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
   *
   * @return path to {@link java.util.Properties} file containing media query definitions
   * @since 3.0.0
   */
  public static String getMediaQueryPropertiesPath() {
    return asString(SYSTEM_PROPERTY_NAME_MEDIA_QUERY_PROPERTIES, DEFAULT_MEDIA_QUERY_PATH);
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
   *
   * @return HTTP username
   * @since 3.0.0
   */
  public static int getNumberOfBrowserInstantiationRetries() {
    return asInteger(SYSTEM_PROPERTY_NAME_RETRY_BROWSER_INSTANTIATION_MAX, 0);
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
   *
   * @return maximum number of retries
   * @since 3.0.0
   */
  public static int getNumberOfRetries() {
    return asInteger(SYSTEM_PROPERTY_NAME_RETRY_MAX, 2);
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
   *
   * @return root folder for storing report artefacts
   * @since 3.0.0
   */
  public static String getReportDirectory() {
    return asString(SYSTEM_PROPERTY_NAME_REPORT_DIRECTORY, DEFAULT_REPORT_DIR);
  }

  /**
   * Test log root folder.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.logging.testlogs
   * </pre>
   *
   * </li>
   * <li>
   * Default:
   *
   * <pre>
   * "./target/testlogs"
   * </pre>
   *
   * </li>
   * </ul>
   *
   * @return root folder for storing test logs
   */
  public static String getTestLogDirectory() {
    return asString(SYSTEM_PROPERTY_NAME_LOG_DIR, "target/testlogs");
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
   * {@link io.wcm.qa.glnm.selenium.RunMode#LOCAL}
   * </pre>
   *
   * </li>
   * </ul>
   *
   * @return {@link io.wcm.qa.glnm.selenium.RunMode} used
   * @since 3.0.0
   */
  public static RunMode getRunMode() {
    String runModeAsString = asString(SYSTEM_PROPERTY_NAME_SELENIUM_RUNMODE);
    if (StringUtils.isBlank(runModeAsString)) {
      return RunMode.LOCAL;
    }
    return RunMode.valueOf(runModeAsString.toUpperCase());
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
   *
   * @return path to expected texts file
   * @since 3.0.0
   */
  public static String getTextComparisonFile() {
    return asString(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_FILE, DEFAULT_EXPECTED_TEXTS_FILE);
  }

  /**
   * Directory to retrieve the text samples from.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.text.directory.input
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
   *
   * @return  {@link io.wcm.qa.glnm.selenium.RunMode} used
   * @since 3.0.0
   */
  public static String getTextComparisonInputDirectory() {
    return asString(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_INPUT_DIRECTORY, DEFAULT_SPEC_PATH);
  }

  /**
   * Directory to retrieve the text samples from.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.sampling.text.directory.input
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
   *
   * @return  {@link io.wcm.qa.glnm.selenium.RunMode} used
   * @since 3.0.0
   */
  public static String getTextComparisonOutputDirectory() {
    return asString(SYSTEM_PROPERTY_NAME_SAMPLING_TEXT_OUTPUT_DIRECTORY, DEFAULT_REPORT_DIR);
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
   *
   * @return whether headless Chrome should be position off screen on Windows machines
   * @since 3.0.0
   */
  public static boolean isChromeHeadlessWindowsWorkaround() {
    return asBoolean(SYSTEM_PROPERTY_NAME_CHROME_HEADLESS_WINDOWS_WORKAROUND);
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
   *
   * @return whether to use workaround to fix Chrome's image comparison behavior
   * @since 3.0.0
   */
  public static boolean isFixChromeImageComparison() {
    if (asString(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX) == null) {
      // default is to fix chrome behavior
      return true;
    }
    return asBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_IMAGE_CHROMEFIX);
  }

  /**
   * Headless browser flag.
   * <ul>
   * <li>Key:
   *
   * <pre>
   * galenium.headless
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
   *
   * @return whether browser is running in headless mode
   * @since 3.0.0
   */
  public static boolean isHeadless() {
    return asBoolean(SYSTEM_PROPERTY_NAME_HEADLESS);
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
   *
   * @return whether to use lazy web driver initialization
   * @since 3.0.0
   */
  public static boolean isLazyWebDriverInitialization() {
    return asBoolean(SYSTEM_PROPERTY_NAME_LAZY_DRIVER);
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
   *
   * @return whether to skip reporting of successful tests
   * @since 3.0.0
   */
  public static boolean isOnlyReportGalenErrors() {
    return asBoolean(SYSTEM_PROPERTY_NAME_REPORT_ERRORS_ONLY);
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
   * false
   * </pre>
   *
   * </li>
   * </ul>
   *
   * @return whether to ignore errors when validating samples
   * @since 3.0.0
   */
  public static boolean isSamplingVerificationIgnore() {
    return asBoolean(SYSTEM_PROPERTY_NAME_SAMPLING_VERIFICATION_IGNORE_ERRORS);
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
   *
   * @return whether TestNG is used
   * @since 3.0.0
   */
  public static boolean isSkipExtentReports() {
    return asBoolean(SYSTEM_PROPERTY_NAME_REPORT_SKIP_EXTENT);
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
   *
   * @return whether to use sparse reporting on this run
   * @since 3.0.0
   */
  public static boolean isSparseReporting() {
    return asBoolean(SYSTEM_PROPERTY_NAME_SPARSE_REPORTING);
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
   *
   * @return whether to skip automatic adjusting of browser size
   * @since 3.0.0
   */
  public static boolean isSuppressAutoAdjustBrowserSize() {
    return asBoolean(SYSTEM_PROPERTY_NAME_GALEN_SUPPRESS_AUTO_ADJUST_BROWSERSIZE);
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
   *
   * @return whether to take and store screenshots of skipped tests for reporting
   * @since 3.0.0
   */
  public static boolean isTakeScreenshotOnSkippedTest() {
    return asBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SKIPPED);
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
   *
   * @return whether to take and store screenshots of successful tests for reporting
   * @since 3.0.0
   */
  public static boolean isTakeScreenshotOnSuccessfulTest() {
    return asBoolean(SYSTEM_PROPERTY_NAME_SCREENSHOT_ON_SUCCESS);
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
   *
   * @return whether browser accepts secure SSL certificates only
   * @since 3.0.0
   */
  public static boolean isWebDriverAcceptTrustedSslCertificatesOnly() {
    return asBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_TRUSTED_ONLY);
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
   *
   * @return whether to always instantiate a new webdriver for each test
   * @since 3.0.0
   */
  public static boolean isWebDriverAlwaysNew() {
    return asBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_ALWAYS_NEW);
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
   *
   * @return whether browser refuses SSL certificates
   * @since 3.0.0
   */
  public static boolean isWebDriverRefuseSslCertificates() {
    return asBoolean(SYSTEM_PROPERTY_NAME_WEB_DRIVER_SSL_REFUSE);
  }

  private static boolean asBoolean(String systemPropertyName) {
    return Boolean.getBoolean(systemPropertyName);
  }

  private static Integer asInteger(String systemPropertyName, int defaultValue) {
    return Integer.getInteger(systemPropertyName, defaultValue);
  }

  private static String asString(String systemPropertyName) {
    return System.getProperty(systemPropertyName);
  }

  private static String asString(String systemPropertyName, String defaultValue) {
    return System.getProperty(systemPropertyName, defaultValue);
  }
}
