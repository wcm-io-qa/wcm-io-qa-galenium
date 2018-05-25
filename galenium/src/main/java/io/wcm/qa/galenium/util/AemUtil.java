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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getAuthorPass;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getAuthorUser;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;
import static io.wcm.qa.galenium.util.InteractionUtil.loadUrl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.interaction.Wait;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFactory;

/**
 * AEM specific utility methods.
 */
public final class AemUtil {

  private static final Selector DIV_LOGIN_BOX = SelectorFactory.fromCss("div#login-box");
  private static final Selector SELECTOR_AUTHOR_INPUT_PASSWORD = SelectorFactory.fromCss("#password");
  private static final Selector SELECTOR_AUTHOR_INPUT_USERNAME = SelectorFactory.fromCss("#username");
  private static final Selector SELECTOR_AUTHOR_LOGIN_BUTTON = SelectorFactory.fromCss("#submit-button");

  private AemUtil() {
    // do not instantiate
  }

  /**
   * @return whether current page is AEM author login page
   */
  public static boolean isAuthorLogin() {
    return Element.isVisible(DIV_LOGIN_BOX);
  }

  /**
   * Login to author if on AEM author login page.
   * @return successful login
   */
  public static boolean loginToAuthor() {
    getLogger().debug("using credentials from configuration.");
    return loginToAuthor(getAuthorUser(), getAuthorPass());
  }

  /**
   * Load URL and login to AEM author if landing on login page.
   * @param targetUrl URL to load
   * @return successful login
   */
  public static boolean loginToAuthor(String targetUrl) {
    getLogger().debug("using credentials from configuration.");
    return loginToAuthor(targetUrl, getAuthorUser(), getAuthorPass());
  }

  /**
   * Load URL and login to AEM author with credentials if landing on login page.
   * @param targetUrl URL to load
   * @param authorUser user for author instance
   * @param authorPass password for author instance
   * @return whether login was necessary and successful
   */
  public static boolean loginToAuthor(String targetUrl, String authorUser, String authorPass) {
    return loginToAuthor(targetUrl, targetUrl, authorUser, authorPass);
  }

  /**
   * @param initialUrl URL to load
   * @param finalUrl URL to check for
   * @param authorUser user for author instance
   * @param authorPass password for author instance
   * @return whether login was necessary and successful
   */
  public static boolean loginToAuthor(String initialUrl, String finalUrl, String authorUser, String authorPass) {
    loadUrl(initialUrl);
    if (isAuthorLogin()) {
      try {
        loginToAuthor(authorUser, authorPass);
        Wait.forUrl(finalUrl, 5);
        return true;
      }
      catch (WebDriverException ex) {
        WebDriver driver = getDriver();
        String actualResult;
        if (driver == null) {
          actualResult = "driver is null in this context";
        }
        else {
          actualResult = driver.getCurrentUrl();
        }
        getLogger().warn("author login not successful, when waiting for '" + finalUrl + "' got '" + actualResult + "'");
      }
    }
    else {
      getLogger().debug("skipping author login, because not on login page.");
    }

    return false;
  }

  private static boolean loginToAuthor(String authorUser, String authorPass) {
    if (isAuthorLogin()) {
      getLogger().debug("Attempting login in to author instance");
      Element.enterText(SELECTOR_AUTHOR_INPUT_USERNAME, authorUser);
      Element.enterText(SELECTOR_AUTHOR_INPUT_PASSWORD, authorPass);
      Element.click(SELECTOR_AUTHOR_LOGIN_BUTTON);
      getLogger().info(MARKER_PASS, "Logging in to author instance.");
      return true;
    }
    getLogger().debug("Not logging in to author instance.");
    return false;
  }

}
