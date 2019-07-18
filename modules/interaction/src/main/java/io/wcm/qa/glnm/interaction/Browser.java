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
import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Alert related convenience methods.
 */
public final class Browser {

  private static final String ABOUT_BLANK = "about:blank";

  private Browser() {
    // do not instantiate
  }

  /**
   * Navigate back.
   */
  public static void back() {
    getLogger().info("navigating back");
    getDriver().navigate().back();
  }

  /**
   * Executes Javascript in current browser.
   * @param jsCode code to execute
   * @param parameters parameters to Javascript code
   * @return return value of Javascript execution
   */
  public static Object executeJs(String jsCode, Object... parameters) {
    if (getDriver() instanceof JavascriptExecutor) {
      JavascriptExecutor executor = (JavascriptExecutor)getDriver();
      return executor.executeScript(jsCode, parameters);
    }
    throw new GaleniumException("driver cannot execute Javascript.");
  }

  /**
   * Navigate forward.
   */
  public static void forward() {
    getLogger().info("navigating forward");
    getDriver().navigate().forward();
  }

  /**
   * @param url to check against
   * @return whether browser is currently pointing at URL
   */
  public static boolean isCurrentUrl(String url) {
    getLogger().debug("checking current URL");
    return StringUtils.equals(url, getCurrentUrl());
  }

  public static String getCurrentUrl() {
    return getDriver().getCurrentUrl();
  }

  /**
   * Load URL in browser.
   * @param url to load
   */
  public static void load(String url) {
    getLogger().info("loading URL: '" + url + "'");
    getDriver().get(url);
  }

  /**
   * Loads 'about:blank'.
   */
  public static void loadBlankPage() {
    load(ABOUT_BLANK);
  }

  /**
   * Load URL in browser and fail test if URL does not match.
   * @param url to load
   */
  public static void loadExactly(String url) {
    load(url);
    GaleniumContext.getAssertion().assertEquals(url, getCurrentUrl(), "Current URL should match.");
  }

  /**
   * Navigate to URL.
   * @param url URL to navigate to
   */
  public static void navigateTo(String url) {
    getLogger().info("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
  }

  /**
   * Navigate to URL.
   * @param url to navigate to
   */
  public static void navigateTo(URL url) {
    getLogger().info("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
  }

  /**
   * Refresh browser.
   */
  public static void refresh() {
    getLogger().info("refreshing browser");
    getDriver().navigate().refresh();
  }
}
