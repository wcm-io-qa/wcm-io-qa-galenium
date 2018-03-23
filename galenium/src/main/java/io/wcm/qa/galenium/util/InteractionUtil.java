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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_FAIL;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.galenframework.browser.SeleniumBrowser;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.selectors.Selector;

/**
 * Collection of utility methods for interaction with browser. It uses {@link Selector} to be able to seamlessly use
 * Selenium or Galen.
 * The methods are static to avoid being locked into a certain inheritance hierarchy.
 * To access the {@link WebDriver} it uses {@link GaleniumContext#getDriver()}.
 */
public final class InteractionUtil {

  private InteractionUtil() {
    // Do not instantiate
  }

  /**
   * Accepting alert popups in browser.
   * @return whether an alert was accepted
   */
  public static boolean acceptAlert() {
    if (isAlertShowing()) {
      getDriver().switchTo().alert().accept();
      return true;
    }
    return false;
  }

  /**
   * Click element.
   * @param selector identifies the element
   */
  public static void click(Selector selector) {
    WebElement element = getElementOrFail(selector);
    element.click();
    getLogger().debug(MARKER_PASS, "clicked '" + selector.elementName() + "'");
  }

  /**
   * Click element.
   * @param selector identifies the elements to be checked for partial text
   * @param searchStr string to be found as part of text of element
   */
  public static void clickByPartialText(Selector selector, String searchStr) {
    getLogger().debug("looking for pattern: " + searchStr);
    WebElement element = findByPartialText(selector, searchStr);
    if (element != null) {
      getLogger().debug("clicked: " + element + " (found by " + selector.elementName() + " with string '" + searchStr + "')");
      element.click();
    }
    else {
      getLogger().debug("did not find anything for: " + searchStr + " AND " + selector.elementName());
    }
  }

  /**
   * Click element only if it is visible and don't fail if element cannot be found.
   * @param selector identifies the element
   */
  public static void clickIfVisible(Selector selector) {
    WebElement element = getElementVisible(selector, 30);
    if (element != null) {
      element.click();
      getLogger().debug(MARKER_PASS, "clicked optional '" + selector.elementName() + "'");
    }
    else {
      getLogger().debug("did not click optional '" + selector.elementName() + "'");
    }
  }

  /**
   * Click first element only even if many are found.
   * @param selector identifies the element
   */
  public static void clickVisibleOfMany(Selector selector) {
    List<WebElement> elements = findElements(selector);

    for (WebElement element : elements) {
      getLogger().debug("found element with " + selector.elementName() + ": " + element);
      if (element.isDisplayed()) {
        getLogger().debug("clicking element: " + element);
        element.click();
        return;
      }
    }
  }

  /**
   * Enters text into element which replaces any text that might already be in element.
   * @param selector identifies the element
   * @param text value to enter
   */
  public static void enterText(Selector selector, String text) {
    WebElement input = getElementOrFail(selector);
    input.clear();
    input.sendKeys(text);
  }

  /**
   * Find elements by partial text.
   * @param selector used to find elements
   * @param searchStr used to filter elements that contain this text
   * @return matching element if it is visible or null
   */
  public static WebElement findByPartialText(Selector selector, String searchStr) {
    List<WebElement> elements = findElements(selector);
    for (WebElement element : elements) {
      String text = element.getText();
      if (StringUtils.containsIgnoreCase(text, searchStr)) {
        return element;
      }
    }
    return null;
  }

  /**
   * @param selector used to find elements
   * @return list of elements matched by selector
   */
  public static List<WebElement> findElements(Selector selector) {
    return getDriver().findElements(selector.asBy());
  }

  /**
   * Return element or fail with {@link GaleniumException}.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement getElementOrFail(Selector selector) {
    WebElement element = getElementVisible(selector, 30);
    if (element == null) {
      String msg = "could not find '" + selector.elementName() + "'";
      getLogger().debug(MARKER_FAIL, msg);
      throw new GaleniumException(msg);
    }
    return element;
  }

  /**
   * @param selector used to find element
   * @return matching element if it is visible or null
   */
  public static WebElement getElementVisible(Selector selector) {
    return getElementVisible(selector, 10);
  }

  /**
   * @param selector used to find element
   * @param howLong how long to wait for element to be visible in seconds
   * @return matching element if it is visible or null
   */
  public static WebElement getElementVisible(Selector selector, int howLong) {
    WebElement element = null;
    WebDriverWait wait = new WebDriverWait(getDriver(), howLong);
    try {
      element = wait.until(ExpectedConditions.visibilityOfElementLocated(selector.asBy()));
    }
    catch (TimeoutException tex) {
      getLogger().trace("timeout when waiting for: " + selector.elementName());
    }
    return element;
  }

  /**
   * @return the hostname of the Selenium Grid node the test is run on or {@link GridHostExtractor#NO_HOST_RETRIEVED} if
   *         hostname cannot be retrieved or {@link GridHostExtractor#NOT_REMOTE} if driver is not a
   *         {@link RemoteWebDriver}.
   */
  public static String getGridNodeHostname() {
    WebDriver driver = getDriver();
    if (driver instanceof RemoteWebDriver) {
      String host = System.getProperty("selenium.host");
      int port = Integer.parseInt(System.getProperty("selenium.port", "4444"));
      SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
      return GridHostExtractor.getHostnameAndPort(host, port, sessionId);
    }
    return GridHostExtractor.NOT_REMOTE;
  }

