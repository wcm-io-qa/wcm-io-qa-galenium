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
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.galenframework.browser.SeleniumBrowser;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Mouse interaction methods.
 */
public final class Mouse {

  private Mouse() {
    // do not instantiate
  }

  /**
   * Click at current position.
   */
  public static void click() {
    getLogger().debug("Clicking at current position.");
    getActions().click().perform();
  }

  /**
   * Click at location of element. Useful when encountering 'other element would receive click' exceptions.
   * @param selector identifies element
   */
  public static void clickLocation(Selector selector) {
    clickLocation(selector, 0);
  }

  /**
   * Click at location of nth element defined by selector. Useful when encountering 'other element would receive click'
   * exceptions.
   * @param selector identifies elements
   * @param index which of the elements
   */
  public static void clickLocation(Selector selector, int index) {
    moveTo(selector, index);
    click();
  }

  /**
   * @return current vertical scroll position of browser with 0 being the very top
   */
  public static Long getVerticalScrollPosition() {
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
   * Hover mouse over element.
   * @param selector identifies element.
   */
  public static void hover(Selector selector) {
    getLogger().debug("attempting mouse over: " + selector.elementName());
    int index = 0;
    hover(selector, index);
  }

  /**
   * Hover mouse over element over nth element defined by selector.
   * @param selector identifies elements
   * @param index identifies which of the elements
   */
  public static void hover(Selector selector, int index) {
    WebDriver driver = getDriver();
    List<WebElement> mouseOverElements = driver.findElements(selector.asBy());
    if (!mouseOverElements.isEmpty()) {
      WebElement mouseOverElement = mouseOverElements.get(index);
      if (mouseOverElement.isDisplayed()) {
        try {
          moveTo(selector);
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
   * Move horizontally.
   * @param horizontalOffset
   * @param verticalOffset
   */
  public static void moveByOffset(int horizontalOffset, int verticalOffset) {
    getActions().moveByOffset(horizontalOffset, verticalOffset).perform();
  }

  /**
   * Move mouse horizontally over page and scroll if necessary to keep it in viewport.
   * @param horizontalOffset to move mouse horizontally by (negative values move to the left.
   */
  public static void moveHorizontally(int horizontalOffset) {
    if (horizontalOffset > 0) {
      getLogger().debug(MARKER_INFO, "move mouse right by " + horizontalOffset);
    }
    else if (horizontalOffset < 0) {
      getLogger().debug(MARKER_INFO, "move mouse left by " + -horizontalOffset);
    }
    int verticalOffset = 0;
    moveByOffset(horizontalOffset, verticalOffset);
  }

  /**
   * Move mouse pointer to element.
   * @param selector identifies element
   */
  public static void moveTo(Selector selector) {
    moveTo(selector, 0);
  }

  /**
   * Move mouse pointer to element.
   * @param selector identifies element
   * @param index which of the elements
   */
  public static void moveTo(Selector selector, int index) {
    List<WebElement> elements = Element.findAll(selector);
    if (elements.isEmpty()) {
      throw new GaleniumException("when moving to '" + selector + "': no elements found");
    }
    if (elements.size() <= index) {
      StringBuilder message = new StringBuilder()
          .append("when moving to '")
          .append(selector)
          .append("[")
          .append(index)
          .append("]: only found ")
          .append(elements.size())
          .append(" elements, but needed ")
          .append(index + 1);
      throw new GaleniumException(message.toString());
    }
    WebElement element = elements.get(index);
    getLogger().debug("Moving to element: " + element);
    getActions().moveToElement(element).perform();
  }

  private static Actions getActions() {
    return new Actions(getDriver());
  }

}
