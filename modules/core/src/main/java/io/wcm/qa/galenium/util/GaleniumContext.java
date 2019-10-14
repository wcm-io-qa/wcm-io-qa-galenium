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
package io.wcm.qa.galenium.util;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.Assertion;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import io.wcm.qa.galenium.assertions.GaleniumAssertion;
import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.verification.base.Verification;
import io.wcm.qa.galenium.verification.strategy.DefaultVerificationStrategy;
import io.wcm.qa.galenium.verification.strategy.IgnoreFailuresStrategy;
import io.wcm.qa.galenium.verification.strategy.VerificationStrategy;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * Keeps important data for each thread. Simplifies integration without need for rigid inheritance hierarchies. Takes a
 * lot of hassle out of multi-threaded runs.
 *
 * @since 1.0.0
 */
public class GaleniumContext {

  private static final ThreadLocal<GaleniumContext> THREAD_LOCAL_CONTEXT = new ThreadLocal<GaleniumContext>() {
    @Override
    protected GaleniumContext initialValue() {
      return new GaleniumContext();
    }
  };

  private Map<String, Object> additionalMappings = new HashMap<String, Object>();
  private GaleniumAssertion assertion = new GaleniumAssertion();
  private WebDriver driver;
  private ExtentTest extentTest;
  private String testDescription;
  private TestDevice testDevice;
  private String testName;
  private VerificationStrategy verificationStrategy = GaleniumConfiguration.isSamplingVerificationIgnore()
      ? new IgnoreFailuresStrategy()
      : new DefaultVerificationStrategy();

  /**
   * Assertion to use. Default is {@link io.wcm.qa.galenium.assertions.GaleniumAssertion}.
   *
   * @param assertion can be soft assertion
   */
  public void setAssertion(GaleniumAssertion assertion) {
    this.assertion = assertion;
  }

  /**
   * WebDriver to use for all things Galenium. This includes interaction with Galen and Selenium. Usually the WebDriver
   * handling can be left to Galenium. Listeners should initialize the driver based on the configured
   * {@link io.wcm.qa.galenium.device.TestDevice}.
   *
   * @param driver WebDriver to use for everything
   */
  public void setDriver(WebDriver driver) {
    this.driver = driver;
  }

  /**
   * Used for {@link com.relevantcodes.extentreports.ExtentReports} reporting. Galenium should handle this internally without additional interaction.
   *
   * @param extentTest report instance
   */
  public void setExtentTest(ExtentTest extentTest) {
    this.extentTest = extentTest;
  }

  /**
   * A short description of current test.
   *
   * @param testDescription short description of test
   */
  public void setTestDescription(String testDescription) {
    this.testDescription = testDescription;
  }

  /**
   * The test device is central to Galenium's WebDriver handling.
   *
   * @param testDevice device to use
   */
  public void setTestDevice(TestDevice testDevice) {
    this.testDevice = testDevice;
  }

  /**
   * Name to use in reporting.
   *
   * @param testName new test name
   */
  public void setTestName(String testName) {
    this.testName = testName;
  }

  /**
   * Different kinds of {@link io.wcm.qa.galenium.verification.base.Verification} can all use the same strategy. This allows to ignore failures when
   * collecting samples to compare against in future runs.
   *
   * @param verificationStrategy strategy to use
   */
  public void setVerificationStrategy(VerificationStrategy verificationStrategy) {
    this.verificationStrategy = verificationStrategy;
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      GaleniumReportUtil.getLogger().debug("finalize Galenium context.");
      if (getDriver() != null) {
        WebDriverManager.closeDriver();
      }
    }
    finally {
      super.finalize();
    }
  }

  /**
   * Get a custom object for the current thread.
   *
   * @param key used for retrieving custom object
   * @return the value to which the specified key is mapped, or null if this thread context contains no mapping for the
   *         key
   */
  public static Object get(String key) {
    return THREAD_LOCAL_CONTEXT.get().additionalMappings.get(key);
  }

  /**
   * <p>Getter for the field <code>assertion</code>.</p>
   *
   * @return {@link org.testng.asserts.Assertion} to use
   */
  public static GaleniumAssertion getAssertion() {
    return THREAD_LOCAL_CONTEXT.get().assertion;
  }

  /**
   * <p>getContext.</p>
   *
   * @return {@link io.wcm.qa.galenium.util.GaleniumContext} object for this thread
   */
  public static GaleniumContext getContext() {
    return THREAD_LOCAL_CONTEXT.get();
  }

  /**
   * <p>Getter for the field <code>driver</code>.</p>
   *
   * @return driver to use with Selenium and Galen
   */
  public static WebDriver getDriver() {
    return THREAD_LOCAL_CONTEXT.get().driver;
  }

  /**
   * <p>Getter for the field <code>extentTest</code>.</p>
   *
   * @return test report to write to
   */
  public static ExtentTest getExtentTest() {
    return THREAD_LOCAL_CONTEXT.get().extentTest;
  }

  /**
   * <p>Getter for the field <code>testDescription</code>.</p>
   *
   * @return short description of the current test
   */
  public static String getTestDescription() {
    return THREAD_LOCAL_CONTEXT.get().testDescription;
  }

  /**
   * <p>Getter for the field <code>testDevice</code>.</p>
   *
   * @return current test device for this thread
   */
  public static TestDevice getTestDevice() {
    return THREAD_LOCAL_CONTEXT.get().testDevice;
  }

  /**
   * <p>Getter for the field <code>testName</code>.</p>
   *
   * @return name of the current test used for reporting
   */
  public static String getTestName() {
    return THREAD_LOCAL_CONTEXT.get().testName;
  }

  /**
   * <p>Getter for the field <code>verificationStrategy</code>.</p>
   *
   * @return verification strategy to use
   */
  public static VerificationStrategy getVerificationStrategy() {
    return THREAD_LOCAL_CONTEXT.get().verificationStrategy;
  }

  /**
   * Store any object in the current threads context.
   *
   * @param key used to store and retrieve object
   * @param customObject custom object
   * @return the previous value associated with key in this thread, or null if there was no mapping for key
   */
  public static Object put(String key, Object customObject) {
    return THREAD_LOCAL_CONTEXT.get().additionalMappings.put(key, customObject);
  }

}
