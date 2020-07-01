/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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
package io.wcm.qa.glnm.webdriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.context.GaleniumContext;

/**
 * Utility class to manage thread safe WebDriver instances.
 *
 * @since 1.0.0
 */
public final class WebDriverManagement {

  private static final int DEFAULT_NUMBER_OF_POLLS_PER_CALL = 5;

  private WebDriverManagement() {
    // do not instantiate
  }

  /**
   * Return the driver already in use. Does not instantiate it and returns
   * null, if no driver is set.
   * *
   *
   * @return driver from current thread's context
   * @since 3.0.0
   */
  public static WebDriver getCurrentDriver() {
    return GaleniumContext.getDriver();
  }

  /**
   * <p>
   * Get a {@link org.openqa.selenium.support.ui.WebDriverWait} for the current driver..
   * </p>
   *
   * @param timeOutInSeconds how many seconds to wait until giving up
   * @return a {@link org.openqa.selenium.support.ui.WebDriverWait} configured with custom timeouts
   * @since 4.0.0
   */
  public static WebDriverWait getWait(int timeOutInSeconds) {
    return getWait(timeOutInSeconds, getPollingIntervalForTimeout(timeOutInSeconds));
  }

  /**
   * <p>
   * Get a {@link org.openqa.selenium.support.ui.WebDriverWait} for the current driver..
   * </p>
   *
   * @param timeOutInSeconds how many seconds to wait until giving up
   * @param pollingInterval how many milliseconds between polls
   * @return a {@link org.openqa.selenium.support.ui.WebDriverWait} configured with custom timeouts
   * @since 4.0.0
   */
  public static WebDriverWait getWait(int timeOutInSeconds, int pollingInterval) {
//    Duration polling = Duration.ofMillis(pollingInterval);
//    Duration timeout = Duration.ofSeconds(timeOutInSeconds);
    WebDriver driver = GaleniumContext.getDriver();
    return new WebDriverWait(driver, timeOutInSeconds, pollingInterval);
  }

  /**
   * Set implicit wait to configured default timeout.
   *
   * @since 3.0.0
   */
  public static void setDefaultTimeout() {
    getCurrentDriver().manage().timeouts().implicitlyWait(GaleniumConfiguration.getDefaultWebdriverTimeout(), TimeUnit.SECONDS);
  }

  /**
   * Set implicit wait to 0 seconds timeout.
   *
   * @since 3.0.0
   */
  public static void setZeroTimeout() {
    getCurrentDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
  }

  private static int getPollingIntervalForTimeout(int timeoutInSeconds) {
    int timeoutInMillis = timeoutInSeconds * 1000;
    return timeoutInMillis / DEFAULT_NUMBER_OF_POLLS_PER_CALL;
  }

}
