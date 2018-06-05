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
package io.wcm.qa.galenium.interaction;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.util.GaleniumContext;

/**
 * Alert related convenience methods.
 */
public final class Browser {

  private Browser() {
    // do not instantiate
  }

  /**
   * Navigate back.
   */
  public static void back() {
    getLogger().debug("navigating back");
    getDriver().navigate().back();
  }

  /**
   * Navigate forward.
   */
  public static void forward() {
    getLogger().debug("navigating forward");
    getDriver().navigate().forward();
  }

  /**
   * @param url to check against
   * @return whether browser is currently pointing at URL
   */
  public static boolean isCurrentUrl(String url) {
    getLogger().trace("checking current URL");
    return StringUtils.equals(url, getDriver().getCurrentUrl());
  }

  /**
   * Load URL in browser.
   * @param url to load
   */
  public static void load(String url) {
    getLogger().trace("loading URL: '" + url + "'");
    getDriver().get(url);
  }

  /**
   * Load URL in browser and fail test if URL does not match.
   * @param url to load
   */
  public static void loadExactly(String url) {
    load(url);
    GaleniumContext.getAssertion().assertEquals(url, getDriver().getCurrentUrl(), "Current URL should match.");
  }

  /**
   * Navigate to URL.
   * @param url URL to navigate to
   */
  public static void navigateTo(String url) {
    getLogger().debug("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
  }

  /**
   * Navigate to URL.
   * @param url to navigate to
   */
  public static void navigateTo(URL url) {
    getLogger().debug("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
  }

  /**
   * Refresh browser.
   */
  public static void refresh() {
    getLogger().debug("refreshing browser");
    getDriver().navigate().refresh();
  }

}
