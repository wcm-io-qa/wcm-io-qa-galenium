/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.junit.seljup;

import static io.wcm.qa.glnm.junit.seljup.SeleniumJupiterUtil.asBrowserList;
import static io.wcm.qa.glnm.junit.seljup.SeleniumJupiterUtil.getSeleniumExtension;
import static org.junit.jupiter.engine.execution.GaleniumDriverParameterContext.driverParamContext;

import java.util.Optional;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.seljup.Arguments;
import io.github.bonigarcia.seljup.BrowserType;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.webdriver.WebDriverManagement;

class BrowserInjectionExtension implements
    BeforeEachCallback,
    AfterEachCallback,
    AfterAllCallback {

  private static final Logger LOG = LoggerFactory.getLogger(BrowserInjectionExtension.class);
  private final BrowserType browserType;

  BrowserInjectionExtension(BrowserType browser) {
    browserType = browser;
  }

  /** {@inheritDoc} */
  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    if (LOG.isTraceEnabled()) {
      LOG.trace("after all: " + context.getUniqueId());
    }
    getSeleniumExtension().afterAll(context);
  }

  /** {@inheritDoc} */
  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (LOG.isTraceEnabled()) {
      LOG.trace("after each: " + context.getUniqueId());
    }
    screenshot(context);
    getSeleniumExtension().afterEach(context);
  }

  /** {@inheritDoc} */
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    if (LOG.isTraceEnabled()) {
      LOG.trace("before each: " + context.getUniqueId());
    }
    String contextId = context.getUniqueId();
    updateBrowserList(contextId);
    Object webDriver = getDriverFromSelJup(context);
    if (isDriver(contextId, webDriver)) {
      setDriver(webDriver);
    }
  }

  private Object getDriverFromSelJup(ExtensionContext context) {
    if (GaleniumConfiguration.isHeadless()) {
      return SeleniumJupiterUtil.getDriverFromSelJup(driverParamContext(this, "setHeadlessDriver"), context);
    }
    return SeleniumJupiterUtil.getDriverFromSelJup(driverParamContext(this, "setVisibleDriver"), context);
  }

  private boolean isDriver(String uniqueId, Object webDriver) {
    if (webDriver == null) {
      if (LOG.isInfoEnabled()) {
        LOG.info("No webdriver resolved. ({0})", uniqueId);
      }
      // no webdriver
      return false;
    }

    if (!(webDriver instanceof WebDriver)) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Not resolved to webdriver: {1} ({0})", uniqueId, webDriver);
      }
      // not a webdriver
      return false;
    }

    return true;
  }

  private void screenshot(ExtensionContext context) {
    Optional<Throwable> executionException = context.getExecutionException();
    if (executionException.isPresent()) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("screenshot after " + executionException.get());
      }
      screenshot();
      return;
    }
    if (GaleniumConfiguration.isTakeScreenshotOnSuccessfulTest()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("screenshot after success");
      }
      screenshot();
      return;
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace("no screenshot");
    }
  }

  private void setDriver(Object webDriver) {
    setDriver((WebDriver)webDriver);
  }

  private void updateBrowserList(String contextId) {
    getSeleniumExtension().putBrowserList(contextId, asBrowserList(browserType));
  }

  void setHeadlessDriver(
      @Arguments({
          "--headless",
          "--enable-logging"
      }) WebDriver driver) {
    setDriver(driver);
  }

  void setVisibleDriver(
      @Arguments({
          "--enable-logging"
      }) WebDriver driver) {
    setDriver(driver);
  }

  private static void screenshot() {
    WebDriver currentDriver = WebDriverManagement.getCurrentDriver();
    if (currentDriver == null) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("no screenshot as driver is null");
      }
      return;
    }
    if (currentDriver instanceof RemoteWebDriver && ((RemoteWebDriver)currentDriver).getSessionId() == null) {
      if (LOG.isInfoEnabled()) {
        LOG.info("no screenshot as session ID is null");
      }
      return;
    }
    if (!(currentDriver instanceof TakesScreenshot)) {
      if (LOG.isInfoEnabled()) {
        LOG.info("no screenshot as driver not able to take screenshot");
      }
      return;
    }
    GaleniumReportUtil.takeScreenshot((TakesScreenshot)currentDriver);
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

}
