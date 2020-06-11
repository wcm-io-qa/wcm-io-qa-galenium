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
package io.wcm.qa.glnm.interaction.aem.author;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getAuthorPass;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getAuthorUser;
import static io.wcm.qa.glnm.context.GaleniumContext.getDriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.interaction.FormElement;
import io.wcm.qa.glnm.interaction.Wait;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;

/**
 * AEM specific utility methods.
 *
 * @since 1.0.0
 */
public final class AuthorLogin {

  private static final Selector DIV_LOGIN_BOX = SelectorFactory.fromCss("div#login-box");

  private static final Logger LOG = LoggerFactory.getLogger(AuthorLogin.class);

  private static final Selector SELECTOR_AUTHOR_INPUT_PASSWORD = SelectorFactory.fromCss("#password");
  private static final Selector SELECTOR_AUTHOR_INPUT_USERNAME = SelectorFactory.fromCss("#username");
  private static final Selector SELECTOR_AUTHOR_LOGIN_BUTTON = SelectorFactory.fromCss("#submit-button");

  private AuthorLogin() {
    // do not instantiate
  }

  /**
   * <p>isAuthorLogin.</p>
   *
   * @return whether current page is AEM author login page
   * @since 5.0.0
   */
  public static boolean isAuthorLogin() {
    return Element.isVisible(DIV_LOGIN_BOX);
  }

  /**
   * Login to author if on AEM author login page.
   *
   * @return successful login
   * @since 5.0.0
   */
  public static boolean loginToAuthor() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("using credentials from configuration.");
    }
    return loginToAuthorViaBrowser(getAuthorUser(), getAuthorPass());
  }

  /**
   * Load URL and login to AEM author if landing on login page.
   *
   * @param targetUrl URL to load
   * @return successful login
   * @since 5.0.0
   */
  public static boolean loginToAuthor(String targetUrl) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("using credentials from configuration.");
    }
    return loginToAuthor(targetUrl, getAuthorUser(), getAuthorPass());
  }

  /**
   * Load URL and login to AEM author with credentials if landing on login page.
   *
   * @param targetUrl URL to load
   * @param authorUser user for author instance
   * @param authorPass password for author instance
   * @return whether login was necessary and successful
   * @since 5.0.0
   */
  public static boolean loginToAuthor(String targetUrl, String authorUser, String authorPass) {
    return loginToAuthor(targetUrl, targetUrl, authorUser, authorPass);
  }

  /**
   * <p>loginToAuthor.</p>
   *
   * @param initialUrl URL to load
   * @param finalUrl URL to check for
   * @param authorUser user for author instance
   * @param authorPass password for author instance
   * @return whether login was necessary and successful
   * @since 5.0.0
   */
  @SuppressWarnings("PMD.UseObjectForClearerAPI")
  public static boolean loginToAuthor(String initialUrl, String finalUrl, String authorUser, String authorPass) {
    Browser.load(initialUrl);
    if (isAuthorLogin()) {
      try {
        loginToAuthorViaBrowser(authorUser, authorPass);
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
        if (LOG.isWarnEnabled()) {
          LOG.warn("author login not successful, when waiting for '" + finalUrl + "' got '" + actualResult + "'");
        }
      }
    }
    else {
      LOG.debug("skipping author login, because not on login page.");
    }

    return false;
  }

  private static boolean loginToAuthorViaBrowser(String authorUser, String authorPass) {
    if (isAuthorLogin()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Attempting login in to author instance");
      }
      FormElement.clearAndEnterText(SELECTOR_AUTHOR_INPUT_USERNAME, authorUser);
      FormElement.clearAndEnterText(SELECTOR_AUTHOR_INPUT_PASSWORD, authorPass);
      Element.click(SELECTOR_AUTHOR_LOGIN_BUTTON);
      if (LOG.isInfoEnabled()) {
        LOG.info("Logging in to author instance.");
      }
      return true;
    }
    LOG.debug("Not logging in to author instance.");
    return false;
  }

}
