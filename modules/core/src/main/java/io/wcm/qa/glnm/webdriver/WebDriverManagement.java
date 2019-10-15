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
import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;

import com.galenframework.utils.GalenUtils;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.BrowserType;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Utility class to manage thread safe WebDriver instances.
 *
 * @since 1.0.0
 */
public final class WebDriverManagement {

  private static final Logger LOG = LoggerFactory.getLogger(WebDriverManagement.class);

  private WebDriverManagement() {
    // do not instantiate
  }

  /**
   * Quits Selenium WebDriver instance managed by this class.
   *
   * @since 3.0.0
   */
  public static void closeDriver() {
    if (getCurrentDriver() != null) {
      try {
        quitDriver();
      }
      catch (WebDriverException ex) {
        if (ex.getCause() instanceof InterruptedException) {
          LOG.info("attempting to close driver again after InterruptedException.");
          LOG.debug("attempting to close driver after InterruptedException.", ex);
          quitDriver();
        }
        else {
          String msg = "Exception when closing driver.";
          LOG.error(msg, ex);
          throw new SkipException("Skipping test because of driver problems. ", ex);
        }
      }
      finally {
        setDriver(null);
        setTestDevice(null);
        LOG.info("Driver and Device set to null");
      }
    }
    else {
      LOG.debug("Unnecessary call to close driver.", new GaleniumException("Attempting to close non existent driver."));
    }
  }

  /**
   * <p>getCurrentDriver.</p>
   *
   * @return driver from current thread's context
   * @since 3.0.0
   */
  public static WebDriver getCurrentDriver() {
    return GaleniumContext.getDriver();
  }

  /**
   * <p>getDriver.</p>
   *
   * @param testDevice test device to use for this driver
   * @return WebDriver for current thread.
   * @since 3.0.0
   */
  public static WebDriver getDriver(TestDevice testDevice) {
    if (testDevice == null) {
      throw new GaleniumException("trying to create driver for null device");
    }

    boolean needsNewDriver = needsNewDriver(testDevice);
    if (needsNewDriver) {
      LOG.info("Needs new device: " + testDevice.toString());
      if (getCurrentDriver() != null) {
        closeDriver();
      }
      WebDriver newDriver = WebDriverFactory.newDriver(testDevice);
      setDriver(newDriver);
      getCurrentDriver().manage().deleteAllCookies();
      LOG.info("Deleted all cookies.");
    }

    // only resize when different or new
    if (needsNewDriver || needsWindowResize(testDevice)) {
      if (isSuppressAutoAdjustBrowserSize()) {
        LOG.debug("resizing suppressed.");
      }
      else {
        try {
          Dimension screenSize = testDevice.getScreenSize();
          GalenUtils.autoAdjustBrowserWindowSizeToFitViewport(getCurrentDriver(), screenSize.width, screenSize.height);
        }
        catch (WebDriverException ex) {
          if (!isHeadless()) {
            // headless chrome does not have a window target
            LOG.debug("Exception when resizing browser", ex);
          }
        }
      }
    }

    setTestDevice(testDevice);
    if (LOG.isTraceEnabled()) {
      LOG.trace("driver for test device: " + testDevice);
      LOG.trace("test device screen size: " + toString(getTestDevice().getScreenSize()));
      Dimension windowSize = getWindowSize();
      if (windowSize == null && GaleniumConfiguration.isHeadless()) {
        LOG.trace("driver window size: none (headless)");
      }
      else if (windowSize == null) {
        LOG.trace("driver window size: none");
      }
      else {
        LOG.trace("driver window size: " + toString(windowSize));
      }
    }
    return getCurrentDriver();
  }

  /**
   * <p>isBrowser.</p>
   *
   * @param testDevice to check
   * @return whether this test device is a browser that needs a webdriver
   * @since 3.0.0
   */
  public static boolean isBrowser(TestDevice testDevice) {
    return testDevice != null && testDevice.getBrowserType() != BrowserType.NO_BROWSER;
  }

  /**
   * Set implicit wait to configured default timeout.
   *
   * @since 3.0.0
   */
  public static void setDefaultTimeout() {
    getCurrentDriver().manage().timeouts().implicitlyWait(GaleniumConfiguration.getDefaultWebdriverTimeout(), TimeUnit.SECONDS);
  }

  /**
   * Set implicit wait to 0 seconds timeout.
   *
   * @since 3.0.0
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
        LOG.trace("exception when fetching window size", ex);
      }
    }
    return null;
  }

  private static boolean needsNewDriver(TestDevice testDevice) {

    if (getCurrentDriver() == null) {
      LOG.trace("needs new device: driver is null");
      return true;
    }
    if (GaleniumConfiguration.isWebDriverAlwaysNew()) {
      LOG.trace("needs new device: always");
      return true;
    }
    if (getTestDevice() == null) {
      LOG.trace("needs new device: no previous test device");
      return true;
    }
    if (testDevice.getBrowserType() != getTestDevice().getBrowserType()) {
      LOG.trace("needs new device: different browser type ("
      + testDevice.getBrowserType()
      + " != "
      + getTestDevice().getBrowserType()
      + ")");
      return true;
    }
    if (testDevice.getChromeEmulator() != null
        && !testDevice.getChromeEmulator().equals(getTestDevice().getChromeEmulator())) {
      LOG.trace("needs new device: different emulator ("
      + testDevice.getChromeEmulator()
      + " != "
      + getTestDevice().getChromeEmulator()
      + ")");
      return true;
    }
    LOG.trace("no need for new device: " + testDevice);
    return false;
  }

  private static boolean needsWindowResize(TestDevice testDevice) {
    if (GaleniumConfiguration.isSuppressAutoAdjustBrowserSize()) {
      LOG.trace("no need for resize: suppress galen auto adjust");
      return false;
    }
    if (StringUtils.isNotBlank(testDevice.getChromeEmulator())) {
      LOG.trace("no need for resize: chrome emulator set (" + testDevice.getChromeEmulator() + ")");
      return false;
    }
    if (getTestDevice() != null
        && testDevice.getScreenSize() != null
        && testDevice.getScreenSize().equals(getTestDevice().getScreenSize())) {
      LOG.trace("no need for resize: same screen size");
      return false;
    }
    return true;
  }

  private static void quitDriver() {
    LOG.info("Attempting to close driver");
    getCurrentDriver().quit();
    LOG.info("Closed driver");
    setDriver(null);
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
    LOG.trace("set driver: " + driver);
  }

  /**
   * @param testDevice test device to use with this web driver
   */
  private static void setTestDevice(TestDevice testDevice) {
    if (testDevice != getTestDevice()) {
      LOG.debug("setting new test device from WebDriverManager: " + testDevice);
      GaleniumContext.getContext().setTestDevice(testDevice);
    }
    else {
      LOG.trace("not setting same test device twice: " + testDevice);
    }
  }

  private static String toString(Dimension dimension) {
    if (dimension == null) {
      return "null";
    }
    return dimension.getWidth() + "x" + dimension.getHeight();
  }


}
