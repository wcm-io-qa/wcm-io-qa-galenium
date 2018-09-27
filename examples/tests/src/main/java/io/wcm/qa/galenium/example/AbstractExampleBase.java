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

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.example.selectors.common.Page;
import io.wcm.qa.galenium.example.selectors.common.Page.Navigation;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.interaction.Aem;
import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.interaction.Wait;
import io.wcm.qa.galenium.testcase.AbstractGaleniumBase;

/**
 * Abstract base class for common functionality needed by multiple tests.
 */
public abstract class AbstractExampleBase extends AbstractGaleniumBase {

  private static final String ROOT_PACKAGE_FOR_TESTS = "io.wcm.qa";
  private static final int CUTOFF_MOBILE_WIDTH = 601;
  protected static final String PATH_TO_CONFERENCE_PAGE = "/en/conference.html";
  protected static final String PATH_TO_HOMEPAGE = "/en.html";

  /**
   * @param testDevice test device to use for test
   */
  public AbstractExampleBase(TestDevice testDevice) {
    super(testDevice);
    getNameDifferences().setRootPackage(ROOT_PACKAGE_FOR_TESTS);
  }

  private void navShouldBeVisible() {
    Element.findOrFail(Page.NAVIGATION);
  }

  protected void assertRelativePath(String relativePath) {
    String currentUrl = getDriver().getCurrentUrl();
    assertEquals(currentUrl, getBaseUrl() + relativePath, "relative path should be: '" + relativePath + "'");
  }

  protected void clickConferenceNavLink() {
    Navigation.LINK_TO_CONFERENCE.click();
  }

  protected abstract String getRelativePath();

  protected String getStartUrl() {
    return getBaseUrl() + getRelativePath();
  }

  protected boolean isMobile() {
    return getDevice().getScreenSize().getWidth() < CUTOFF_MOBILE_WIDTH;
  }

  protected void loadStartUrl() {
    if (Aem.loginToAuthor(getStartUrl())) {
      getLogger().debug("loaded start URL: " + getStartUrl());
      return;
    }
    throw new GaleniumException("could not login to author when loading start URL.");
  }

  protected void openNav() {
    navShouldBeVisible();
    if (isMobile()) {
      Navigation.MENU_OPENER.click();
      Wait.forVisibility(Navigation.LINK_TO_CONFERENCE);
    }
  }

}
