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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.bonigarcia.seljup.Arguments;
import io.github.bonigarcia.seljup.BrowserType;
import io.wcm.qa.glnm.context.GaleniumContext;

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
    getSeleniumExtension().afterAll(context);
  }

  /** {@inheritDoc} */
  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    getSeleniumExtension().afterEach(context);
  }

  /** {@inheritDoc} */
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    String contextId = context.getUniqueId();
    updateBrowserList(contextId);
    Object webDriver = getDriverFromSelJup(context);
    if (isDriver(contextId, webDriver)) {
      setDriver(webDriver);
    }
  }

  private Object getDriverFromSelJup(ExtensionContext context) {
    return SeleniumJupiterUtil.getDriverFromSelJup(driverParamContext(this, "setHeadlessDriver"), context);
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

  private void setDriver(Object webDriver) {
    setDriver((WebDriver)webDriver);
  }

  private void updateBrowserList(String contextId) {
    getSeleniumExtension().putBrowserList(contextId, asBrowserList(browserType));
  }

  void setHeadlessDriver(@Arguments("--headless") WebDriver driver) {
    setDriver(driver);
  }

  void setVisibleDriver(WebDriver driver) {
    setDriver(driver);
  }

  private static void setDriver(WebDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

}
