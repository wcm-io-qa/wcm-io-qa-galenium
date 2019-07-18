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

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.Marker;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.webdriver.WebDriverManagement;

/**
 * Utility methods for interaction with web elements.
 */
public final class Element {

  private static final Marker MARKER = GaleniumReportUtil.getMarker("galenium.interaction.element");

  private Element() {
    // do not instantiate
  }

  /**
   * Click element.
   * @param selector identifies the element
   */
  public static void click(Selector selector) {
    WebElement element = findOrFail(selector);
    click(selector, element);
  }

  /**
   * Click element with specific text.
   * @param selector identifies the elements to be checked for partial text
   * @param searchStr string to be found as part of text of element
   */
  public static boolean clickByPartialText(Selector selector, String searchStr) {
    getLogger().debug("looking for pattern: '" + searchStr + "'");
    WebElement element = findByPartialText(selector, searchStr);
    if (element != null) {
      clickNth(selector, 0, element, "(found by string '" + searchStr + "')");
      return true;
    }
    getLogger().debug("did not find element for text and selector combination: '" + searchStr + "' AND '" + selector.elementName() + "'");
    return false;
  }

  /**
   * Click element.
   * @param selector identifies the element
   */
  public static void clickNth(Selector selector, int index) {
    WebElement element = findNthOrFail(selector, index);
    clickNth(selector, index, element, "");
  }

  /**
   * @param selector used to find first matching element
   * @return matching element if it is visible or null
   */
  public static WebElement find(Selector selector) {
    return find(selector, TimeoutType.DEFAULT);
  }

  /**
   * @param selector used to find elements
   * @return list of elements matched by selector
   */
  public static List<WebElement> findAll(Selector selector) {
    return findAll(selector, TimeoutType.DEFAULT);
  }

  /**
   * @param selector used to find elements
   * @return list of elements matched by selector
   */
  public static List<WebElement> findAllNow(Selector selector) {
    return findAll(selector, TimeoutType.NOW);
  }

  /**
   * Find elements by partial text.
   * @param selector used to find elements
   * @param searchStr used to filter elements that contain this text
   * @return matching element if it is visible or null
   */
  public static WebElement findByPartialText(Selector selector, String searchStr) {
    List<WebElement> elements = findAll(selector);
    for (WebElement element : elements) {
      String text = element.getText();
      if (StringUtils.containsIgnoreCase(text, searchStr)) {
        return element;
      }
    }
    return null;
  }

  /**
   * Will return immediately whether element is found or not.
   * @param selector used to find element
   * @return matching element if it is visible or null
   */
  public static WebElement findNow(Selector selector) {
    return find(selector, TimeoutType.NOW);
  }

  private static WebElement find(Selector selector, TimeoutType timeout) {
    return findNth(selector, 0, timeout);
  }

  /**
   * @param selector used to find elements
   * @param index used to choose which element
   * @return matching element if it is visible or null
   */
  public static WebElement findNth(Selector selector, int index) {
    return findNth(selector, index, TimeoutType.DEFAULT);
  }

  /**
   * @param selector used to find elements
   * @param index used to choose which element
   * @return matching element if it is immediately visible or null
   */
  public static WebElement findNthNow(Selector selector, int index) {
    return findNth(selector, index, TimeoutType.NOW);
  }

  /**
   * Return nth element or fail with {@link GaleniumException}.
   * @param selector identifies elements
   * @param index identifies which element
   * @return matching element
   * @throws GaleniumException when element cannot be found
   */
  public static WebElement findNthOrFail(Selector selector, int index) {
    return findNthOrFail(selector, index, TimeoutType.DEFAULT);
  }

  /**
   * Return nth element immediately or fail with {@link GaleniumException}.
   * @param selector identifies elements
   * @param index identifies which element
   * @return matching element
   * @throws GaleniumException when element cannot be found
   */
  public static WebElement findNthOrFailNow(Selector selector, int index) {
    return findNthOrFail(selector, index, TimeoutType.NOW);
  }

  /**
   * Return element or fail with {@link GaleniumException}.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement findOrFail(Selector selector) {
    return findNthOrFail(selector, 0);
  }

  /**
   * Return element or fail with {@link GaleniumException} immediately.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement findOrFailNow(Selector selector) {
    int index = 0;
    return findNthOrFailNow(selector, index);
  }

  /**
   * Checks whether element attribute value equals string argument.
   * @param selector identifies element
   * @param name attribute to check
   * @param value value to compare against
   * @return whether element with attribute exists and attribute string representation is equal to value.
   */
  public static boolean hasAttribute(Selector selector, String name, String value) {
    WebElement element = find(selector);
    if (element == null) {
      return false;
    }
    return StringUtils.equals(value, element.getAttribute(name));
  }

  /**
   * Checks for CSS class on element.
   * @param selector identifies element
   * @param cssClass CSS class to check for
   * @return whether element has a CSS class equal to the value passed
   */
  public static boolean hasCssClass(Selector selector, String cssClass) {
    WebElement element = find(selector);
    if (element == null) {
      return false;
    }
    String[] split = element.getAttribute("class").split(" ");

    return ArrayUtils.contains(split, cssClass);
  }

