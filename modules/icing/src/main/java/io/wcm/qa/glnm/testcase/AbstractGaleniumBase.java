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
package io.wcm.qa.glnm.testcase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.testng.SkipException;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.TestInstanceParameter;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.webdriver.HasDevice;

/**
 * Abstract base class encapsulating basic interaction with Selenium and reporting.
 */
public abstract class AbstractGaleniumBase implements ITest, HasDevice {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractGaleniumBase.class);

  @TestInstanceParameter("{device.name}")
  private TestDevice device;

  /**
   * Constructor.
   * @param testDevice test device to use
   */
  public AbstractGaleniumBase(TestDevice testDevice) {
    setDevice(testDevice);
  }

  /**
   * @return the test device used for this test run.
   */
  @Override
  public TestDevice getDevice() {
    return device;
  }

  @Override
  public String getTestName() {
    return getClass().getName();
  }

  protected String getBaseUrl() {
    return GaleniumConfiguration.getBaseUrl();
  }

  protected void setDevice(TestDevice device) {
    this.device = device;
  }

  protected void skipTest(String skipMessage) {
    LOG.info("Skipping: " + skipMessage);
    Allure.step("Skipping: " + skipMessage);
    throw new SkipException(skipMessage);
  }

  protected void skipTest(String skipMessage, Throwable ex) {
    LOG.info("Skipping: " + getTestName(), ex);
    Allure.step("Skipping: " + getTestName());
    throw new SkipException(skipMessage, ex);
  }

}
