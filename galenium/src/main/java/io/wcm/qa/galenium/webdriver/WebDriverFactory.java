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
package io.wcm.qa.galenium.webdriver;

import static io.wcm.qa.galenium.util.GaleniumConfiguration.getGridHost;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getGridPort;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.isChromeHeadless;
import static java.text.MessageFormat.format;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;

import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.util.RunMode;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * Static factory methods for use by {@link WebDriverManager}.
 */
final class WebDriverFactory {

  private WebDriverFactory() {
    // do not instantiate
  }

  private static CapabilityProvider getDesiredCapabilitiesProvider(TestDevice device) {
    switch (device.getBrowserType()) {
      case CHROME:
        CapabilityProvider chromeCapabilityProvider;
        String chromeEmulator = device.getChromeEmulator();
        boolean withEmulator = StringUtils.isNotBlank(chromeEmulator);
        if (isChromeHeadless()) {
          if (withEmulator) {
            chromeCapabilityProvider = new CombinedCapabilityProvider(
                new HeadlessChromeCapabilityProvider(device),
                new ChromeEmulatorCapabilityProvider(chromeEmulator));
          }
          else {
            chromeCapabilityProvider = new HeadlessChromeCapabilityProvider(device);
          }
        }
        else if (withEmulator) {
          chromeCapabilityProvider = new ChromeEmulatorCapabilityProvider(chromeEmulator);
        }
        else {
          chromeCapabilityProvider = new ChromeCapabilityProvider();
        }
        return chromeCapabilityProvider;
      case FIREFOX:
        return new FirefoxCapabilityProvider();
      case IE:
        return new InternetExplorerCapabilityProvider();
      case SAFARI:
        return new SafariCapabilityProvider();
      default:
        break;
    }
    return null;
  }

  private static WebDriver getDriver() {
    return GaleniumContext.getDriver();
  }

  private static Logger getLogger() {
    return WebDriverManager.getLogger();
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

  /**
   * Create webdriver based on test device
   * @param newTestDevice info on browser and size
   * @return ready to use driver
   */
  static WebDriver newDriver(TestDevice newTestDevice) {

    RunMode runMode = GaleniumConfiguration.getRunMode();
    getLogger()
        .info(format("Creating new {0} {1} WebDriver for thread {2}",
            runMode,
            newTestDevice.getBrowserType(),
            Thread.currentThread().getName()));

    DesiredCapabilities capabilities = getDesiredCapabilitiesProvider(newTestDevice).getDesiredCapabilities();

    getLogger().info("Getting driver for runmode '" + runMode + "'");
    switch (runMode) {
      case REMOTE:
        getLogger().info("Connecting to grid at " + getGridHost() + ":" + getGridPort() + "...");
        try {
          setDriver(new RemoteWebDriver(new URL("http", getGridHost(), getGridPort(), "/wd/hub"), capabilities));
        }
        catch (MalformedURLException ex) {
          throw new RuntimeException(
              format("Couldn''t construct valid URL using selenium.host={0} and selenium.port={1}",
                  getGridHost(),
                  getGridPort()));
        }
        break;

      default:
      case LOCAL:
        switch (newTestDevice.getBrowserType()) {
          case CHROME:
            ChromeDriver chromeDriver = new ChromeDriver(capabilities);
            setDriver(chromeDriver);
            break;

          case IE:
            InternetExplorerDriver ieDriver = new InternetExplorerDriver(capabilities);
            setDriver(ieDriver);
            break;

          case SAFARI:
            SafariDriver safariDriver = new SafariDriver(capabilities);
            setDriver(safariDriver);
            break;

          default:
          case FIREFOX:
            FirefoxDriver firefoxDriver = new FirefoxDriver(capabilities);
            setDriver(firefoxDriver);
            break;
        }
        break;
    }

    return getDriver();
  }
}
