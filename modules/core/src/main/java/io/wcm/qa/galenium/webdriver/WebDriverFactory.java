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

import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.getGridHost;
import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.getGridPort;
import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.isHeadless;
import static java.text.MessageFormat.format;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.testng.SkipException;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.selenium.RunMode;
import io.wcm.qa.galenium.util.GaleniumContext;

/**
 * Static factory methods for use by {@link WebDriverManager}.
 */
final class WebDriverFactory {

  private WebDriverFactory() {
    // do not instantiate
  }

  private static OptionsProvider getDesiredCapabilitiesProvider(TestDevice device) {
    switch (device.getBrowserType()) {
      case CHROME:
        OptionsProvider chromeOptionProvider;
        String chromeEmulator = device.getChromeEmulator();
        boolean withEmulator = StringUtils.isNotBlank(chromeEmulator);
        ChromeEmulatorOptionsProvider emulatorProvider = new ChromeEmulatorOptionsProvider(chromeEmulator);
        if (isHeadless()) {
          HeadlessChromeCapabilityProvider headlessProvider = new HeadlessChromeCapabilityProvider(device);
          getLogger().trace("chrome headless: " + ReflectionToStringBuilder.toString(headlessProvider, ToStringStyle.MULTI_LINE_STYLE));
          if (withEmulator) {
            getLogger().trace("with emulator: " + ReflectionToStringBuilder.toString(emulatorProvider, ToStringStyle.MULTI_LINE_STYLE));
            chromeOptionProvider = new CombinedOptionsProvider(headlessProvider, emulatorProvider);
          }
          else {
            chromeOptionProvider = headlessProvider;
          }
        }
        else if (withEmulator) {
          getLogger().trace("with emulator: " + ReflectionToStringBuilder.toString(emulatorProvider, ToStringStyle.MULTI_LINE_STYLE));
          chromeOptionProvider = emulatorProvider;
        }
        else {
          chromeOptionProvider = new ChromeOptionsProvider();
        }
        getLogger().debug("chrome provider: " + ReflectionToStringBuilder.toString(chromeOptionProvider, ToStringStyle.MULTI_LINE_STYLE));
        return chromeOptionProvider;
      case FIREFOX:
        return new FirefoxOptionsProvider();
      case IE:
        return new InternetExplorerOptionsProvider();
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

  private static void setChromeDriver(OptionsProvider capabilitiesProvider) {
    ChromeDriver chromeDriver = new ChromeDriver((ChromeOptions)capabilitiesProvider.getOptions());
    setDriver(chromeDriver);
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

  private static void setFirefoxDriver(OptionsProvider capabilitiesProvider) {
    FirefoxDriver firefoxDriver = new FirefoxDriver((FirefoxOptions)capabilitiesProvider.getOptions());
    setDriver(firefoxDriver);
  }

  @SuppressWarnings("deprecation")
  private static void setInternetExplorerDriver(OptionsProvider capabilitiesProvider) {
    InternetExplorerDriver ieDriver = new InternetExplorerDriver(capabilitiesProvider.getOptions());
    setDriver(ieDriver);
  }

  private static void setNewDriver(TestDevice newTestDevice, RunMode runMode) {
    OptionsProvider capabilitiesProvider = getDesiredCapabilitiesProvider(newTestDevice);
    getLogger().info("Getting driver for runmode '" + runMode + "'");
    switch (runMode) {
      case REMOTE:
        setRemoteDriver(capabilitiesProvider);
        break;

      default:
      case LOCAL:
        switch (newTestDevice.getBrowserType()) {
          case CHROME:
            setChromeDriver(capabilitiesProvider);
            break;

          case IE:
            setInternetExplorerDriver(capabilitiesProvider);
            break;

          default:
          case FIREFOX:
            setFirefoxDriver(capabilitiesProvider);
            break;
        }
        break;
    }
  }

  private static void setRemoteDriver(OptionsProvider capabilitiesProvider) {
    String gridHost = getGridHost();
    getLogger().info("Connecting to grid at " + gridHost + ":" + getGridPort() + "...");
    try {
      String protocol = "http";
      if (StringUtils.startsWith(gridHost, "https://")) {
        gridHost = StringUtils.removeStart(gridHost, "https://");
        protocol = "https";
      }
      setDriver(new RemoteWebDriver(new URL(protocol, gridHost, getGridPort(), "/wd/hub"), capabilitiesProvider.getOptions()));
    }
    catch (MalformedURLException ex) {
      throw new RuntimeException(
          format("Couldn''t construct valid URL using selenium.host={0} and selenium.port={1}",
              gridHost,
              getGridPort()));
    }
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

    try {
      setNewDriver(newTestDevice, runMode);
    }
    catch (WebDriverException ex) {
      throw new SkipException("Could not connect to browser.", ex);
    }

    int timeout = GaleniumConfiguration.getDefaultWebdriverTimeout();
    getDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    return getDriver();
  }

}
