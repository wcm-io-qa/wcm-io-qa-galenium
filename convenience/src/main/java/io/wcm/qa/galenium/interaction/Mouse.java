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

import io.wcm.qa.galenium.selectors.Selector;

public final class Mouse {

  private Mouse() {
    // do not instantiate
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
    WebDriver driver = getDriver();
    List<WebElement> mouseOverElements = driver.findElements(selector.asBy());
    if (!mouseOverElements.isEmpty()) {
      WebElement mouseOverElement = mouseOverElements.get(0);
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

  public static void moveByOffset(int horizontalOffset, int verticalOffset) {
    getActions().moveByOffset(horizontalOffset, verticalOffset).perform();
  }

  public static void moveTo(Selector selector) {
    WebElement element = Element.findOrFail(selector);
    getLogger().debug("Moving to element: " + element);
    getActions().moveToElement(element).perform();
  }

  public static void clickLocation(Selector selector) {
    moveTo(selector);
    click();
  }

  public static void click() {
    getLogger().debug("Clicking at current position.");
    getActions().click().perform();
  }

  private static Actions getActions() {
    return new Actions(getDriver());
  }

}
