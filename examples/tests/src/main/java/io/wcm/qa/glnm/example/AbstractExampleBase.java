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
package io.wcm.qa.glnm.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.example.selectors.common.Page;
import io.wcm.qa.glnm.example.selectors.common.Page.Navigation;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.interaction.Wait;
import io.wcm.qa.glnm.interaction.aem.author.AuthorLogin;
import io.wcm.qa.glnm.webdriver.HasDevice;

/**
 * Abstract base class for common functionality needed by multiple tests.
 */
public abstract class AbstractExampleBase implements HasDevice {

  private static final int CUTOFF_MOBILE_WIDTH = 601;
  private static final boolean IS_AUTHOR_SUT = Boolean.getBoolean("galenium.example.sut.author");
  private static final Logger LOG = LoggerFactory.getLogger(AbstractExampleBase.class);
  private static final boolean SKIP_AUTHOR_LOGIN = !IS_AUTHOR_SUT;
  protected static final String PATH_TO_CONFERENCE_PAGE = "/en/conference.html";
  private static final String PATH_TO_HOMEPAGE = "/en.html";

  private void navShouldBeVisible() {
    Element.findOrFail(Page.NAVIGATION);
  }

  protected String getBaseUrl() {
    return GaleniumConfiguration.getBaseUrl();
  }

  protected abstract String getRelativePath();

  protected String getStartUrl() {
    return getBaseUrl() + getRelativePath();
  }

  protected boolean isAuthor() {
    return IS_AUTHOR_SUT;
  }

  protected boolean isMobile() {
    return getDevice().getScreenSize().getWidth() < CUTOFF_MOBILE_WIDTH;
  }

  protected void loadStartUrl() {
    if (SKIP_AUTHOR_LOGIN) {
      Browser.load(getStartUrl());
      LOG.debug("loaded start URL: " + getStartUrl());
      return;
    }
    if (AuthorLogin.loginToAuthor(getStartUrl())) {
      LOG.debug("logged in to start URL: " + getStartUrl());
      return;
    }
    throw new GaleniumException("could not login to author when loading start URL.");
  }

  protected void openNav() {
    navShouldBeVisible();
    if (isMobile()) {
      Navigation.MENU_OPENER.click();
      Wait.forVisibility(Navigation.LINK_TO_HOMEPAGE);
    }
  }

}
