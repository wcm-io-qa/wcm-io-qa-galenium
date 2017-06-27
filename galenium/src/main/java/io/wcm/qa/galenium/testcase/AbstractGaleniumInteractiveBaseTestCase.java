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
package io.wcm.qa.galenium.testcase;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.images.ImageComparisonValidationListener;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GalenLayoutChecker;
import io.wcm.qa.galenium.util.InteractionUtil;
import io.wcm.qa.galenium.util.TestDevice;

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

  protected void assertElementNotVisible(String message, Selector selector) {
    assertNull(getElementVisible(selector), message);
    getLogger().debug(GaleniumReportUtil.MARKER_PASS, "not visible: " + selector.elementName());
  }

  protected void assertElementVisible(String message, Selector selector) {
    assertNotNull(InteractionUtil.getElementVisible(selector, 10), message);
    getLogger().debug(GaleniumReportUtil.MARKER_PASS, "visible: " + selector.elementName());
  }

  protected void checkLayout(String testName, String specPath) {
    getLogger().debug("checking layout: " + specPath);
    LayoutReport layoutReport = GalenLayoutChecker.checkLayout(testName, specPath, getDevice(), getValidationListener());
    handleLayoutReport(specPath, layoutReport);
  }

  protected void click(Selector selector) {
    getElementOrFail(selector).click();
    getLogger().debug(GaleniumReportUtil.MARKER_PASS, "clicked '" + selector.elementName() + "'");
  }

  protected void clickByPartialText(Selector selector, String searchStr) {
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

  protected WebElement findByPartialText(Selector selector, String searchStr) {
    return InteractionUtil.findByPartialText(selector, searchStr);
  }

  protected List<WebElement> findElements(Selector selector) {
    return InteractionUtil.findElements(selector);
  }

  protected WebElement getElementOrFail(Selector selector) {
    WebElement element = getElementVisible(selector, 30);
    if (element == null) {
      getLogger().debug(GaleniumReportUtil.MARKER_FAIL, "could not find '" + selector.elementName() + "'");
    }
    assertNotNull(element, "Visibility: '" + selector.elementName() + "'");
    return element;
  }

  protected WebElement getElementVisible(Selector selector) {
    return InteractionUtil.getElementVisible(selector, 10);
  }

  protected WebElement getElementVisible(Selector selector, int howLong) {
    WebElement elementVisible = InteractionUtil.getElementVisible(selector, howLong);
    if (elementVisible != null) {
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, "found '" + selector.elementName() + "'");
    }
    return elementVisible;
  }

  protected Long getScrollYPosition() {
    return InteractionUtil.getScrollYPosition();
  }

  protected List<String> getTags() {
    return getDevice().getTags();
  }

  protected ValidationListener getValidationListener() {
    return new ImageComparisonValidationListener();
  }

  protected void handleLayoutReport(String specName, LayoutReport layoutReport) {
    String errorMessage = "FAILED: Layoutcheck " + specName + " with device " + getDevice();
    String successMessage = "successfully ran spec: " + specName;
    try {
      GalenLayoutChecker.handleLayoutReport(layoutReport, errorMessage, successMessage);
    }
    catch (Throwable ex) {
      fail(errorMessage, ex);
    }
  }

  protected boolean isCurrentUrl(String url) {
    return StringUtils.equals(url, getDriver().getCurrentUrl());
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

  protected void loadUrlExactly(String url) {
    loadUrl(url);
    assertEquals(url, getDriver().getCurrentUrl(), "Current URL should match.");
  }

  protected void mouseOver(Selector selector) {
    InteractionUtil.mouseOver(selector);
  }

}
