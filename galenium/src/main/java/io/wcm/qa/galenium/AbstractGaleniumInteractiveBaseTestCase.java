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
package io.wcm.qa.galenium;

import io.wcm.qa.galenium.imagecomparison.ImageComparisonValidationListener;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GalenLayoutChecker;
import io.wcm.qa.galenium.util.InteractionUtil;
import io.wcm.qa.galenium.util.TestDevice;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationListener;

/**
 * Abstract base test class with methods to interact directly with browser. Uses {@link InteractionUtil}.
 */
public abstract class AbstractGaleniumInteractiveBaseTestCase extends AbstractGaleniumBase {

  /**
   * @param testDevice used for Selenium browser integration
   */
  public AbstractGaleniumInteractiveBaseTestCase(TestDevice testDevice) {
    super(testDevice);
  }

  protected WebElement getElementVisible(Selector selector) {
    return InteractionUtil.getElementVisible(getDriver(), selector, 10);
  }

  protected WebElement getElementVisible(Selector selector, int howLong) {
    WebElement elementVisible = InteractionUtil.getElementVisible(getDriver(), selector, howLong);
    if (elementVisible != null) {
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, "found '" + selector.elementName() + "'");
    }
    return elementVisible;
  }

  protected void assertElementVisible(String message, Selector selector) {
    WebDriver driver = getDriver();
    assertNotNull(InteractionUtil.getElementVisible(driver, selector, 10), message);
    getLogger().debug(GaleniumReportUtil.MARKER_PASS, "visible: " + selector.elementName());
  }

  protected void assertElementNotVisible(String message, Selector selector) {
    assertNull(getElementVisible(selector), message);
    getLogger().debug(GaleniumReportUtil.MARKER_PASS, "not visible: " + selector.elementName());
  }

  protected void clickByPartialText(String searchStr, Selector selector) {
    getLogger().debug("looking for pattern: " + searchStr);
    WebElement element = findByPartialText(searchStr, selector);
    if (element != null) {
      getLogger().debug("clicked: " + element + " (found by " + selector.elementName() + " with string '" + searchStr + "')");
      element.click();
    }
    else {
      getLogger().debug("did not find anything for: " + searchStr + " AND " + selector.elementName());
    }
  }

  protected WebElement findByPartialText(String searchStr, Selector selector) {
    return InteractionUtil.findByPartialText(getDriver(), selector, searchStr);
  }

  protected WebElement getElementOrFail(Selector selector) {
    WebElement element = getElementVisible(selector, 30);
    if (element == null) {
      getLogger().debug(GaleniumReportUtil.MARKER_FAIL, "could not find '" + selector.elementName() + "'");
    }
    assertNotNull(element, "Visibility: '" + selector.elementName() + "'");
    return element;
  }

  protected void mouseOver(Selector selector) {
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

  protected void clickVisibleOfMany(Selector selector) {
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

  protected void click(Selector selector) {
    getElementOrFail(selector).click();
    getLogger().debug(GaleniumReportUtil.MARKER_PASS, "clicked '" + selector.elementName() + "'");
  }

  protected void clickIfVisible(Selector selector) {
    WebElement element = getElementVisible(selector, 30);
    if (element != null) {
      element.click();
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, "clicked optional '" + selector.elementName() + "'");
    }
    else {
      getLogger().debug("did not click optional '" + selector.elementName() + "'");
    }
  }

  protected List<WebElement> findElements(Selector selector) {
    return InteractionUtil.findElements(getDriver(), selector);
  }

  protected Long getScrollYPosition() {
    return InteractionUtil.getScrollYPosition(getDriver());
  }

  protected List<String> getTags() {
    return getDevice().getTags();
  }

  protected void checkLayout(String testName, String specPath) {
    getLogger().debug("checking layout: " + specPath);
    LayoutReport layoutReport = GalenLayoutChecker.checkLayout(testName, specPath, getDevice(), getValidationListener());
    handleLayoutReport(specPath, layoutReport);
  }

  protected void handleLayoutReport(String specName, LayoutReport layoutReport) {
    String errorMessage = "FAILED: Layoutcheck " + specName + " with device " + getDevice();
    String successMessage = "successfully ran spec: " + specName;
    try {
      InteractionUtil.handleLayoutReport(layoutReport, errorMessage, successMessage);
    }
    catch (Throwable ex) {
      fail("layout check failed.", ex);
    }
  }

  protected ValidationListener getValidationListener() {
    return new ImageComparisonValidationListener(getLogger());
  }

  protected void loadUrlExactly(String url) {
    loadUrl(url);
    assertEquals(url, getDriver().getCurrentUrl(), "Current URL should match.");
  }

  protected void loadUrl(String url) {
    WebDriver driver = getDriver();
    getLogger().debug("loading URL: <a href=\"" + url + "\">" + url + "</a>");
    driver.get(url);
    if (!isCurrentUrl(url)) {
      String currentUrl = getDriver().getCurrentUrl();
      getLogger().debug("landed on URL: <a href=\"" + currentUrl + "\">" + currentUrl + "</a>");
    }
    else {
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, "landed on URL: <a href=\"" + url + "\">" + url + "</a>");
    }
  }

  protected boolean isCurrentUrl(String url) {
    return StringUtils.equals(url, getDriver().getCurrentUrl());
  }

}
