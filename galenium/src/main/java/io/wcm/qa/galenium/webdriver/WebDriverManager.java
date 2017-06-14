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

import static io.wcm.qa.galenium.util.GaleniumContext.getTestDevice;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.MarkerFactory;
import org.testng.SkipException;

import com.galenframework.utils.GalenUtils;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * Utility class to manage thread safe WebDriver instances.
 */
public final class WebDriverManager {

  private WebDriverManager() {
    // do not instantiate
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
      RuntimeException ex = new RuntimeException("Attempting to close non existent driver.");
      logDebug("Unnecessary call to close driver.", ex);
    }
  }

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

    if (isDifferentFromCurrentDevice(testDevice)) {
      logInfo("Needs new device: " + testDevice.toString());
      if (getDriver() != null) {
        closeDriver();
      }
      WebDriver newDriver = WebDriverFactory.newDriver(testDevice);
      setDriver(newDriver);
    }

    // only resize when different or new
    if (needsWindowResize(testDevice)) {
      try {
        Dimension screenSize = testDevice.getScreenSize();
        GalenUtils.autoAdjustBrowserWindowSizeToFitViewport(getDriver(), screenSize.width, screenSize.height);
      }
      catch (WebDriverException ex) {
        String msg = "Exception when resizing browser";
        logDebug(msg, ex);
      }
      getDriver().manage().deleteAllCookies();
      logInfo("Deleted all cookies.");
    }

    setTestDevice(testDevice);
    return getDriver();
  }

  private static WebDriver getDriver() {
    WebDriver driver = GaleniumContext.getDriver();
    logTrace("getting WebDriver: " + driver);
    return driver;
  }

  private static boolean isDifferentFromCurrentDevice(TestDevice testDevice) {

    if (getDriver() == null) {
      logTrace("needs new device: driver is null");
      return true;
    }
    if (GaleniumConfiguration.isChromeHeadless()) {
      logTrace("needs new device: headless chrome always needs new device");
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

  private static boolean needsWindowResize(TestDevice testDevice) {
    if (GaleniumConfiguration.isSuppressAutoAdjustBrowserSize()) {
      logTrace("no need for resize: suppress galen auto adjust");
      return false;
    }
    if (GaleniumConfiguration.isChromeHeadless()) {
      logTrace("no need for resize: headless chrome always started as new instance in correct size");
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
    logTrace("needs resize: " + testDevice);
    return isDifferentFromCurrentDevice(testDevice);
  }

  private static void quitDriver() {
    logInfo("Attempting to close driver");
    getDriver().quit();
    logInfo("Closed driver");
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

  static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MarkerFactory.getMarker("webdriver"));
  }

}
