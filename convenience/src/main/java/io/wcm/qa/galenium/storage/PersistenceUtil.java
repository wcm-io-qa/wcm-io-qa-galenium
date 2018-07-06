/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.storage;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.assignCategory;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.Collection;
import java.util.Map.Entry;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

import io.wcm.qa.galenium.storage.cookies.CookieProfile;
import io.wcm.qa.galenium.storage.local.LocalStorageProfile;
import io.wcm.qa.galenium.util.GaleniumContext;

/**
 * Handles persisting fetched cookies so they can be used by all tests.
 */
@SuppressWarnings("deprecation")
public final class PersistenceUtil {

  private static final String CATEGORY_PREFIX_COOKIE_PROFILE = "CP_";
  private static final String CATEGORY_PREFIX_LOCAL_STORAGE_PROFILE = "LSP_";

  private PersistenceUtil() {
    // do not instantiate
  }

  /**
   * Sets all fetched cookies from profile in current driver.
   * @param profileToApply profile to apply
   */
  @Deprecated
  public static void applyProfileToDriver(CookieProfile profileToApply) {
    if (profileToApply == null) {
      getLogger().warn("cookie profile is null.");
      return;
    }
    getLogger().debug("applying cookie profile: '" + profileToApply.getProfileName() + "'");
    WebDriver driver = GaleniumContext.getDriver();
    if (driver == null) {
      getLogger().error("driver is null, when trying to apply profile: " + profileToApply.getProfileName());
      return;
    }
    Collection<Cookie> fetchedCookies = profileToApply.getFetchedItems();
    for (Cookie cookie : fetchedCookies) {
      if (getLogger().isDebugEnabled()) {
        StringBuilder addCookieMessage = new StringBuilder();
        addCookieMessage.append("adding cookie to driver: '");
        addCookieMessage.append(cookie.getName());
        addCookieMessage.append("' (domain: '");
        addCookieMessage.append(cookie.getDomain());
        addCookieMessage.append("', path: '");
        addCookieMessage.append(cookie.getPath());
        addCookieMessage.append("')");
        getLogger().debug(addCookieMessage.toString());
      }
      try {
        driver.manage().addCookie(cookie);
      }
      catch (WebDriverException ex) {
        getLogger().warn("could not set cookie ('" + cookie.getName() + "') when applying profile '" + profileToApply.getProfileName() + "'", ex);
      }
    }
    assignCategory(CATEGORY_PREFIX_COOKIE_PROFILE + profileToApply.getProfileName());
  }

  /**
   * Sets all fetched cookies from profile in current driver.
   * @param profileToApply profile to apply
   */
  @Deprecated
  public static void applyProfileToDriver(LocalStorageProfile profileToApply) {
    if (profileToApply == null) {
      getLogger().warn("local storage profile is null.");
      return;
    }
    getLogger().debug("applying local storage profile: '" + profileToApply.getProfileName() + "'");
    WebDriver driver = GaleniumContext.getDriver();
    if (driver == null) {
      getLogger().error("driver is null, when trying to apply profile: " + profileToApply.getProfileName());
      return;
    }
    if (!(driver instanceof WebStorage)) {
      getLogger().error("driver cannot handle local storage, when trying to apply profile: " + profileToApply.getProfileName());
      return;
    }
    LocalStorage localStorage = ((WebStorage)driver).getLocalStorage();
    Collection<Entry<String, String>> fetchedItems = profileToApply.getFetchedItems();
    for (Entry<String, String> item : fetchedItems) {
      String key = item.getKey();
      String value = item.getValue();
      if (getLogger().isDebugEnabled()) {
        StringBuilder addItemMessage = new StringBuilder();
        addItemMessage.append("adding local storage item to driver: '");
        addItemMessage.append(key);
        addItemMessage.append("' : '");
        addItemMessage.append(value);
        addItemMessage.append("'");
        getLogger().debug(addItemMessage.toString());
      }
      try {
        localStorage.setItem(key, value);
      }
      catch (WebDriverException ex) {
        getLogger().warn("could not set local storage item ('" + key + "') when applying profile '" + profileToApply.getProfileName() + "'", ex);
      }
    }
    assignCategory(CATEGORY_PREFIX_LOCAL_STORAGE_PROFILE + profileToApply.getProfileName());
  }

}
