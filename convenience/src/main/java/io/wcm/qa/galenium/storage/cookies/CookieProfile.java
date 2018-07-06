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
package io.wcm.qa.galenium.storage.cookies;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import io.wcm.qa.galenium.storage.AbstractProfile;

/**
 * Combines multiple {@link CookieFetcher} into one profile that can be easily selected on a per test case basis.
 */
@Deprecated
public class CookieProfile extends AbstractProfile<CookieFetcher, Cookie> {

  private static final String CATEGORY_PREFIX_FETCHER = "CF_";

  private Collection<Cookie> fetchedCookies = new ArrayList<>();

  /**
   * Constructor.
   * @param profileName identifies profile, should be unique per test run
   */
  public CookieProfile(String profileName) {
    setProfileName(profileName);
  }

  /**
   * Constructor.
   * @param profileName identifies profile, should be unique per test run
   * @param cookieFetchers fetcher(s) to add right away
   */
  public CookieProfile(String profileName, CookieFetcher... cookieFetchers) {
    this(profileName);
    for (CookieFetcher cookieFetcher : cookieFetchers) {
      add(cookieFetcher);
    }
  }

  /**
   * @return fetched cookies
   */
  @Override
  public Collection<Cookie> getFetchedItems() {
    if (!isInitialized()) {
      getLogger().warn("getting cookies before they are fetched.");
    }
    return fetchedCookies;
  }

  protected boolean addCookie(Cookie cookie) {
    return fetchedCookies.add(cookie);
  }

  @Override
  protected void clearFetchedItems() {
    fetchedCookies.clear();
  }

  @Override
  protected String getCategoryPrefixFetcher() {
    return CATEGORY_PREFIX_FETCHER;
  }

  @Override
  protected void handleFetcher(WebDriver driver, CookieFetcher cookieFetcher) {
    if (cookieFetcher.fetchItems()) {
      Set<Cookie> cookies = driver.manage().getCookies();
      Collection<String> cookieNames = cookieFetcher.getItemNames();
      for (String cookieName : cookieNames) {
        for (Cookie cookie : cookies) {
          if (StringUtils.equals(cookieName, cookie.getName())) {
            getLogger().trace("adding cookie to '" + getProfileName() + "': '" + cookieName + "'");
            addCookie(cookie);
          }
        }
      }
    }
    else {
      getLogger().warn("fetching cookies failed in profile '" + getProfileName() + "': " + cookieFetcher.getFetcherName());
    }
  }

}
