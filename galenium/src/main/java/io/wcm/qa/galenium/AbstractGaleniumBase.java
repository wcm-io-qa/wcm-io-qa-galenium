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
package io.wcm.qa.galenium;

import io.wcm.qa.galenium.reporting.ExtentReportable;
import io.wcm.qa.galenium.reporting.GaleniumLogging;
import io.wcm.qa.galenium.reporting.GaleniumLoggingAdapter;
import io.wcm.qa.galenium.reporting.GaleniumReportable;
import io.wcm.qa.galenium.reporting.HasReportable;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.util.GridHostExtractor;
import io.wcm.qa.galenium.util.TestDevice;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITest;
import org.testng.SkipException;
import org.testng.asserts.Assertion;

/**
 * Abstract base class encapsulating basic interaction with Selenium and reporting.
 */
public abstract class AbstractGaleniumBase implements ITest, GaleniumLogging, HasReportable {

  private Assertion assertion;
  private TestDevice device;
  private GaleniumLogging logging;
  private ExtentReportable reportable;

  /**
   * Constructor.
   * @param testDevice test device to use
   */
  public AbstractGaleniumBase(TestDevice testDevice) {
    setDevice(testDevice);
    setReportable(new GaleniumReportable(getTestName()));
    setLogging(new GaleniumLoggingAdapter(getReportable()));
  }

