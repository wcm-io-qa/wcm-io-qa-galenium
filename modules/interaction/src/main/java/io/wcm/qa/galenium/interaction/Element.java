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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.Marker;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Utility methods for interaction with web elements.
 */
public final class Element {

  private static final Marker MARKER = GaleniumReportUtil.getMarker("galenium.interaction.element");

  private Element() {
    // do not instantiate
  }

  /**
   * Enters text into element which replaces any text that might already be in element.
   * @param selector identifies the element
   * @param text value to enter
   */
  public static void clearAndenterText(Selector selector, String text) {
    WebElement input = findOrFail(selector);
    try {
      input.clear();
    }
    catch (InvalidElementStateException ex) {
      getLogger().debug(GaleniumReportUtil.MARKER_WARN, "could not clear element: '" + selector + "'");
    }
    input.sendKeys(text);
  }

  /**
   * Click element.
   * @param selector identifies the element
   */
  public static void click(Selector selector) {
    WebElement element = findOrFail(selector);
    element.click();
    getLogger().info(MARKER_PASS, "clicked '" + selector.elementName() + "'");
  }

  /**
   * Click element.
   * @param selector identifies the elements to be checked for partial text
   * @param searchStr string to be found as part of text of element
   */
  public static boolean clickByPartialText(Selector selector, String searchStr) {
    getLogger().debug("looking for pattern: " + searchStr);
    WebElement element = findByPartialText(selector, searchStr);
    if (element != null) {
      getLogger().info("clicked: " + element + " (found by " + selector.elementName() + " with string '" + searchStr + "')");
      element.click();
      return true;
    }
    getLogger().debug("did not find element for text and selector combination: '" + searchStr + "' AND '" + selector.elementName() + "'");
    return false;
  }

  /**
   * @param selector used to find first matching element
   * @return matching element if it is visible or null
   */
  public static WebElement find(Selector selector) {
    return findNth(selector, 0, TimeoutType.DEFAULT);
  }

  /**
   * @param selector used to find elements
   * @return list of elements matched by selector
   */
  public static List<WebElement> findAll(Selector selector) {
    TimeoutType type = TimeoutType.DEFAULT;
    return findAll(selector, type);
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
    return findNth(selector, 0, TimeoutType.NOW);
  }

  /**
   * Return element or fail with {@link GaleniumException}.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement findOrFail(Selector selector) {
    return findOrFailNth(selector, 0, TimeoutType.DEFAULT);
  }

  /**
   * Return element or fail with {@link GaleniumException} immediately.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement findOrFailNow(Selector selector) {
    return findOrFailNth(selector, 0, TimeoutType.NOW);
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
   * @param cssClass css class to check for
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
   * @return whether elment can be found and is displayed
   */
  public static boolean isVisible(Selector selector) {
    WebElement element = find(selector);
    return isVisible(element);
  }

  /**
   * @param selector identifies element
   * @return whether elment can be found and is displayed now
   */
  public static boolean isVisibleNow(Selector selector) {
    WebElement element = findNow(selector);
    return isVisible(element);
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
    StringBuilder message = getSelectorMessageBuilder(selector, index).insert(0, "Scrolling to element: ");
    getLogger().debug(MARKER_INFO, message.toString());
    WebElement elementToScrollTo = findNth(selector, index);
    scrollTo(elementToScrollTo);
  }

  private static List<WebElement> findAll(Selector selector, TimeoutType type) {
    WebDriver driver = getDriver();
    if (isNow(type)) {
      switchDriverToNow(driver);
    }
    List<WebElement> allElements = driver.findElements(selector.asBy());
    switchDriverToDefaultTimeout(driver);
    return allElements;
  }

  private static WebElement findNth(Selector selector, int index) {
    return findNth(selector, index, TimeoutType.DEFAULT);
  }

  private static WebElement findNth(Selector selector, int index, TimeoutType type) {
    List<WebElement> allElements = findAll(selector, type);
    StringBuilder message = getSelectorMessageBuilder(selector, index)
        .insert(0, "looking for '");
    if (allElements.isEmpty()) {
      message.append(": no elements found");
      throw new GaleniumException(message.toString());
    }
    if (allElements.size() <= index) {
      message.append(" only found: ").append(allElements.size());
      throw new GaleniumException(message.toString());
    }
    message.append(" and found ").append(allElements.size()).append(" element(s) total");
    getLogger().trace(message.toString());
    return allElements.get(index);
  }

  private static WebElement findOrFailNth(Selector selector, int index, TimeoutType type) {
    WebElement element = findNth(selector, index, type);
    if (element != null) {
      return element;
    }
    throw new GaleniumException("did not find '" + selector + "'");
  }

  private static StringBuilder getSelectorMessageBuilder(Selector selector, int index) {
    StringBuilder message = new StringBuilder()
        .append(selector)
        .append("[")
        .append(index)
        .append("]'");
    return message;
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER);
  }

  private static boolean isNow(TimeoutType type) {
    return type == TimeoutType.NOW;
  }

  private static boolean isVisible(WebElement element) {
    return element != null && element.isDisplayed();
  }

  private static void switchDriverToDefaultTimeout(WebDriver driver) {
    driver.manage().timeouts().implicitlyWait(GaleniumConfiguration.getDefaultWebdriverTimeout(), TimeUnit.SECONDS);
  }

  private static void switchDriverToNow(WebDriver driver) {
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
  }

  private enum TimeoutType {
    /** Uses implicit wait configured in webdriver */
    DEFAULT,
    /** Uses timeout of zero seconds. */
    NOW;
  }

}
