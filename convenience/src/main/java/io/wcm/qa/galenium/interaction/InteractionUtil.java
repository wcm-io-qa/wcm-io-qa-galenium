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
package io.wcm.qa.galenium.interaction;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.util.GridHostExtractor;

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
   * @deprecated Use {@link Alert#accept()} instead
   */
  @Deprecated
  public static boolean acceptAlert() {
    return Alert.accept();
  }

  /**
   * Click element.
   * @param selector identifies the element
   * @deprecated Use {@link Element#click(Selector)} instead
   */
  @Deprecated
  public static void click(Selector selector) {
    Element.click(selector);
  }

  /**
   * Click element.
   * @param selector identifies the elements to be checked for partial text
   * @param searchStr string to be found as part of text of element
   * @deprecated Use {@link Element#clickByPartialText(Selector,String)} instead
   */
  @Deprecated
  public static void clickByPartialText(Selector selector, String searchStr) {
    Element.clickByPartialText(selector, searchStr);
  }

  /**
   * Click element only if it is visible and don't fail if element cannot be found.
   * @param selector identifies the element
   * @deprecated Use {@link Element#clickIfVisible(Selector)} instead
   */
  @Deprecated
  public static void clickIfVisible(Selector selector) {
    Element.clickIfVisible(selector);
  }

  /**
   * Click first element only even if many are found.
   * @param selector identifies the element
   * @deprecated Use {@link Element#clickFirstVisibleOfMany(Selector)} instead
   */
  @Deprecated
  public static void clickVisibleOfMany(Selector selector) {
    Element.clickFirstVisibleOfMany(selector);
  }

  /**
   * Enters text into element which replaces any text that might already be in element.
   * @param selector identifies the element
   * @param text value to enter
   * @deprecated Use {@link Element#enterText(Selector,String)} instead
   */
  @Deprecated
  public static void enterText(Selector selector, String text) {
    Element.enterText(selector, text);
  }

  /**
   * Find elements by partial text.
   * @param selector used to find elements
   * @param searchStr used to filter elements that contain this text
   * @return matching element if it is visible or null
   * @deprecated Use {@link Element#findByPartialText(Selector,String)} instead
   */
  @Deprecated
  public static WebElement findByPartialText(Selector selector, String searchStr) {
    return Element.findByPartialText(selector, searchStr);
  }

  /**
   * @param selector used to find elements
   * @return list of elements matched by selector
   * @deprecated Use {@link Element#findAll(Selector)} instead
   */
  @Deprecated
  public static List<WebElement> findElements(Selector selector) {
    return Element.findAll(selector);
  }

  /**
   * Return element or fail with {@link GaleniumException}.
   * @param selector identifies the element
   * @return element found
   * @deprecated Use {@link Element#findOrFail(Selector)} instead
   */
  @Deprecated
  public static WebElement findOrFail(Selector selector) {
    return Element.findOrFail(selector);
  }

  /**
   * @param selector used to find element
   * @return matching element if it is visible or null
   * @deprecated Use {@link Element#find(Selector)} instead
   */
  @Deprecated
  public static WebElement getElementVisible(Selector selector) {
    return Element.find(selector);
  }

  /**
   * @param selector used to find element
   * @param howLong how long to wait for element to be visible in seconds
   * @return matching element if it is visible or null
   * @deprecated Use {@link Element#find(Selector,int)} instead
   */
  @Deprecated
  public static WebElement getElementVisible(Selector selector, int howLong) {
    return Element.find(selector, howLong);
  }

  /**
   * @return the hostname of the Selenium Grid node the test is run on or {@link GridHostExtractor#NO_HOST_RETRIEVED} if
   *         hostname cannot be retrieved or NOT_REMOTE if driver is not a
   *         {@link RemoteWebDriver}.
   * @deprecated Use {@link GridHostExtractor#getGridNodeHostname()} instead
   */
  @Deprecated
  public static String getGridNodeHostname() {
    return GridHostExtractor.getGridNodeHostname();
  }

  /**
   * @return current vertical scroll position of browser with 0 being the very top
   * @deprecated Use {@link Mouse#getVerticalScrollPosition()} instead
   */
  @Deprecated
  public static Long getScrollYPosition() {
    return Mouse.getVerticalScrollPosition();
  }

  /**
   * Checks whether element attribute value equals string argument.
   * @param selector identifies element
   * @param name attribute to check
   * @param value value to compare against
   * @return whether element with attribute exists and attribute string representation is equal to value.
   * @deprecated Use {@link Element#hasAttribute(Selector,String,String)} instead
   */
  @Deprecated
  public static boolean hasAttribute(Selector selector, String name, String value) {
    return Element.hasAttribute(selector, name, value);
  }

  /**
   * Checks for CSS class on element.
   * @param selector identifies element
   * @param cssClass css class to check for
   * @return whether element has a CSS class equal to the value passed
   * @deprecated Use {@link Element#hasCssClass(Selector,String)} instead
   */
  @Deprecated
  public static boolean hasCssClass(Selector selector, String cssClass) {
    return Element.hasCssClass(selector, cssClass);
  }

  /**
   * @return whether browser is showing an alert popup.
   * @deprecated Use {@link Alert#isShowing()} instead
   */
  @Deprecated
  public static boolean isAlertShowing() {
    return Alert.isShowing();
  }

  /**
   * @param url to check against
   * @return whether browser is currently pointing at URL
   * @deprecated Use {@link Browser#isCurrentUrl(String)} instead
   */
  @Deprecated
  public static boolean isCurrentUrl(String url) {
    return Browser.isCurrentUrl(url);
  }

  /**
   * @param selector to check
   * @return whether element is visible
   * @deprecated Use {@link Element#isVisible(Selector)} instead
   */
  @Deprecated
  public static boolean isElementVisible(Selector selector) {
    return Element.isVisible(selector);
  }

  /**
   * Load URL in browser.
   * @param url to load
   * @deprecated Use {@link Browser#load(String)} instead
   */
  @Deprecated
  public static void loadUrl(String url) {
    Browser.load(url);
  }

  /**
   * Load URL in browser and fail test if URL does not match.
   * @param url to load
   * @deprecated Use {@link Browser#loadExactly(String)} instead
   */
  @Deprecated
  public static void loadUrlExactly(String url) {
    Browser.loadExactly(url);
  }

  /**
   * Hover mouse over element.
   * @param selector identifies element.
   * @deprecated Use {@link Mouse#hover(Selector)} instead
   */
  @Deprecated
  public static void mouseOver(Selector selector) {
    Mouse.hover(selector);
  }

  /**
   * Move mouse horizontally over page and scroll if necessary to keep it in viewport.
   * @param offsetInPixel to move mouse horizontally by (negative values move to the left.
   * @deprecated Use {@link Mouse#moveHorizontally(int)} instead
   */
  @Deprecated
  public static void moveMouseHorizontally(int offsetInPixel) {
    Mouse.moveHorizontally(offsetInPixel);
  }

  /**
   * Scroll element into view.
   * @param selector identifies element
   * @deprecated Use {@link Element#scrollTo(Selector)} instead
   */
  @Deprecated
  public static void scrollToElement(Selector selector) {
    Element.scrollTo(selector);
  }

  /**
   * Scroll element into view.
   * @param elementToScrollTo element to scroll to
   * @deprecated Use {@link Element#scrollTo(WebElement)} instead
   */
  @Deprecated
  public static void scrollToElement(WebElement elementToScrollTo) {
    Element.scrollTo(elementToScrollTo);
  }

  /**
   * Load URL and wait for it to be loaded.
   * @param url to load
   * @deprecated Use {@link Wait#forUrl(String)} instead
   */
  @Deprecated
  public static void waitForUrl(String url) {
    Wait.forUrl(url);
  }

  /**
   * Load URL and wait passed number of seconds for it to be loaded.
   * @param url to load
   * @param timeOutInSeconds how long to wait for URL to be current
   * @deprecated Use {@link Wait#forUrl(String,int)} instead
   */
  @Deprecated
  public static void waitForUrl(String url, int timeOutInSeconds) {
    Wait.forUrl(url, timeOutInSeconds);
  }

}
