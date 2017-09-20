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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getAuthorPass;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.getAuthorUser;
import static io.wcm.qa.galenium.util.InteractionUtil.click;
import static io.wcm.qa.galenium.util.InteractionUtil.enterText;
import static io.wcm.qa.galenium.util.InteractionUtil.loadUrl;
import static io.wcm.qa.galenium.util.InteractionUtil.waitForUrl;

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
    return InteractionUtil.getElementVisible(DIV_LOGIN_BOX) != null;
  }

  /**
   * Login to author if on AEM author login page.
   */
  public static void loginToAuthor() {
    if (isAuthorLogin()) {
      getLogger().info("Logging in to author instance");
      enterText(SELECTOR_AUTHOR_INPUT_USERNAME, getAuthorUser());
      enterText(SELECTOR_AUTHOR_INPUT_PASSWORD, getAuthorPass());
      click(SELECTOR_AUTHOR_LOGIN_BUTTON);
    }
  }

  /**
   * Load URL and login to AEM author if landing on login page.
   */
  public static void loginToAuthor(String targetUrl) {
    loadUrl(targetUrl);
    if (isAuthorLogin()) {
      loginToAuthor();
      waitForUrl(targetUrl);
    }
  }

}
