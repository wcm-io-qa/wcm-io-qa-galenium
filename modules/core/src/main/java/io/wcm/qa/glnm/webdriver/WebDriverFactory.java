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
package io.wcm.qa.glnm.webdriver;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getGridHost;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getGridPort;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.isHeadless;
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
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.opentest4j.TestSkippedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.selenium.RunMode;

/**
 * Static factory methods for use by {@link WebDriverManager}.
 */
final class WebDriverFactory {

  private static final Logger LOG = LoggerFactory.getLogger(WebDriverFactory.class);

  private WebDriverFactory() {
    // do not instantiate
  }

  private static ChromeDriver getChromeDriver(OptionsProvider capabilitiesProvider) {
    return new ChromeDriver((ChromeOptions)capabilitiesProvider.getOptions());
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
          LOG.trace("chrome headless: " + ReflectionToStringBuilder.toString(headlessProvider, ToStringStyle.MULTI_LINE_STYLE));
          if (withEmulator) {
            LOG.trace("with emulator: " + ReflectionToStringBuilder.toString(emulatorProvider, ToStringStyle.MULTI_LINE_STYLE));
            chromeOptionProvider = new CombinedOptionsProvider(headlessProvider, emulatorProvider);
          }
          else {
            chromeOptionProvider = headlessProvider;
          }
        }
        else if (withEmulator) {
          LOG.trace("with emulator: " + ReflectionToStringBuilder.toString(emulatorProvider, ToStringStyle.MULTI_LINE_STYLE));
          chromeOptionProvider = emulatorProvider;
        }
        else {
          chromeOptionProvider = new ChromeOptionsProvider();
        }
        LOG.debug("chrome provider: " + ReflectionToStringBuilder.toString(chromeOptionProvider, ToStringStyle.MULTI_LINE_STYLE));
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

  private static FirefoxDriver getFirefoxDriver(OptionsProvider capabilitiesProvider) {
    return new FirefoxDriver((FirefoxOptions)capabilitiesProvider.getOptions());
  }

  private static InternetExplorerDriver getInternetExplorerDriver(OptionsProvider capabilitiesProvider) {
    return new InternetExplorerDriver((InternetExplorerOptions)capabilitiesProvider.getOptions());
  }

  private static WebDriver getNewDriver(TestDevice newTestDevice, RunMode runMode) {
    LOG.info("Getting driver for runmode '" + runMode + "'");
    OptionsProvider capabilitiesProvider = getDesiredCapabilitiesProvider(newTestDevice);
    switch (runMode) {
      case REMOTE:
        return getRemoteDriver(capabilitiesProvider);

      default:
      case LOCAL:
        switch (newTestDevice.getBrowserType()) {
          case FIREFOX:
            WebDriverManager.firefoxdriver().setup();
            return getFirefoxDriver(capabilitiesProvider);
          case IE:
            WebDriverManager.iedriver().setup();
            return getInternetExplorerDriver(capabilitiesProvider);
          default:
          case CHROME:
            WebDriverManager.chromedriver().setup();
            return getChromeDriver(capabilitiesProvider);
        }
    }
  }

  private static WebDriver getRemoteDriver(OptionsProvider capabilitiesProvider) {
    String gridHost = getGridHost();
    int gridPort = getGridPort();
    LOG.info("Connecting to grid at " + gridHost + ":" + gridPort + "...");
    try {
      String protocol = "http";
      if (StringUtils.startsWith(gridHost, "https://")) {
        gridHost = StringUtils.removeStart(gridHost, "https://");
        protocol = "https";
      }
      return new RemoteWebDriver(new URL(protocol, gridHost, gridPort, "/wd/hub"), capabilitiesProvider.getOptions());
    }
    catch (MalformedURLException ex) {
      throw new RuntimeException(
          format("Couldn''t construct valid URL using selenium.host={0} and selenium.port={1}",
              gridHost,
              gridPort));
    }
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
    LOG.info(
        format("Creating new {0} {1} WebDriver for thread {2}",
            runMode,
            newTestDevice.getBrowserType(),
            Thread.currentThread().getName()));

    try {
      setDriver(getNewDriver(newTestDevice, runMode));
    }
    catch (WebDriverException ex) {
      throw new TestSkippedException("Could not connect to browser.", ex);
    }

    int timeout = GaleniumConfiguration.getDefaultWebdriverTimeout();
    getDriver().manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    return getDriver();
  }

}