  protected void assertEquals(boolean actual, boolean expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(boolean actual, boolean expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(byte actual, byte expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(byte actual, byte expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(byte[] actual, byte[] expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(byte[] actual, byte[] expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(char actual, char expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(char actual, char expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(Collection<?> actual, Collection<?> expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(Collection<?> actual, Collection<?> expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(double actual, double expected, double delta) {
    getAssertion().assertEquals(actual, expected, delta);
  }

  protected void assertEquals(double actual, double expected, double delta, String message) {
    getAssertion().assertEquals(actual, expected, delta, message);
  }

  protected void assertEquals(float actual, float expected, float delta) {
    getAssertion().assertEquals(actual, expected, delta);
  }

  protected void assertEquals(float actual, float expected, float delta, String message) {
    getAssertion().assertEquals(actual, expected, delta, message);
  }

  protected void assertEquals(int actual, int expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(int actual, int expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(long actual, long expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(long actual, long expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(Map<?, ?> actual, Map<?, ?> expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(Object[] actual, Object[] expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(Object[] actual, Object[] expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(Set<?> actual, Set<?> expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(Set<?> actual, Set<?> expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(short actual, short expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(short actual, short expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEquals(String actual, String expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected void assertEquals(String actual, String expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected <T> void assertEquals(T actual, T expected) {
    getAssertion().assertEquals(actual, expected);
  }

  protected <T> void assertEquals(T actual, T expected, String message) {
    getAssertion().assertEquals(actual, expected, message);
  }

  protected void assertEqualsNoOrder(Object[] actual, Object[] expected) {
    getAssertion().assertEqualsNoOrder(actual, expected);
  }

  protected void assertEqualsNoOrder(Object[] actual, Object[] expected, String message) {
    getAssertion().assertEqualsNoOrder(actual, expected, message);
  }

  protected void assertFalse(boolean condition) {
    getAssertion().assertFalse(condition);
  }

  protected void assertFalse(boolean condition, String message) {
    getAssertion().assertFalse(condition, message);
  }

  protected void assertNotEquals(double actual, double expected, double delta) {
    getAssertion().assertNotEquals(actual, expected, delta);
  }

  protected void assertNotEquals(double actual, double expected, double delta, String message) {
    getAssertion().assertNotEquals(actual, expected, delta, message);
  }

  protected void assertNotEquals(float actual, float expected, float delta) {
    getAssertion().assertNotEquals(actual, expected, delta);
  }

  protected void assertNotEquals(float actual, float expected, float delta, String message) {
    getAssertion().assertNotEquals(actual, expected, delta, message);
  }

  protected void assertNotEquals(Object actual, Object expected) {
    getAssertion().assertNotEquals(actual, expected);
  }

  protected void assertNotEquals(Object actual, Object expected, String message) {
    getAssertion().assertNotEquals(actual, expected, message);
  }

  protected void assertNotNull(Object object) {
    getAssertion().assertNotNull(object);
  }

  protected void assertNotNull(Object object, String message) {
    getAssertion().assertNotNull(object, message);
  }

  protected void assertNotSame(Object actual, Object expected) {
    getAssertion().assertNotSame(actual, expected);
  }

  protected void assertNotSame(Object actual, Object expected, String message) {
    getAssertion().assertNotSame(actual, expected, message);
  }

  protected void assertNull(Object object) {
    getAssertion().assertNull(object);
  }

  protected void assertNull(Object object, String message) {
    getAssertion().assertNull(object, message);
  }

  protected void assertSame(Object actual, Object expected) {
    getAssertion().assertSame(actual, expected);
  }

  protected void assertSame(Object actual, Object expected, String message) {
    getAssertion().assertSame(actual, expected, message);
  }

  protected void assertTrue(boolean condition) {
    getAssertion().assertTrue(condition);
  }

  protected void assertTrue(boolean condition, String message) {
    getAssertion().assertTrue(condition, message);
  }

  @Override
  public void assignCategory(String category) {
    getLogging().assignCategory(category);
  }

  @Override
  public void debugAndIgnoreException(Throwable ex) {
    getLogging().debugAndIgnoreException(ex);
  }

  @Override
  public void debugException(Throwable ex) {
    getLogging().debugException(ex);
  }

  @Override
  public void debugFailed(String msg) {
    getLogging().debugFailed(msg);
  }

  @Override
  public void debugInfo(String msg) {
    getLogging().debugInfo(msg);
  }

  @Override
  public void debugPassed(String msg) {
    getLogging().debugPassed(msg);
  }

  @Override
  public void debugSkip(String msg) {
    getLogging().debugSkip(msg);
  }

  @Override
  public void debugUnknown(String msg) {
    getLogging().debugUnknown(msg);
  }

  @Override
  public void debugWarning(String msg) {
    getLogging().debugWarning(msg);
  }

  protected void fail() {
    reportFailed("Failed without message.");
    getAssertion().fail();
  }

  protected void fail(String message) {
    reportFailed(message);
    getAssertion().fail(message);
  }

  protected void fail(String message, Throwable realCause) {
    reportException(realCause);
    getAssertion().fail(message, realCause);
  }

  protected Assertion getAssertion() {
    if (assertion == null) {
      assertion = new GaleniumAssertion(getLogging());
    }
    return assertion;
  }

  protected String getBaseUrl() {
    return GaleniumConfiguration.getBaseUrl();
  }

  /**
   * @return the test device used for this test run.
   */
  public TestDevice getDevice() {
    return device;
  }

  protected abstract WebDriver getDriver();

  protected String getGridNodeHostname() {
    WebDriver driver = getDriver();
    if (driver instanceof RemoteWebDriver) {
      String host = System.getProperty("selenium.host");
      int port = Integer.parseInt(System.getProperty("selenium.port", "4444"));
      SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
      return GridHostExtractor.getHostnameAndPort(host, port, sessionId);
    }
    return "NOT_REMOTE";
  }

  protected GaleniumLogging getLogging() {
    return logging;
  }

  @Override
  public ExtentReportable getReportable() {
    return reportable;
  }

  @Override
  public String getTestName() {
    return getClass().getSimpleName() + "/" + getDevice();
  }

  @Override
  public boolean isDebugging() {
    return !GaleniumConfiguration.isSparseReporting();
  }

  @Override
  public void reportException(Throwable ex) {
    getLogging().reportException(ex);
  }

  @Override
  public void reportFailed(String msg) {
    getLogging().reportFailed(msg);
  }

  @Override
  public void reportFatal(String msg) {
    getLogging().reportFatal(msg);
  }

  @Override
  public void reportFatalException(Throwable ex) {
    getLogging().reportFatalException(ex);
  }

  @Override
  public void reportInfo(String msg) {
    getLogging().reportInfo(msg);
  }

  @Override
  public void reportPassed(String msg) {
    getLogging().reportPassed(msg);
  }

  @Override
  public void reportSkip(String msg) {
    getLogging().reportSkip(msg);
  }

  @Override
  public void reportWarning(String msg) {
    getLogging().reportWarning(msg);
  }

  protected void setAssertion(Assertion assertion) {
    this.assertion = assertion;
  }

  protected void setDevice(TestDevice device) {
    this.device = device;
  }

  protected void setLogging(GaleniumLogging logging) {
    this.logging = logging;
  }

  public void setReportable(ExtentReportable reportable) {
    this.reportable = reportable;
  }

  protected void skipTest(String skipMessage) {
    reportSkip("Skipping: " + skipMessage);
    throw new SkipException(skipMessage);
  }

  protected void skipTest(String skipMessage, Throwable ex) {
    debugAndIgnoreException(ex);
    reportSkip("Skipping: " + getTestName());
    throw new SkipException(skipMessage, ex);
  }

}