  /**
   * @param selector identifies element
   * @return whether element can be found immediately and is displayed
   */
  public static boolean isNthVisible(Selector selector, int index) {
    WebElement element = findNth(selector, index);
    return isVisible(element);
  }

  /**
   * @param selector identifies element
   * @return whether element can be found immediately and is displayed
   */
  public static boolean isNthVisibleNow(Selector selector, int index) {
    WebElement element = findNthNow(selector, index);
    return isVisible(element);
  }

  /**
   * @param selector identifies element
   * @return whether element can be found and is displayed
   */
  public static boolean isVisible(Selector selector) {
    return isNthVisible(selector, 0);
  }

  /**
   * @param selector identifies element
   * @return whether element can be found and is displayed
   */
  public static boolean isVisibleNow(Selector selector) {
    return isNthVisibleNow(selector, 0);
  }

  /**
   * Scroll element into view.
   * @param selector identifies element
   */
  public static void scrollTo(Selector selector) {
    int index = 0;
    scrollToNth(selector, index);
  }

  /**
   * Scroll element into view.
   * @param elementToScrollTo element to scroll to
   */
  public static void scrollTo(WebElement elementToScrollTo) {
    Actions actions = new Actions(getDriver());
    actions.moveToElement(elementToScrollTo);
    actions.perform();
  }

  /**
   * Scroll nth element into view.
   * @param selector identifies element
   */
  public static void scrollToNth(Selector selector, int index) {
    StringBuilder message = getSelectorMessageBuilder("Scrolling to element: ", selector, index);
    getLogger().debug(MARKER_INFO, message.toString());
    WebElement elementToScrollTo = findNth(selector, index);
    scrollTo(elementToScrollTo);
  }

  private static void click(Selector selector, WebElement element) {
    clickNth(selector, 0, element, "");
  }

  private static void clickNth(Selector selector, int index, WebElement element, String extraMessage) {
    try {
      element.click();
    }
    catch (StaleElementReferenceException ex) {
      StringBuilder message = getSelectorMessageBuilder("Stale element when attempting to click ", selector, index)
          .append(": '")
          .append(ex.getMessage());
      getLogger().debug(message.toString());
      findNthOrFailNow(selector, index).click();
    }
    getLogger().info(MARKER_PASS, getClickLogMessage(selector, index, extraMessage));
  }

  private static List<WebElement> findAll(Selector selector, TimeoutType type) {
    WebDriver driver = getDriver();
    if (isNow(type)) {
      switchDriverToNow();
    }
    List<WebElement> allElements = driver.findElements(selector.asBy());
    switchDriverToDefaultTimeout();
    return allElements;
  }

  private static WebElement findNth(Selector selector, int index, TimeoutType type) {

    List<WebElement> allElements = findAll(selector, type);

    if (getLogger().isTraceEnabled()) {
      StringBuilder message = getSelectorMessageBuilder("looking for ", selector, index);
      message.append(" and found ").append(allElements.size()).append(" element(s) total");
      getLogger().trace(message.toString());
    }

    if (allElements.size() > index) {
      return allElements.get(index);
    }

    // not enough elements found
    return null;
  }

  private static WebElement findNthOrFail(Selector selector, int index, TimeoutType type) {
    List<WebElement> allElements = findAll(selector, type);

    StringBuilder message = getSelectorMessageBuilder("looking for ", selector, index);
    if (allElements.isEmpty()) {
      // no elements
      message.append(": no elements found");
      throw new GaleniumException(message.toString());
    }
    if (allElements.size() <= index) {
      // not enough elements
      message.append(" only found: ").append(allElements.size());
      throw new GaleniumException(message.toString());
    }

    // success
    message.append(" and found ").append(allElements.size()).append(" element(s) total");
    getLogger().trace(message.toString());
    WebElement element = allElements.get(index);
    if (element != null) {
      return element;
    }
    StringBuilder failureMessage = getSelectorMessageBuilder("did not find ", selector, index);
    throw new GaleniumException(failureMessage.toString());
  }

  private static String getClickLogMessage(Selector selector, int index, String extraMessage) {
    StringBuilder message = getSelectorMessageBuilder("clicked ", selector, index);
    if (StringUtils.isNotBlank(extraMessage)) {
      message.append(" ");
      message.append(extraMessage);
    }
    return message.toString();
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER);
  }

  private static StringBuilder getSelectorMessageBuilder(String prefix, Selector selector, int index) {
    StringBuilder message = new StringBuilder()
        .append(prefix)
        .append('\'')
        .append(selector)
        .append('[')
        .append(index)
        .append("]'");
    return message;
  }

  private static boolean isNow(TimeoutType type) {
    return type == TimeoutType.NOW;
  }

  private static boolean isVisible(WebElement element) {
    return element != null && element.isDisplayed();
  }

  private static void switchDriverToDefaultTimeout() {
    WebDriverManagement.setDefaultTimeout();
  }

  private static void switchDriverToNow() {
    WebDriverManagement.setZeroTimeout();
  }

  private enum TimeoutType {
    /** Uses implicit wait configured in webdriver */
    DEFAULT,
    /** Uses timeout of zero seconds. */
    NOW;
  }

}
