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
package io.wcm.qa.glnm.interaction;

import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getAuthorPass;
import static io.wcm.qa.glnm.configuration.GaleniumConfiguration.getAuthorUser;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_PASS;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.glnm.util.GaleniumContext.getDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;
import io.wcm.qa.glnm.util.HttpUtil;

/**
 * AEM specific utility methods.
 */
public final class Aem {

  private static final String RELATIVE_PATH_LOGIN_FORM_POST = "/libs/granite/core/content/login.html/j_security_check";
  private static final String PARAM_VALUE_CHARSET = "utf-8";
  private static final String PARAM_VALUE_VALIDATE = "true";
  private static final String PARAM_NAME_CHARSET = "_charset_";
  private static final String PARAM_NAME_VALIDATE = "j_validate";
  private static final String PARAM_NAME_PASSWORD = "j_password";
  private static final String PARAM_NAME_USERNAME = "j_username";
  private static final Selector DIV_LOGIN_BOX = SelectorFactory.fromCss("div#login-box");
  private static final Selector SELECTOR_AUTHOR_INPUT_PASSWORD = SelectorFactory.fromCss("#password");
  private static final Selector SELECTOR_AUTHOR_INPUT_USERNAME = SelectorFactory.fromCss("#username");
  private static final Selector SELECTOR_AUTHOR_LOGIN_BUTTON = SelectorFactory.fromCss("#submit-button");

  private Aem() {
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
    return loginToAuthorViaBrowser(getAuthorUser(), getAuthorPass());
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
        getLogger().warn("author login not successful, when waiting for '" + finalUrl + "' got '" + actualResult + "'");
      }
    }
    else {
      getLogger().debug("skipping author login, because not on login page.");
    }

    return false;
  }

  /**
   * Posting to login URL and check for HTTP status 200.
   * @param authorBaseUrl
   * @param authorUser
   * @param authorPass
   * @return whether POST response had status code 200
   */
  public static boolean loginToAuthorViaHttp(String authorBaseUrl, String authorUser, String authorPass) {
    URL url;
    try {
      url = new URL(authorBaseUrl + RELATIVE_PATH_LOGIN_FORM_POST);
    }
    catch (MalformedURLException ex) {
      throw new GaleniumException("could not parse author base URL");
    }

    Map<String, String> paramMap = getCredentialParamsForPost(authorUser, authorPass);
    HttpResponse response = HttpUtil.postForm(url, paramMap);
    logResponse(response);
    if (response.getStatusLine().getStatusCode() == 200) {
      return true;
    }

    return false;
  }

  private static void logResponse(HttpResponse response) {
    Header[] allHeaders = response.getAllHeaders();
    getLogger().debug("status: " + response.getStatusLine().getStatusCode() + "(" + response.getStatusLine().getReasonPhrase() + ")");
    for (Header header : allHeaders) {
      getLogger().debug("'" + header.getName() + "': '" + header.getValue() + "'");
    }
  }

  private static Map<String, String> getCredentialParamsForPost(String authorUser, String authorPass) {
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put(PARAM_NAME_USERNAME, authorUser);
    paramMap.put(PARAM_NAME_PASSWORD, authorPass);
    paramMap.put(PARAM_NAME_VALIDATE, PARAM_VALUE_VALIDATE);
    paramMap.put(PARAM_NAME_CHARSET, PARAM_VALUE_CHARSET);
    return paramMap;
  }

  private static boolean loginToAuthorViaBrowser(String authorUser, String authorPass) {
    if (isAuthorLogin()) {
      getLogger().debug("Attempting login in to author instance");
      FormElement.clearAndEnterText(SELECTOR_AUTHOR_INPUT_USERNAME, authorUser);
      FormElement.clearAndEnterText(SELECTOR_AUTHOR_INPUT_PASSWORD, authorPass);
      Element.click(SELECTOR_AUTHOR_LOGIN_BUTTON);
      getLogger().info(MARKER_PASS, "Logging in to author instance.");
      return true;
    }
    getLogger().debug("Not logging in to author instance.");
    return false;
  }

}
