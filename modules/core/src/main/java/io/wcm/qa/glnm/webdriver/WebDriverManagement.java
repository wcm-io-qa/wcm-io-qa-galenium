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
package io.wcm.qa.glnm.webdriver;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.isHeadless;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.isSuppressAutoAdjustBrowserSize;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_ERROR;
import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.testng.SkipException;

import com.galenframework.utils.GalenUtils;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.BrowserType;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Utility class to manage thread safe WebDriver instances.
 */
public final class WebDriverManagement {

  /** Marker for use in logging related directly to webdriver handling and internals. */
  public static final Marker MARKER_WEBDRIVER = MarkerFactory.getMarker("galenium.webdriver");

  private WebDriverManagement() {
    // do not instantiate
  }

  /**
   * Quits Selenium WebDriver instance managed by this class.
   */
  public static void closeDriver() {
    if (getCurrentDriver() != null) {
      try {
        quitDriver();
      }
      catch (WebDriverException ex) {
        if (ex.getCause() instanceof InterruptedException) {
          logInfo("attempting to close driver again after InterruptedException.");
          logDebug("attempting to close driver after InterruptedException.", ex);
          quitDriver();
        }
        else {
          String msg = "Exception when closing driver.";
          logError(msg, ex);
          throw new SkipException("Skipping test because of driver problems. ", ex);
        }
      }
      finally {
        setDriver(null);
        setTestDevice(null);
        logInfo("Driver and Device set to null");
      }
    }
    else {
      logDebug("Unnecessary call to close driver.", new GaleniumException("Attempting to close non existent driver."));
    }
  }

  /**
   * @return driver from current thread's context
   */
  public static WebDriver getCurrentDriver() {
    return GaleniumContext.getDriver();
  }

  /**
   * @param testDevice test device to use for this driver
   * @return WebDriver for current thread.
   */
  public static WebDriver getDriver(TestDevice testDevice) {
    if (testDevice == null) {
      throw new GaleniumException("trying to create driver for null device");
    }

    boolean needsNewDriver = needsNewDriver(testDevice);
    if (needsNewDriver) {
      logInfo("Needs new device: " + testDevice.toString());
      if (getCurrentDriver() != null) {
        closeDriver();
      }
      WebDriver newDriver = WebDriverFactory.newDriver(testDevice);
      setDriver(newDriver);
      getCurrentDriver().manage().deleteAllCookies();
      logInfo("Deleted all cookies.");
    }

    // only resize when different or new
    if (needsNewDriver || needsWindowResize(testDevice)) {
      if (isSuppressAutoAdjustBrowserSize()) {
        logDebug("resizing suppressed.");
      }
      else {
        try {
          Dimension screenSize = testDevice.getScreenSize();
          GalenUtils.autoAdjustBrowserWindowSizeToFitViewport(getCurrentDriver(), screenSize.width, screenSize.height);
        }
        catch (WebDriverException ex) {
          if (!isHeadless()) {
            // headless chrome does not have a window target
            logDebug("Exception when resizing browser", ex);
          }
        }
      }
    }

    setTestDevice(testDevice);
    if (getLogger().isTraceEnabled()) {
      getLogger().trace("driver for test device: " + testDevice);
      getLogger().trace("test device screen size: " + toString(getTestDevice().getScreenSize()));
      Dimension windowSize = getWindowSize();
      if (windowSize == null && GaleniumConfiguration.isHeadless()) {
        getLogger().trace("driver window size: none (headless)");
      }
      else if (windowSize == null) {
        getLogger().trace("driver window size: none");
      }
      else {
        getLogger().trace("driver window size: " + toString(windowSize));
      }
    }
    return getCurrentDriver();
  }

  /**
   * @param testDevice to check
   * @return whether this test device is a browser that needs a webdriver
   */
  public static boolean isBrowser(TestDevice testDevice) {
    return testDevice != null && testDevice.getBrowserType() != BrowserType.NO_BROWSER;
  }

  /**
   * Set implicit wait to configured default timeout.
   */
  public static void setDefaultTimeout() {
    getCurrentDriver().manage().timeouts().implicitlyWait(GaleniumConfiguration.getDefaultWebdriverTimeout(), TimeUnit.SECONDS);
  }

  /**
   * Set implicit wait to 0 seconds timeout.
   */
  public static void setZeroTimeout() {
    getCurrentDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
  }

  private static Dimension getWindowSize() {
    try {
      return getCurrentDriver().manage().window().getSize();
    }
    catch (NullPointerException | WebDriverException ex) {
      if (!GaleniumConfiguration.isHeadless()) {
        getLogger().trace(MARKER_ERROR, "exception when fetching window size", ex);
      }
    }
    return null;
  }

  private static void logDebug(String msg) {
    getLogger().debug(msg);
  }

  private static void logDebug(String msg, Throwable ex) {
    getLogger().debug(msg, ex);
  }

  private static void logError(String msg, Throwable ex) {
    getLogger().error(msg, ex);
  }

  private static void logInfo(String msg) {
    getLogger().info(msg);
  }

  private static void logTrace(String msg) {
    getLogger().trace(msg);
  }

  private static boolean needsNewDriver(TestDevice testDevice) {

    if (getCurrentDriver() == null) {
      logTrace("needs new device: driver is null");
      return true;
    }
    if (GaleniumConfiguration.isWebDriverAlwaysNew()) {
      logTrace("needs new device: always");
      return true;
    }
    if (getTestDevice() == null) {
      logTrace("needs new device: no previous test device");
      return true;
    }
    if (testDevice.getBrowserType() != getTestDevice().getBrowserType()) {
      logTrace("needs new device: different browser type ("
          + testDevice.getBrowserType()
          + " != "
          + getTestDevice().getBrowserType()
          + ")");
      return true;
    }
    if (testDevice.getChromeEmulator() != null
        && !testDevice.getChromeEmulator().equals(getTestDevice().getChromeEmulator())) {
      logTrace("needs new device: different emulator ("
          + testDevice.getChromeEmulator()
          + " != "
          + getTestDevice().getChromeEmulator()
          + ")");
      return true;
    }
    logTrace("no need for new device: " + testDevice);
    return false;
  }

  private static boolean needsWindowResize(TestDevice testDevice) {
    if (GaleniumConfiguration.isSuppressAutoAdjustBrowserSize()) {
      logTrace("no need for resize: suppress galen auto adjust");
      return false;
    }
    if (StringUtils.isNotBlank(testDevice.getChromeEmulator())) {
      logTrace("no need for resize: chrome emulator set (" + testDevice.getChromeEmulator() + ")");
      return false;
    }
    if (getTestDevice() != null
        && testDevice.getScreenSize() != null
        && testDevice.getScreenSize().equals(getTestDevice().getScreenSize())) {
      logTrace("no need for resize: same screen size");
      return false;
    }
    return true;
  }

  private static void quitDriver() {
    logInfo("Attempting to close driver");
    getCurrentDriver().quit();
    logInfo("Closed driver");
    setDriver(null);
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
    logTrace("set driver: " + driver);
  }

  /**
   * @param testDevice test device to use with this web driver
   */
  private static void setTestDevice(TestDevice testDevice) {
    if (testDevice != getTestDevice()) {
      logDebug("setting new test device from WebDriverManager: " + testDevice);
      GaleniumContext.getContext().setTestDevice(testDevice);
    }
    else {
      logTrace("not setting same test device twice: " + testDevice);
    }
  }

  private static String toString(Dimension dimension) {
    if (dimension == null) {
      return "null";
    }
    return dimension.getWidth() + "x" + dimension.getHeight();
  }

  static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER_WEBDRIVER);
  }

}
