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

import com.relevantcodes.extentreports.ExtentTest;

import io.wcm.qa.galenium.assertions.GaleniumAssertion;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.verification.strategy.DefaultVerificationStrategy;
import io.wcm.qa.galenium.verification.strategy.IgnoreFailuresStrategy;
import io.wcm.qa.galenium.verification.strategy.VerificationStrategy;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

public class GaleniumContext {

  private static final ThreadLocal<GaleniumContext> THREAD_LOCAL_CONTEXT = new ThreadLocal<GaleniumContext>() {
    @Override
    protected GaleniumContext initialValue() {
      return new GaleniumContext();
    }
  };

  private Map<String, Object> additionalMappings = new HashMap<String, Object>();
  private Assertion assertion = new GaleniumAssertion();
  private WebDriver driver;
  private ExtentTest extentTest;
  private String testDescription;
  private TestDevice testDevice;
  private String testName;
  private VerificationStrategy verificationStrategy = (GaleniumConfiguration.isSamplingVerificationIgnore()
      ? new IgnoreFailuresStrategy()
      : new DefaultVerificationStrategy());

  public void setAssertion(Assertion assertion) {
    this.assertion = assertion;
  }

  public void setDriver(WebDriver driver) {
    this.driver = driver;
  }

  public void setExtentTest(ExtentTest extentTest) {
    this.extentTest = extentTest;
  }

  public void setTestDescription(String testDescription) {
    this.testDescription = testDescription;
  }

  public void setTestDevice(TestDevice testDevice) {
    this.testDevice = testDevice;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

  public void setVerificationStrategy(VerificationStrategy verificationStrategy) {
    this.verificationStrategy = verificationStrategy;
  }

  @Override
  protected void finalize() throws Throwable {
    try {
      GaleniumReportUtil.getLogger().debug("finalize Galenium context.");
      WebDriverManager.closeDriver();
    }
    finally {
      super.finalize();
    }
  }

  public static Object get(String arg0) {
    return THREAD_LOCAL_CONTEXT.get().additionalMappings.get(arg0);
  }

  public static Assertion getAssertion() {
    return THREAD_LOCAL_CONTEXT.get().assertion;
  }

  public static GaleniumContext getContext() {
    return THREAD_LOCAL_CONTEXT.get();
  }

  public static WebDriver getDriver() {
    return THREAD_LOCAL_CONTEXT.get().driver;
  }

  public static ExtentTest getExtentTest() {
    return THREAD_LOCAL_CONTEXT.get().extentTest;
  }

  public static String getTestDescription() {
    return THREAD_LOCAL_CONTEXT.get().testDescription;
  }

  public static TestDevice getTestDevice() {
    return THREAD_LOCAL_CONTEXT.get().testDevice;
  }

  public static String getTestName() {
    return THREAD_LOCAL_CONTEXT.get().testName;
  }

  public static VerificationStrategy getVerificationStrategy() {
    return THREAD_LOCAL_CONTEXT.get().verificationStrategy;
  }

  public static Object put(String arg0, Object arg1) {
    return THREAD_LOCAL_CONTEXT.get().additionalMappings.put(arg0, arg1);
  }

}
