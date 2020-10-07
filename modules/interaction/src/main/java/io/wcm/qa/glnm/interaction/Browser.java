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

import static io.qameta.allure.Allure.addAttachment;
import static io.wcm.qa.glnm.context.GaleniumContext.getDriver;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.passStep;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.startStep;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.stopStep;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.join;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Allure;
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
   * Add cookie to browser.
   *
   * @param cookie to add
   * @since 5.0
   */
  public static void addCookie(Cookie cookie) {
    String step = startStep("add cookie: " + cookie);
    getDriver().manage().addCookie(cookie);
    passStep(step);
    stopStep();
  }

  /**
   * Handles to all open windows/tabs of browser.
   *
   * @return all window handles
   */
  public static Set<String> allWindowHandles() {
    return getDriver().getWindowHandles();
  }

  /**
   * Navigate back.
   */
  public static void back() {
    String step = startStep("navigating back");
    getDriver().navigate().back();
    passStep(step);
    stopStep();
  }

  /**
   * Handle of current window/tab.
   *
   * @return current window handle
   */
  public static String currentWindowHandle() {
    return getDriver().getWindowHandle();
  }

  /**
   * Deletes all cookies for current domain from browser.
   *
   * @since 5.0
   */
  public static void deleteAllCookies() {
    String step = startStep("delete all cookies");
    getDriver().manage().deleteAllCookies();
    passStep(step);
    stopStep();
  }

  /**
   * Deletes cookie from browser.
   *
   * @param cookieName name of cookie to delete
   * @since 5.0
   */
  public static void deleteCookieNamed(String cookieName) {
    String step = startStep("delete cookie: " + cookieName);
    getDriver().manage().deleteCookieNamed(cookieName);
    passStep(step);
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
    String step = startStep("executing JavaScript: " + StringUtils.abbreviateMiddle(jsCode, " ... ", 160));
    if (getDriver() instanceof JavascriptExecutor) {
      addAttachment("executed script", jsCode);
      if (ArrayUtils.isNotEmpty(parameters)) {
        addAttachment("script parameters", "[" + join(parameters, ", ") + "]");
      }
      JavascriptExecutor executor = (JavascriptExecutor)getDriver();
      Object executedScriptResult = executor.executeScript(jsCode, parameters);
      if (executedScriptResult != null) {
        addAttachment("script result", executedScriptResult.toString());
      }
      passStep(step);
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
    String step = startStep("navigating forward");
    getDriver().navigate().forward();
    passStep(step);
    stopStep();
  }

  /**
   * <p>
   * Get cookie for domain by name.
   * </p>
   *
   * @since 5.0
   * @param cookieName name of cookie to retrieve
   * @return cookie for current domain or null
   */
  public static Cookie getCookieNamed(String cookieName) {
    return getDriver().manage().getCookieNamed(cookieName);
  }

  /**
   * <p>
   * Get all cookies for domain.
   * </p>
   *
   * @since 5.0
   * @return a set of cookies for current domain
   */
  public static Set<Cookie> getCookies() {
    return getDriver().manage().getCookies();
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
   * <p>
   * Get browser log entries.
   * </p>
   *
   * @return browser log
   */
  public static List<LogEntry> getLog() {
    String type = LogType.BROWSER;
    return getLog(type);
  }

  /**
   * <p>
   * Get browser log entries at level or higher.
   * </p>
   *
   * @param level to filter
   * @return browser log
   */
  public static List<LogEntry> getLog(Level level) {
    return getLog().stream()
        .filter(e -> e.getLevel().intValue() >= level.intValue())
        .collect(toList());
  }

  /**
   * <p>getPageSource.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public static String getPageSource() {
    return getDriver().getPageSource();
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
   * <p>getPerformanceLog.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public static List<LogEntry> getPerformanceLog() {
    return getLog(LogType.PERFORMANCE);
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
    String step = startStep("loading URL: '" + url + "'");
    Allure.link(url, url);
    getDriver().get(url);
    passStep(step);
    stopStep();
  }

  /**
   * Load URL in browser.
   *
   * @param url to load
   */
  public static void load(URL url) {
    load(url.toString());
  }


  /**
   * Loads 'about:blank'.
   */
  public static void loadBlankPage() {
    load(ABOUT_BLANK);
  }

  /**
   * Navigate to URL.
   *
   * @param url URL to navigate to
   */
  public static void navigateTo(String url) {
    String step = startStep("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
    passStep(step);
    stopStep();
  }


  /**
   * Navigate to URL.
   *
   * @param url to navigate to
   */
  public static void navigateTo(URL url) {
    String step = startStep("navigating to URL: '" + url + "'");
    getDriver().navigate().to(url);
    passStep(step);
    stopStep();
  }

  /**
   * Refresh browser.
   */
  public static void refresh() {
    String step = startStep("refreshing browser");
    getDriver().navigate().refresh();
    passStep(step);
    stopStep();
  }

  /**
   * Switch to window or tab.
   *
   * @param nameOrHandle a {@link java.lang.String} object.
   */
  public static void switchTo(String nameOrHandle) {
    getDriver().switchTo().window(nameOrHandle);
  }

  /**
   * Switch to next window or tab.
   */
  public static void switchToNext() {
    String currentHandle = currentWindowHandle();
    for (Iterator handles = allWindowHandles().iterator(); handles.hasNext();) {
      String handle = (String)handles.next();
      if (StringUtils.equals(handle, currentHandle)) {
        if (handles.hasNext()) {
          switchTo((String)handles.next());
        }
        else {
          switchTo(firstWindowHandle());
        }
      }
    }
  }

  /**
   * Uses JavaScript to determine current viewport size.
   *
   * @return dimension of viewport
   */
  public static Dimension viewportSize() {
    Object rawJsResult = executeJs("return [window.innerWidth, window.innerHeight];");
    if (rawJsResult instanceof ArrayList) {
      ArrayList jsResult = (ArrayList)rawJsResult;
      if (jsResult.size() == 2) {
        int width = getIntFromJsResult(jsResult, 0);
        int height = getIntFromJsResult(jsResult, 1);
        return new Dimension(width, height);
      }
      throw new GaleniumException("Viewport script did not return two element list.");
    }
    throw new GaleniumException("Viewport script did not return list.");
  }

  private static String firstWindowHandle() {
    return allWindowHandles().iterator().next();
  }

  private static int getIntFromJsResult(ArrayList jsResult, int index) {
    Object rawValue = jsResult.get(index);
    if (rawValue instanceof Long) {
      return ((Long)rawValue).intValue();
    }
    throw new GaleniumException("Could not get integer from result list.");
  }

  private static List<LogEntry> getLog(String type) {
    return getDriver().manage().logs().get(type).getAll();
  }

}
