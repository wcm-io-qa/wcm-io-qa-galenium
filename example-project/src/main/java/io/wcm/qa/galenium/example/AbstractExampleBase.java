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

import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;
import static io.wcm.qa.galenium.util.InteractionUtil.click;
import static io.wcm.qa.galenium.util.InteractionUtil.clickByPartialText;
import static io.wcm.qa.galenium.util.InteractionUtil.getElementOrFail;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFactory;
import io.wcm.qa.galenium.testcase.AbstractGaleniumBase;
import io.wcm.qa.galenium.util.AemUtil;
import io.wcm.qa.galenium.util.TestDevice;

/**
 * Abstract base class for common functionality needed by multiple tests.
 */
public abstract class AbstractExampleBase extends AbstractGaleniumBase {

  private static final int CUTOFF_MOBILE_WIDTH = 601;
  private static final Selector SELECTOR_NAV = SelectorFactory.fromCss("nav");
  private static final Selector SELECTOR_NAV_LINK = SelectorFactory.fromCss("a.navlink-main");
  private static final Selector SELECTOR_NAV_MENU_OPENER = SelectorFactory.fromCss("a.menu-opener");
  protected static final String PATH_TO_CONFERENCE_PAGE = "/en/conference.html";
  protected static final String PATH_TO_HOMEPAGE = "/en.html";

  /**
   * @param testDevice test device to use for test
   */
  public AbstractExampleBase(TestDevice testDevice) {
    super(testDevice);
  }

  @Override
  public String getTestName() {
    return "Example." + super.getTestName();
  }

  private void navShouldBeVisible() {
    getElementOrFail(SELECTOR_NAV);
  }

  protected void assertRelativePath(String relativePath) {
    String currentUrl = getDriver().getCurrentUrl();
    assertEquals(currentUrl, getBaseUrl() + relativePath, "relative path should be: '" + relativePath + "'");
  }

  protected void clickConferenceNavLink() {
    clickByPartialText(SELECTOR_NAV_LINK, "conference");
  }

  protected abstract String getRelativePath();

  protected String getStartUrl() {
    return getBaseUrl() + getRelativePath();
  }

  protected boolean isMobile() {
    return getDevice().getScreenSize().getWidth() < CUTOFF_MOBILE_WIDTH;
  }

  protected void loadStartUrl() {
    if (AemUtil.loginToAuthor(getStartUrl())) {
      getLogger().debug("loaded start URL: " + getStartUrl());
      return;
    }
    throw new GaleniumException("could not login to author when loading start URL.");
  }

  protected void openNav() {
    navShouldBeVisible();
    if (isMobile()) {
      click(SELECTOR_NAV_MENU_OPENER);
      getElementOrFail(SELECTOR_NAV_LINK);
    }
  }

}
