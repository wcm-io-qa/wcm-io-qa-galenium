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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_FAIL;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.Marker;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.base.Selector;

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
    element.click();
    getLogger().info(MARKER_PASS, "clicked '" + selector.elementName() + "'");
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
      getLogger().info("clicked: " + element + " (found by " + selector.elementName() + " with string '" + searchStr + "')");
      element.click();
    }
    else {
      getLogger().debug("did not find element for text and selector combination: '" + searchStr + "' AND '" + selector.elementName() + "'");
    }
  }

  /**
   * Click first element only even if many are found.
   * @param selector identifies the element
   */
  public static void clickFirstVisibleOfMany(Selector selector) {
    List<WebElement> elements = findAll(selector);

    for (WebElement element : elements) {
      getLogger().debug("found element with " + selector.elementName() + ": " + element);
      if (element.isDisplayed()) {
        getLogger().info(MARKER_PASS, "clicking element: " + element);
        element.click();
        return;
      }
    }
  }

  /**
   * Click element only if it is visible and don't fail if element cannot be found.
   * @param selector identifies the element
   */
  public static void clickIfVisible(Selector selector) {
    WebElement element = find(selector, 30);
    if (element != null) {
      element.click();
      getLogger().info(MARKER_PASS, "clicked optional '" + selector.elementName() + "'");
    }
    else {
      getLogger().debug("did not click optional '" + selector.elementName() + "'");
    }
  }

  /**
   * Enters text into element which replaces any text that might already be in element.
   * @param selector identifies the element
   * @param text value to enter
   */
  public static void enterText(Selector selector, String text) {
    WebElement input = findOrFail(selector);
    input.clear();
    input.sendKeys(text);
  }

  /**
   * @param selector used to find element
   * @return matching element if it is visible or null
   */
  public static WebElement find(Selector selector) {
    return find(selector, 10);
  }

  /**
   * @param selector used to find element
   * @param howLong how long to wait for element to be visible in seconds
   * @return matching element if it is visible or null
   */
  public static WebElement find(Selector selector, int howLong) {
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
   * @param selector used to find elements
   * @return list of elements matched by selector
   */
  public static List<WebElement> findAll(Selector selector) {
    return getDriver().findElements(selector.asBy());
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
    return find(selector, 0);
  }

  /**
   * Return element or fail with {@link GaleniumException}.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement findOrFail(Selector selector) {
    return findOrFail(selector, 10);
  }

  /**
   * Return element or fail with {@link GaleniumException}.
   * @param selector identifies the element
   * @param howLong seconds to try until failure
   * @return element found
   */
  public static WebElement findOrFail(Selector selector, int howLong) {
    WebElement element = find(selector, howLong);
    if (element == null) {
      String msg = "could not find '" + selector.elementName() + "'";
      getLogger().debug(MARKER_FAIL, msg);
      throw new GaleniumException(msg);
    }
    return element;
  }

  /**
   * Return element or fail with {@link GaleniumException} immediately.
   * @param selector identifies the element
   * @return element found
   */
  public static WebElement findOrFailNow(Selector selector) {
    return findOrFail(selector, 0);
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

  public static boolean isVisible(Selector selector) {
    return find(selector) != null;
  }

  /**
   * Scroll element into view.
   * @param selector identifies element
   */
  public static void scrollTo(Selector selector) {
    getLogger().debug(MARKER_INFO, "Scrolling to element: '" + selector + "'");
    WebElement elementToScrollTo = getDriver().findElement(selector.asBy());
    scrollTo(elementToScrollTo);
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

  private static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER);
  }

}
