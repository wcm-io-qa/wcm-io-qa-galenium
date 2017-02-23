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
package io.wcm.qa.galenium.webdriver;

import static io.wcm.qa.galenium.util.GaleniumConfiguration.getGridHost;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getGridPort;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

import com.galenframework.utils.GalenUtils;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.util.RunMode;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * Utility class to manage thread safe WebDriver instances.
 */
public final class WebDriverManager {

  private static final Logger log = LoggerFactory.getLogger(WebDriverManager.class);

  private WebDriverManager() {
    // do not instantiate
  }

  /**
   * @param testDevice test device to use for this driver
   * @return WebDriver for current thread.
   */
  public static WebDriver getDriver(TestDevice testDevice) {
    boolean needsNewDevice = getDriver() == null
        || getTestDevice() == null
        || testDevice.getBrowserType() != getTestDevice().getBrowserType()
        || (testDevice.getChromeEmulator() != null && !testDevice.getChromeEmulator().equals(getTestDevice().getChromeEmulator()));
    
    if (needsNewDevice) {
      GaleniumReportUtil.getLogger().info("Needs new device: " + testDevice.toString());
      if (getDriver() != null) {
        closeDriver();
      }
      setTestDevice(testDevice);
      setDriver(newDriver(testDevice));
    }
    boolean needsWindowResize = StringUtils.isEmpty(testDevice.getChromeEmulator()) // don't size chrome-emulator
        && (!testDevice.getScreenSize().equals(getTestDevice().getScreenSize()) || needsNewDevice); // only resize when different or new
    if (needsWindowResize) {
      try {
        Dimension screenSize = testDevice.getScreenSize();
        GalenUtils.autoAdjustBrowserWindowSizeToFitViewport(getDriver(), screenSize.width, screenSize.height);
      }
      catch (WebDriverException ex) {
        String msg = "Exception when resizing browser";
        log.info(msg, ex);
        GaleniumReportUtil.getLogger().debug(msg, ex);
      }
      getDriver().manage().deleteAllCookies();
      GaleniumReportUtil.getLogger().info("Deleted all cookies.");
      setTestDevice(testDevice);
    }
    return getDriver();
  }

  public static WebDriver getCurrentDriver() {
    return GaleniumContext.getDriver();
  }

  /**
   * Quits Selenium WebDriver instance managed by this class.
   */
  public static void closeDriver() {
    if (getDriver() != null) {
      try {
        quitDriver();
      }
      catch (WebDriverException ex) {
        if (ex.getCause() instanceof InterruptedException) {
          logInfo("attempting to close driver again after InterruptedException.");
          GaleniumReportUtil.getLogger().debug("attempting to close driver after InterruptedException.", ex);
          quitDriver();
        }
        else {
          logError("Exception when closing driver.", ex);
          throw new SkipException("Skipping test because of driver problems. ", ex);
        }
      }
      finally {
        setDriver(null);
        setTestDevice(null);
        GaleniumReportUtil.getLogger().info("Driver and Device set to null");
      }
    }
    else {
      RuntimeException ex = new RuntimeException("Attempting to close non existent driver.");
      GaleniumReportUtil.getLogger().debug("Unnecessary call to close driver.", ex);
    }
  }

  protected static void quitDriver() {
    GaleniumReportUtil.getLogger().info("Attempting to close driver");
    getDriver().quit();
    GaleniumReportUtil.getLogger().info("Closed driver");
  }

  private static DesiredCapabilities getDesiredCapabilities(TestDevice newTestDevice) {
    DesiredCapabilities capabilities;

    GaleniumReportUtil.getLogger().info("Getting capabilities for " + newTestDevice.getBrowserType());
    switch (newTestDevice.getBrowserType()) {
      case CHROME:
        capabilities = DesiredCapabilities.chrome();
        String chromeEmulator = newTestDevice.getChromeEmulator();
        if (chromeEmulator != null) {
          Map<String, String> mobileEmulation = new HashMap<String, String>();
          mobileEmulation.put("deviceName", chromeEmulator);
          Map<String, Object> chromeOptions = new HashMap<String, Object>();
          chromeOptions.put("mobileEmulation", mobileEmulation);
          capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        break;

      case IE:
        capabilities = DesiredCapabilities.internetExplorer();
        break;

      case SAFARI:
        capabilities = DesiredCapabilities.safari();
        break;

      case PHANTOMJS:
        capabilities = DesiredCapabilities.phantomjs();
        capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
            "--ignore-ssl-errors=true", "--ssl-protocol=tlsv1", "--web-security=false", "--webdriver-loglevel=OFF", "--webdriver-loglevel=NONE"
        });
        break;

      default:
      case FIREFOX:
        capabilities = DesiredCapabilities.firefox();
        // Workaround for click events spuriously failing in Firefox (https://code.google.com/p/selenium/issues/detail?id=6112)
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        firefoxProfile.setEnableNativeEvents(false);
        firefoxProfile.setAcceptUntrustedCertificates(true);
        firefoxProfile.setAssumeUntrustedCertificateIssuer(false);
        capabilities.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
        break;
    }

    // Request browser logging capabilities for capturing console.log output
    LoggingPreferences loggingPrefs = new LoggingPreferences();
    loggingPrefs.enable(LogType.BROWSER, Level.INFO);
    capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs);
    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

    GaleniumReportUtil.getLogger().info("Done generating capabilities");
    return capabilities;
  }

  private static WebDriver newDriver(TestDevice newTestDevice) {

    RunMode runMode = GaleniumConfiguration.getRunMode();
    logInfo("Creating new " + runMode + " " + newTestDevice.getBrowserType() + " WebDriver for thread " + Thread.currentThread().getName());

    DesiredCapabilities capabilities = getDesiredCapabilities(newTestDevice);

    GaleniumReportUtil.getLogger().info("Getting driver for runmode '" + runMode + "'");
    switch (runMode) {
      case REMOTE:
        logInfo("Connecting to grid at " + getGridHost() + ":" + getGridPort() + "...");
        try {
          setDriver(new RemoteWebDriver(new URL("http", getGridHost(), getGridPort(), "/wd/hub"), capabilities));
        }
        catch (MalformedURLException ex) {
          throw new RuntimeException("Couldn't construct valid URL using selenium.host=" + getGridHost() + " and selenium.port=" + getGridPort());
        }
        break;

      default:
      case LOCAL:
        switch (newTestDevice.getBrowserType()) {
          case CHROME:
            setDriver(new ChromeDriver(capabilities));
            break;

          case IE:
            setDriver(new InternetExplorerDriver(capabilities));
            break;

          case SAFARI:
            setDriver(new SafariDriver(capabilities));
            break;

          case PHANTOMJS:
            setDriver(new PhantomJSDriver(capabilities));
            break;

          default:
          case FIREFOX:
            setDriver(new FirefoxDriver(capabilities));
            break;
        }
        break;
    }

    return getDriver();
  }

  public static TestDevice getTestDevice() {
    return GaleniumContext.getTestDevice();
  }

  /**
   * @param testDevice test device to use with this web driver
   */
  public static void setTestDevice(TestDevice testDevice) {
    GaleniumContext.getContext().setTestDevice(testDevice);
  }

  private static void logInfo(String msg) {
    log.info(msg);
    GaleniumReportUtil.getLogger().info(msg);
  }

  private static void logError(String msg, WebDriverException ex) {
    log.error(msg, ex);
    GaleniumReportUtil.getLogger().error(msg, ex);
  }

  private static WebDriver getDriver() {
    return GaleniumContext.getDriver();
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

}
