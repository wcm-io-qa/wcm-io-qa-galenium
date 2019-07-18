/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.glnm.interaction;

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;

import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.CanCache;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.util.GaleniumContext;
import io.wcm.qa.glnm.verification.base.Verifiable;
import io.wcm.qa.glnm.verification.base.Verification;
import io.wcm.qa.glnm.verification.element.InvisibilityVerification;
import io.wcm.qa.glnm.verification.element.TextVerification;
import io.wcm.qa.glnm.verification.element.VisibilityVerification;

/**
 * Wraps WebDriverWait functionalities.
 */
public final class Wait {

  private static final int DEFAULT_NUMBER_OF_POLLS_PER_CALL = 5;
  private static final int DEFAULT_TIMEOUT = 1;

  private Wait() {
    // do not instantiate
  }

  /**
   * Waits for {@link Verifiable} or {@link Verification}.
   * @param condition to wait for
   */
  public static void forCondition(Verifiable condition) {
    int timeOut = DEFAULT_TIMEOUT;
    forCondition(condition, timeOut);
  }

  /**
   * Waits for {@link Verifiable} or {@link Verification}.
   * @param condition to wait for
   * @param timeout how many seconds to wait
   */
  public static void forCondition(Verifiable condition, int timeout) {
    int pollingInterval = getPollingIntervalForTimeout(timeout);
    forCondition(condition, timeout, pollingInterval);
  }

  /**
   * Waits for {@link Verifiable} or {@link Verification}.
   * @param condition to wait for
   * @param timeOut how many seconds to wait
   * @param pollingInterval how many milliseconds between attempts
   */
  public static void forCondition(Verifiable condition, int timeOut, int pollingInterval) {
    WebDriverWait wait = getWait(timeOut, pollingInterval);
    VerifiableExpectedCondition verifiableCondition = new VerifiableExpectedCondition(condition);
    try {
      wait.until(verifiableCondition);
    }
    catch (TimeoutException ex) {
      if (verifiableCondition.isVerification()) {
        Verification verification = (Verification)verifiableCondition.getVerifiable();
        throw new GaleniumException(verification.getMessage(), ex);
      }
      throw ex;
    }
  }

  /**
   * Wait for domReady for a maximum of one second.
   */
  public static void forDomReady() {
    forDomReady(DEFAULT_TIMEOUT);
  }

  /**
   * Wait for "DOM ready" for a maximum of seconds specified by parameter.
   * @param timeOutInSeconds how long to wait for "DOM ready"
   */
  public static void forDomReady(int timeOutInSeconds) {
    WebDriverWait wait = getWait(timeOutInSeconds);
    wait.until(driver -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
  }

  /**
   * Wait for element to be invisible or not in page.
   * @param selector identifies element
   */
  public static void forInvisibility(Selector selector) {
    forCondition(new InvisibilityVerification(selector), DEFAULT_TIMEOUT);
  }

  /**
   * Wait for element to be invisible or not in page.
   * @param selector identifies element
   * @param timeout how many seconds to wait
   */
  public static void forInvisibility(Selector selector, int timeout) {
    forCondition(new InvisibilityVerification(selector), timeout);
  }

  /**
   * Wait for element to display text from expected properties.
   * @param selector identifies element
   */
  public static void forText(Selector selector) {
    forText(selector, null, DEFAULT_TIMEOUT);
  }

  /**
   * Wait for element to display text from expected properties.
   * @param selector identifies element
   * @param timeout how many seconds to wait
   */
  public static void forText(Selector selector, int timeout) {
    forText(selector, null, timeout);
  }

  /**
   * Wait for element to display text.
   * @param selector identifies element
   * @param text to match
   */
  public static void forText(Selector selector, String text) {
    forText(selector, text, DEFAULT_TIMEOUT);
  }

  /**
   * Wait for element to display text.
   * @param selector identifies element
   * @param text to match
   * @param timeout how many seconds to wait
   */
  public static void forText(Selector selector, String text, int timeout) {
    forCondition(new TextVerification(selector, text), timeout);
  }

  /**
   * Load URL and wait for it to be loaded.
   * @param url to load
   */
  public static void forUrl(String url) {
    forUrl(url, DEFAULT_TIMEOUT);
  }

  /**
   * Load URL and wait passed number of seconds for it to be loaded.
   * @param url to load
   * @param timeOutInSeconds how long to wait for URL to be current
   */
  public static void forUrl(String url, int timeOutInSeconds) {
    getLogger().trace("waiting for URL: '" + url + "'");
    WebDriverWait wait = getWait(timeOutInSeconds);
    wait.until((Function<? super WebDriver, Boolean>)ExpectedConditions.urlToBe(url));
    getLogger().trace("found URL: '" + url + "'");
  }

  /**
   * Wait for element to be visible.
   * @param selector identifies element
   */
  public static void forVisibility(Selector selector) {
    forCondition(new VisibilityVerification(selector), DEFAULT_TIMEOUT);
  }

  /**
   * Wait for element to be visible.
   * @param selector identifies element
   * @param timeout how many seconds to wait
   */
  public static void forVisibility(Selector selector, int timeout) {
    forCondition(new VisibilityVerification(selector), timeout);
  }

  private static int getPollingIntervalForTimeout(int timeoutInSeconds) {
    int timeoutInMillis = timeoutInSeconds * 1000;
    return timeoutInMillis / DEFAULT_NUMBER_OF_POLLS_PER_CALL;
  }

  private static WebDriverWait getWait(int timeOutInSeconds) {
    return getWait(timeOutInSeconds, getPollingIntervalForTimeout(timeOutInSeconds));
  }

  private static WebDriverWait getWait(int timeOutInSeconds, int pollingInterval) {
    return new WebDriverWait(GaleniumContext.getDriver(), timeOutInSeconds, pollingInterval);
  }

  private static final class VerifiableExpectedCondition implements Function<WebDriver, Boolean> {

    private final Verifiable condition;

    private VerifiableExpectedCondition(Verifiable condition) {
      this.condition = condition;
      if (condition instanceof CanCache) {
        CanCache verification = (CanCache)condition;
        getLogger().debug("disable caching for '" + verification + "' verification");
        verification.setCaching(false);
        if (verification.isCaching()) {
          getLogger().warn("waiting for a caching verification is not a sensible thing to do. Offending verification: '" + verification + "'");
        }
      }
    }

    @Override
    public Boolean apply(WebDriver arg0) {
      return this.condition.verify();
    }

    public Verifiable getVerifiable() {
      return condition;
    }

    public boolean isVerification() {
      return getVerifiable() instanceof Verification;
    }

    @Override
    public String toString() {
      return super.toString() + ": '" + condition + "'";
    }
  }

}
