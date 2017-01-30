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

import io.wcm.qa.galenium.WebDriverManager;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.Selector;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationErrorException;
import com.galenframework.validation.ValidationObject;
import com.galenframework.validation.ValidationResult;

/**
 * Collection of utility methods for interaction with browser. It uses {@link Selector} to be able to seamlessly use
 * Selenium or Galen.
 */
public final class InteractionUtil {

  private InteractionUtil() {
    // Do not instantiate
  }

  /**
   * @param driver driver
   * @return current vertical scroll position of browser with 0 being the very top
   */
  public static Long getScrollYPosition(WebDriver driver) {
    SeleniumBrowser seleniumBrowser = new SeleniumBrowser(driver);
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
   * @param driver driver
   * @param selector used to find elements
   * @return list of elements matched by selector
   */
  public static List<WebElement> findElements(WebDriver driver, Selector selector) {
    return driver.findElements(selector.asBy());
  }

  /**
   * @param driver driver
   * @param selector used to find element
   * @param howLong how long to wait for element to be visible in seconds
   * @return matching element if it is visible or null
   */
  public static WebElement getElementVisible(WebDriver driver, Selector selector, int howLong) {
    WebDriverWait wait = new WebDriverWait(driver, howLong);
    try {
      return wait.until(ExpectedConditions.visibilityOfElementLocated(selector.asBy()));
    }
    catch (TimeoutException tex) {
      return null;
    }
  }

  /**
   * @param driver driver
   * @param selector used to find element
   * @return matching element if it is visible or null
   */
  public static WebElement getElementVisible(WebDriver driver, Selector selector) {
    return getElementVisible(driver, selector, 10);
  }

  /**
   * @param driver driver
   * @param selector used to find elements
   * @param searchStr used to filter elements that contain this text
   * @return matching element if it is visible or null
   */
  public static WebElement findByPartialText(WebDriver driver, Selector selector, String searchStr) {
    List<WebElement> elements = findElements(driver, selector);
    for (WebElement element : elements) {
      String text = element.getText();
      if (StringUtils.containsIgnoreCase(text, searchStr)) {
        return element;
      }
    }
    return null;
  }

  /**
   * @param layoutReport Galen layout report
   * @param errorMessage message to use for errors and failures
   * @param successMessage message to use in case of success
   */
  public static void handleLayoutReport(LayoutReport layoutReport, String errorMessage, String successMessage) {
    if (!(layoutReport.errors() > 0 || layoutReport.warnings() > 0)) {
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, successMessage);
    }
    else {
      List<ValidationResult> validationErrorResults = layoutReport.getValidationErrorResults();
      for (ValidationResult validationResult : validationErrorResults) {
        ValidationError error = validationResult.getError();
        String errorMessages = StringUtils.join(error.getMessages(), "|");
        if (error.isOnlyWarn()) {
          getLogger().warn(errorMessages);
        }
        else {
          getLogger().error(GaleniumReportUtil.MARKER_FAIL, errorMessages);
        }
      }
      if (layoutReport.errors() > 0) {
        ValidationResult validationResult = layoutReport.getValidationErrorResults().get(0);
        List<String> messages = validationResult.getError().getMessages();
        List<ValidationObject> validationObjects = validationResult.getValidationObjects();
        ValidationErrorException ex = new ValidationErrorException(validationObjects, messages);
        throw new GalenLayoutChecker.GalenLayoutException(errorMessage, ex);
      }
    }
  }

  private static Logger getLogger() {
    return WebDriverManager.get().getLogger();
  }

}
