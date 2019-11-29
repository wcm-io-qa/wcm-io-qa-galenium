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
package io.wcm.qa.glnm.listeners.testng;


import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getNumberOfBrowserInstantiationRetries;
import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import io.qameta.allure.Allure;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.util.GaleniumContext;
import io.wcm.qa.glnm.util.TestInfoUtil;
import io.wcm.qa.glnm.webdriver.WebDriverManagement;

/**
 * Handles {@link org.openqa.selenium.WebDriver} life cycle in single and multi threaded scenarios.
 *
 * @since 1.0.0
 */
public class WebDriverListener extends TestListenerAdapter {

  private static final String CATEGORY_WEB_DRIVER_NOT_INSTANTIATED = "WEBDRIVER_NOT_INSTANTIATED";
  private static final Logger LOG = LoggerFactory.getLogger(WebDriverListener.class);

  /** {@inheritDoc} */
  @Override
  public void onFinish(ITestContext context) {
    closeDriver();
  }

  /** {@inheritDoc} */
  @Override
  public void onTestStart(ITestResult result) {
    // do nothing for lazy initialization
    if (GaleniumConfiguration.isLazyWebDriverInitialization()) {
      LOG.debug("Driver will be initialized on first use.");
      return;
    }

    // create driver for test
    int retries = 0;
    TestDevice testDevice = getTestDevice(result);
    if (!WebDriverManagement.isBrowser(testDevice)) {
      return;
    }

    while (retries <= getNumberOfBrowserInstantiationRetries()) {
      try {
        if (testDevice != null) {
          LOG.debug("new driver for: " + testDevice);
          WebDriverManagement.getDriver(testDevice);
        }
        else {
          LOG.debug("no test device set for: " + result.getTestName());
        }
      }
      finally {
        if (getDriver() == null) {
          LOG.warn("driver not instantiated");
          Allure.label(CATEGORY_WEB_DRIVER_NOT_INSTANTIATED, "true");
          retries++;
        }
        else {
          // we have a driver and can move on
          break;
        }
      }
    }
  }

  private TestDevice getTestDevice(ITestResult result) {
    LOG.debug("fetch test device from result.");
    TestDevice testDevice = TestInfoUtil.getTestDevice(result);
    if (testDevice == null) {
      LOG.debug("no test device found in result.");
      testDevice = GaleniumContext.getTestDevice();
    }
    return testDevice;
  }

  protected void closeDriver() {
    WebDriverManagement.closeDriver();
  }

}
