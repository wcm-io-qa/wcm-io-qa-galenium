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
package io.wcm.qa.galenium.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.AbstractGaleniumInteractiveBaseTestCase;
import io.wcm.qa.galenium.WebDriverManager;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFactory;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * Abstract base class for common functionality needed by multiple tests.
 */
public abstract class AbstractExampleBase extends AbstractGaleniumInteractiveBaseTestCase {

  private static final Selector DIV_LOGIN_BOX = SelectorFactory.fromCss("div#login-box");
  private static final Selector SELECTOR_AUTHOR_INPUT_PASSWORD = SelectorFactory.fromCss("#password");
  private static final Selector SELECTOR_AUTHOR_INPUT_USERNAME = SelectorFactory.fromCss("#username");
  private static final Selector SELECTOR_AUTHOR_LOGIN_BUTTON = SelectorFactory.fromCss("#submit-button");
  protected static final String PATH_TO_HOMEPAGE = "/en.html";
  private static final String LOGIN_AUTHOR_NAME = "admin";
  private static final String LOGIN_AUTHOR_PASS = "admin";
  private static final int CUTOFF_MOBILE_WIDTH = 601;
  protected static final String PATH_TO_CONFERENCE_PAGE = "/en/conference.html";
  private static final Selector SELECTOR_NAV = SelectorFactory.fromCss("nav");
  private static final Selector SELECTOR_NAV_LINK = SelectorFactory.fromCss("a.navlink-main");
  private static final Selector SELECTOR_NAV_MENU_OPENER = SelectorFactory.fromCss("a.menu-opener");

  /**
   * @param testDevice test device to use for test
   */
  public AbstractExampleBase(TestDevice testDevice) {
    super(testDevice);
  }

  @Override
  protected WebDriver getDriver() {
    return WebDriverManager.getDriver(getDevice());
  }

  protected boolean isMobile() {
    return getDevice().getScreenSize().getWidth() < CUTOFF_MOBILE_WIDTH;
  }

  protected void loadStartUrl() {
    loadUrl(getBaseUrl() + getRelativePath());
    loginToAuthor();
  }

  protected abstract String getRelativePath();

  protected void loginToAuthor() {
    if (isAuthorLogin()) {
      getLogger().info("Logging in to author instance");
      enterText(SELECTOR_AUTHOR_INPUT_USERNAME, LOGIN_AUTHOR_NAME);
      enterText(SELECTOR_AUTHOR_INPUT_PASSWORD, LOGIN_AUTHOR_PASS);
      click(SELECTOR_AUTHOR_LOGIN_BUTTON);
    }
  }

  private boolean isAuthorLogin() {
    return getElementVisible(DIV_LOGIN_BOX) != null;
  }

  private void enterText(Selector selector, String text) {
    WebElement input = getElementOrFail(selector);
    input.sendKeys(text);
  }

  protected void assertRelativePath(String relativePath) {
    String currentUrl = getDriver().getCurrentUrl();
    assertEquals(currentUrl, getBaseUrl() + relativePath, "relative path should be: '" + relativePath + "'");
  }

  protected void openNav() {
    navShouldBeVisible();
    if (isMobile()) {
      click(SELECTOR_NAV_MENU_OPENER);
      getElementOrFail(SELECTOR_NAV_LINK);
    }
  }

  protected void clickConferenceNavLink() {
    clickByPartialText("conference", SELECTOR_NAV_LINK);
  }

  private void navShouldBeVisible() {
    getElementOrFail(SELECTOR_NAV);
  }

  @Override
  public String getTestName() {
    return "Example." + super.getTestName();
  }

}
