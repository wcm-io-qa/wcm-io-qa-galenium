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

import static io.wcm.qa.glnm.context.GaleniumContext.getDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Allure;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Alert related convenience methods.
 *
 * @since 1.0.0
 */
public final class Browser {

  private static final String ABOUT_BLANK = "about:blank";
  private static final Logger LOG = LoggerFactory.getLogger(Browser.class);

  private Browser() {
    // do not instantiate
  }

  /**
   * Navigate back.
   */
  public static void back() {
    GaleniumReportUtil.startStep("navigating back");
    getDriver().navigate().back();
    stopStep();
  }

  /**
   * Executes Javascript in current browser.
   *
   * @param jsCode code to execute
   * @param parameters parameters to Javascript code
   * @return return value of Javascript execution
   */
  public static Object executeJs(String jsCode, Object... parameters) {
    String step = GaleniumReportUtil.startStep("executing JavaScript");
    if (getDriver() instanceof JavascriptExecutor) {
      JavascriptExecutor executor = (JavascriptExecutor)getDriver();
      Object executedScriptResult = executor.executeScript(jsCode, parameters);
      GaleniumReportUtil.passStep(step);
      stopStep();
      return executedScriptResult;
    }
    GaleniumReportUtil.failStep(step);
    stopStep();
    throw new GaleniumException("driver cannot execute Javascript.");
  }

  /**
   * Navigate forward.
   */
  public static void forward() {
    String step = GaleniumReportUtil.startStep("navigating forward");
    getDriver().navigate().forward();
    GaleniumReportUtil.passStep(step);
    stopStep();
  }

  /**
   * <p>getCurrentUrl.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getCurrentUrl() {
    return getDriver().getCurrentUrl();
  }

  /**
   * <p>getLog.</p>
   *
   * @return a {@link io.wcm.qa.glnm.interaction.BrowserLog} object.
   */
  public static BrowserLog getLog() {
    return new BrowserLog();
  }

  /**
   * <p>getPageSource.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getPageSource() {
    return GaleniumContext.getDriver().getPageSource();
  }


  /**
   * <p>getPageTitle.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getPageTitle() {
    return getDriver().getTitle();
  }

  /**
   * <p>isCurrentUrl.</p>
   *
   * @param url to check against
   * @return whether browser is currently pointing at URL
   */
  public static boolean isCurrentUrl(String url) {
    String currentUrl = getCurrentUrl();
    if (LOG.isDebugEnabled()) {
      LOG.debug("checking current URL: " + currentUrl);
    }
    return StringUtils.equals(url, currentUrl);
  }

  /**
   * Load URL in browser.
   *
   * @param url to load
   */
  public static void load(String url) {
    String step = GaleniumReportUtil.startStep("loading URL: '" + url + "'");
    Allure.link(url, url);
    getDriver().get(url);
    GaleniumReportUtil.passStep(step);
    stopStep();
  }

  /**
   * Loads 'about:blank'.
   */
  public static void loadBlankPage() {
    load(ABOUT_BLANK);
  }

  /**
   * Load URL in browser and fail test if URL does not match.
   *
   * @param url to load
   */
  public static void loadExactly(String url) {
    load(url);
    assertThat(getCurrentUrl(), is(url));
  }

  /**
   * Navigate to URL.
   *
   * @param url URL to navigate to
   */
  public static void navigateTo(String url) {
    String step = GaleniumReportUtil.startStep("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
    GaleniumReportUtil.passStep(step);
    stopStep();
  }

  /**
   * Navigate to URL.
   *
   * @param url to navigate to
   */
  public static void navigateTo(URL url) {
    String step = GaleniumReportUtil.startStep("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
    GaleniumReportUtil.passStep(step);
    stopStep();
  }

  /**
   * Refresh browser.
   */
  public static void refresh() {
    String step = GaleniumReportUtil.startStep("refreshing browser");
    getDriver().navigate().refresh();
    GaleniumReportUtil.passStep(step);
    stopStep();
  }

  private static void stopStep() {
    GaleniumReportUtil.stopStep();
  }
}
