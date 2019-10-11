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

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.webdriver.HasDevice;

/**
 * Abstract base class encapsulating basic interaction with Selenium.
 */
public abstract class AbstractBrowserBasedTest extends AbstractNamedTest implements HasDevice {

  private TestDevice device;
  /**
   * Constructor.
   * @param testDevice test device to use
   */
  public AbstractBrowserBasedTest(TestDevice testDevice) {
    super();
    setDevice(testDevice);
  }

  /**
   * @return the test device used for this test run.
   */
  @Override
  public TestDevice getDevice() {
    return device;
  }

  protected String getBaseUrl() {
    return GaleniumConfiguration.getBaseUrl();
  }

  protected void setDevice(TestDevice device) {
    this.device = device;
  }

}
