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
package io.wcm.qa.galenium.listeners;

import org.openqa.selenium.WebDriver;

import io.wcm.qa.galenium.util.TestDevice;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * Listener to manage WebDriver management, reporting and screenshots.
 */
public class DefaultGaleniumListener extends AbstractGaleniumListener {

  private Boolean isDriverInitialized = false;

  @Override
  protected void closeDriver() {
    WebDriverManager.get().closeDriver();
    isDriverInitialized = false;
  }

  @Override
  protected TestDevice getTestDevice() {
    return WebDriverManager.get().getTestDevice();
  }

  @Override
  protected WebDriver getDriver() {
    WebDriver driver = null;
    if (isDriverInitialized) {
      driver = WebDriverManager.getCurrentDriver();
    }
    else if (getTestDevice() != null) {
      driver = WebDriverManager.getDriver(getTestDevice());
      isDriverInitialized = true;
    }
    return driver;
  }

}
