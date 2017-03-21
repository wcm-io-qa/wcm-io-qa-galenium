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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.galenframework.browser.SeleniumBrowser;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.Selector;

/**
 * Collection of utility methods for interaction with browser. It uses {@link Selector} to be able to seamlessly use
 * Selenium or Galen.
 */
public final class InteractionUtil {

  private InteractionUtil() {
    // Do not instantiate
  }

  public static void click(Selector selector) {
    getElementOrFail(selector).click();
  }

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

  public static void enterText(Selector selector, String text) {
    WebElement input = getElementOrFail(selector);
    input.clear();
    input.sendKeys(text);
  }

  /**
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

  public static WebElement getElementOrFail(Selector selector) {
    WebElement element = getElementVisible(selector, 30);
    if (element == null) {
      String msg = "could not find '" + selector.elementName() + "'";
      getLogger().debug(GaleniumReportUtil.MARKER_FAIL, msg);
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
    WebDriverWait wait = new WebDriverWait(getDriver(), howLong);
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(selector.asBy()));
    }
    catch (TimeoutException tex) {
      return null;
    }
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

  public static boolean hasAttribute(Selector selector, String name, String value) {
    WebElement element = getElementVisible(selector);
    if (element == null) {
      return false;
    }
    return StringUtils.equals(value, element.getAttribute(name));
  }

  public static boolean hasCssClass(Selector selector, String cssClass) {
    WebElement element = getElementVisible(selector);
    if (element == null) {
      return false;
    }
    String[] split = element.getAttribute("class").split(" ");

    return ArrayUtils.contains(split, cssClass);
  }

  public static void mouseOver(Selector selector) {
    getLogger().debug("attempting mouse over: " + selector.elementName());
    WebDriver driver = getDriver();
    List<WebElement> mouseOverElements = driver.findElements(selector.asBy());
    if (!mouseOverElements.isEmpty()) {
      WebElement mouseOverElement = mouseOverElements.get(0);
      if (mouseOverElement.isDisplayed()) {
        getLogger().debug("Moving to element: " + mouseOverElement);
        Actions actions = new Actions(driver);
        actions.moveToElement(mouseOverElement).perform();
      }
    }
    else {
      getLogger().debug("no elements found.");
    }
  }

  public static void moveMouseHorizontally(int offsetInPixel) {
    if (offsetInPixel > 0) {
      getLogger().debug(GaleniumReportUtil.MARKER_INFO, "move mouse right by " + offsetInPixel);
    }
    else if (offsetInPixel < 0) {
      getLogger().debug(GaleniumReportUtil.MARKER_INFO, "move mouse left by " + -offsetInPixel);
    }
    getActions().moveByOffset(offsetInPixel, 0).perform();
  }

  public static void scrollToElement(Selector selector) {
    getLogger().debug(GaleniumReportUtil.MARKER_INFO, "Scrolling to element: '" + selector + "'");
    WebElement elementToScrollTo = getDriver().findElement(selector.asBy());
    scrollToElement(elementToScrollTo);
  }

  public static void scrollToElement(WebElement elementToScrollTo) {
    Actions actions = new Actions(getDriver());
    actions.moveToElement(elementToScrollTo);
    actions.perform();
  }

  private static Actions getActions() {
    return new Actions(getDriver());
  }

}
