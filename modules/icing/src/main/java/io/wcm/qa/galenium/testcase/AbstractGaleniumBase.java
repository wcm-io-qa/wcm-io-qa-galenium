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
package io.wcm.qa.galenium.testcase;

import static io.wcm.qa.galenium.util.GaleniumContext.getContext;

import org.slf4j.Logger;
import org.testng.ITest;
import org.testng.SkipException;

import io.wcm.qa.galenium.assertions.GaleniumAssertion;
import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.differences.specialized.TestNameDifferences;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.webdriver.HasDevice;

/**
 * Abstract base class encapsulating basic interaction with Selenium and reporting.
 *
 * @since 1.0.0
 */
public abstract class AbstractGaleniumBase implements ITest, HasDevice {

  private TestDevice device;
  private TestNameDifferences nameDifferences = new TestNameDifferences();

  /**
   * Constructor.
   *
   * @param testDevice test device to use
   */
  public AbstractGaleniumBase(TestDevice testDevice) {
    setDevice(testDevice);
    getNameDifferences().setTestDevice(testDevice);
    getNameDifferences().setClass(getClass());
    getNameDifferences().setClassNameMaxLength(30);
  }

  /** {@inheritDoc} */
  @Override
  public TestDevice getDevice() {
    return device;
  }

  /**
   * Convenience method delegating to {@link io.wcm.qa.galenium.reporting.GaleniumReportUtil#getLogger()}.
   *
   * @return current logger
   */
  public Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  /** {@inheritDoc} */
  @Override
  public String getTestName() {
    return getNameDifferences().asPropertyKey();
  }

  protected String getBaseUrl() {
    return GaleniumConfiguration.getBaseUrl();
  }

  protected TestNameDifferences getNameDifferences() {
    return nameDifferences;
  }

  protected void setAssertion(GaleniumAssertion assertion) {
    getContext().setAssertion(assertion);
  }

  protected void setDevice(TestDevice device) {
    this.device = device;
  }

  protected void setNameDifferences(TestNameDifferences nameDifferences) {
    this.nameDifferences = nameDifferences;
  }

  protected void skipTest(String skipMessage) {
    getLogger().info(GaleniumReportUtil.MARKER_SKIP, "Skipping: " + skipMessage);
    throw new SkipException(skipMessage);
  }

  protected void skipTest(String skipMessage, Throwable ex) {
    getLogger().info(GaleniumReportUtil.MARKER_SKIP, "Skipping: " + getTestName(), ex);
    throw new SkipException(skipMessage, ex);
  }

}
