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
          getLogger().info("attempting to close driver again after InterruptedException.");
          getLogger().debug("attempting to close driver after InterruptedException.", ex);
          quitDriver();
        }
        else {
          getLogger().error("Exception when closing driver.", ex);
          throw new SkipException("Skipping test because of driver problems. ", ex);
        }
      }
      finally {
        setDriver(null);
        setTestDevice(null);
        getLogger().info("Driver and Device set to null");
      }
    }
    else {
      RuntimeException ex = new RuntimeException("Attempting to close non existent driver.");
      getLogger().debug("Unnecessary call to close driver.", ex);
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
    boolean needsNewDevice = isDifferentFromCurrentDevice(testDevice);
    if (needsNewDevice) {
      getLogger().info("Needs new device: " + testDevice.toString());
      if (getDriver() != null) {
        closeDriver();
      }
      WebDriver newDriver = WebDriverFactory.newDriver(testDevice);
      setDriver(newDriver);
    }

    // only resize when different or new
    if (needsNewDevice || needsWindowResize(testDevice)) {
      try {
        Dimension screenSize = testDevice.getScreenSize();
        GalenUtils.autoAdjustBrowserWindowSizeToFitViewport(getDriver(), screenSize.width, screenSize.height);
      }
      catch (WebDriverException ex) {
        String msg = "Exception when resizing browser";
        getLogger().debug(msg, ex);
      }
      getDriver().manage().deleteAllCookies();
      getLogger().info("Deleted all cookies.");

      setTestDevice(testDevice);
    }
    return getDriver();
  }

  private static WebDriver getDriver() {
    WebDriver driver = GaleniumContext.getDriver();
    getLogger().trace("getting WebDriver: " + driver);
    return driver;
  }

  private static boolean isDifferentFromCurrentDevice(TestDevice testDevice) {
    boolean needsNewDevice = getDriver() == null
        || GaleniumConfiguration.isChromeHeadless()
        || getTestDevice() == null
        || testDevice.getBrowserType() != getTestDevice().getBrowserType()
        || (testDevice.getChromeEmulator() != null && !testDevice.getChromeEmulator().equals(getTestDevice().getChromeEmulator()));
    return needsNewDevice;
  }

  private static boolean needsWindowResize(TestDevice testDevice) {
    boolean needsWindowResize = !GaleniumConfiguration.isSuppressAutoAdjustBrowserSize()
        && StringUtils.isEmpty(testDevice.getChromeEmulator()) // don't size chrome-emulator
        && (!testDevice.getScreenSize().equals(getTestDevice().getScreenSize()));
    return needsWindowResize;
  }

  private static void quitDriver() {
    getLogger().info("Attempting to close driver");
    getDriver().quit();
    getLogger().info("Closed driver");
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
    getLogger().trace("set driver: " + driver);
  }

  /**
   * @param testDevice test device to use with this web driver
   */
  private static void setTestDevice(TestDevice testDevice) {
    GaleniumContext.getContext().setTestDevice(testDevice);
  }

  static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MarkerFactory.getMarker("webdriver"));
  }

}
