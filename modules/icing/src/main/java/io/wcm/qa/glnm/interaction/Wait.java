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

import static io.wcm.qa.glnm.webdriver.WebDriverManagement.getWait;

import java.util.function.Function;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wraps WebDriverWait functionalities.
 *
 * @since 1.0.0
 */
public final class Wait {

  private static final int DEFAULT_TIMEOUT = 1;

  private static final Logger LOG = LoggerFactory.getLogger(Wait.class);

  private Wait() {
    // do not instantiate
  }

  /**
   * Wait for domReady for a maximum of one second.
   *
   * @since 1.0.0
   */
  public static void forDomReady() {
    forDomReady(DEFAULT_TIMEOUT);
  }

  /**
   * Wait for "DOM ready" for a maximum of seconds specified by parameter.
   *
   * @param timeOutInSeconds how long to wait for "DOM ready"
   * @since 1.0.0
   */
  public static void forDomReady(int timeOutInSeconds) {
    WebDriverWait wait = getWait(timeOutInSeconds);
    wait.until(driver -> ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete"));
  }

  /**
   * Load URL and wait for it to be loaded.
   *
   * @param url to load
   * @since 1.0.0
   */
  public static void forUrl(String url) {
    forUrl(url, DEFAULT_TIMEOUT);
  }

  /**
   * Load URL and wait passed number of seconds for it to be loaded.
   *
   * @param url to load
   * @param timeOutInSeconds how long to wait for URL to be current
   * @since 1.0.0
   */
  public static void forUrl(String url, int timeOutInSeconds) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("waiting for URL: '" + url + "'");
    }
    WebDriverWait wait = getWait(timeOutInSeconds);
    wait.until((Function<? super WebDriver, Boolean>)ExpectedConditions.urlToBe(url));
    if (LOG.isTraceEnabled()) {
      LOG.trace("found URL: '" + url + "'");
    }
  }

}