  /**
   * @return current vertical scroll position of browser with 0 being the very top
   */
  public static Long getScrollYPosition() {
    SeleniumBrowser seleniumBrowser = new SeleniumBrowser(getDriver());
    StringBuilder builder = new StringBuilder();
    builder.append("if (window.pageYOffset) ");
    builder.append("return window.pageYOffset;");
    builder.append("else if(window.document.documentElement.scrollTop)");
    builder.append("return window.document.documentElement.scrollTop;");
    builder.append("else ");
    builder.append("return window.document.body.scrollTop;");
    Long scrollYPosition = (Long)seleniumBrowser.executeJavascript(builder.toString());
    return scrollYPosition;
  }

  /**
   * Checks whether element attribute value equals string argument.
   * @param selector identifies element
   * @param name attribute to check
   * @param value value to compare against
   * @return whether element with attribute exists and attribute string representation is equal to value.
   */
  public static boolean hasAttribute(Selector selector, String name, String value) {
    WebElement element = getElementVisible(selector);
    if (element == null) {
      return false;
    }
    return StringUtils.equals(value, element.getAttribute(name));
  }

  /**
   * Checks for CSS class on element.
   * @param selector identifies element
   * @param cssClass css class to check for
   * @return whether element has a CSS class equal to the value passed
   */
  public static boolean hasCssClass(Selector selector, String cssClass) {
    WebElement element = getElementVisible(selector);
    if (element == null) {
      return false;
    }
    String[] split = element.getAttribute("class").split(" ");

    return ArrayUtils.contains(split, cssClass);
  }

  /**
   * @return whether browser is showing an alert popup.
   */
  public static boolean isAlertShowing() {
    try {
      getDriver().switchTo().alert();
      return true;
    }
    catch (NoAlertPresentException e) {
      return false;
    }
  }

  /**
   * @param url to check against
   * @return whether browser is currently pointing at URL
   */
  public static boolean isCurrentUrl(String url) {
    return StringUtils.equals(url, getDriver().getCurrentUrl());
  }

  public static boolean isElementVisible(Selector selector) {
    return getElementVisible(selector) != null;
  }

  /**
   * Load URL in browser.
   * @param url to load
   */
  public static void loadUrl(String url) {
    getLogger().trace("loading URL: '" + url + "'");
    getDriver().get(url);
  }

  /**
   * Load URL in browser and fail test if URL does not match.
   * @param url to load
   */
  public static void loadUrlExactly(String url) {
    loadUrl(url);
    GaleniumContext.getAssertion().assertEquals(url, getDriver().getCurrentUrl(), "Current URL should match.");
  }

  /**
   * Hover mouse over element.
   * @param selector identifies element.
   */
  public static void mouseOver(Selector selector) {
    getLogger().debug("attempting mouse over: " + selector.elementName());
    WebDriver driver = getDriver();
    List<WebElement> mouseOverElements = driver.findElements(selector.asBy());
    if (!mouseOverElements.isEmpty()) {
      WebElement mouseOverElement = mouseOverElements.get(0);
      if (mouseOverElement.isDisplayed()) {
        getLogger().debug("Moving to element: " + mouseOverElement);
        try {
          Actions actions = new Actions(driver);
          actions.moveToElement(mouseOverElement).perform();
        }
        catch (UnsupportedCommandException ex) {
          getLogger().debug("Attempting JS workaround for mouseover.");
          String javaScript = "var evObj = document.createEvent('MouseEvents');" +
              "evObj.initMouseEvent(\"mouseover\",true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
              "arguments[0].dispatchEvent(evObj);";

          ((JavascriptExecutor)driver).executeScript(javaScript, mouseOverElement);
        }
      }
    }
    else {
      getLogger().debug("no elements found.");
    }
  }

  /**
   * Move mouse horizontally over page and scroll if necessary to keep it in viewport.
   * @param offsetInPixel to move mouse horizontally by (negative values move to the left.
   */
  public static void moveMouseHorizontally(int offsetInPixel) {
    if (offsetInPixel > 0) {
      getLogger().debug(MARKER_INFO, "move mouse right by " + offsetInPixel);
    }
    else if (offsetInPixel < 0) {
      getLogger().debug(MARKER_INFO, "move mouse left by " + -offsetInPixel);
    }
    getActions().moveByOffset(offsetInPixel, 0).perform();
  }

  /**
   * Scroll element into view.
   * @param selector identifies element
   */
  public static void scrollToElement(Selector selector) {
    getLogger().debug(MARKER_INFO, "Scrolling to element: '" + selector + "'");
    WebElement elementToScrollTo = getDriver().findElement(selector.asBy());
    scrollToElement(elementToScrollTo);
  }

  /**
   * Scroll element into view.
   * @param elementToScrollTo element to scroll to
   */
  public static void scrollToElement(WebElement elementToScrollTo) {
    Actions actions = new Actions(getDriver());
    actions.moveToElement(elementToScrollTo);
    actions.perform();
  }

  /**
   * Load URL and wait for it to be loaded.
   * @param url to load
   */
  public static void waitForUrl(String url) {
    waitForUrl(url, 5);
  }

  /**
   * Load URL and wait passed number of seconds for it to be loaded.
   * @param url to load
   * @param timeOutInSeconds how long to wait for URL to be current
   */
  public static void waitForUrl(String url, int timeOutInSeconds) {
    getLogger().trace("waiting for URL: '" + url + "'");
    WebDriverWait wait = new WebDriverWait(getDriver(), timeOutInSeconds);
    wait.until(ExpectedConditions.urlToBe(url));
    getLogger().trace("found URL: '" + url + "'");
  }

  private static Actions getActions() {
    return new Actions(getDriver());
  }

}
