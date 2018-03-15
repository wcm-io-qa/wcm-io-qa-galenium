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
package io.wcm.qa.galenium.cookies;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

/**
 * Combines multiple {@link CookieFetcher} into one profile that can be easily selected on a per test case basis.
 */
public class CookieProfile {

  private Collection<CookieFetcher> cookieFetchers = new ArrayList<CookieFetcher>();
  private Collection<Cookie> fetchedCookies = new ArrayList<>();
  private boolean initialized;
  private String profileName;

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
   * @param fetcherToAdd
   * @return whether fetcher collection changed as a result of the call
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(CookieFetcher fetcherToAdd) {
    if (cookieFetchers.add(fetcherToAdd)) {
      setInitialized(false);
      return true;
    }
    return false;
  }

  /**
   * @param collectionOfFetchersToAdd
   * @return whether fetcher collection changed as a result of the call
   * @see java.util.Collection#addAll(java.util.Collection)
   */
  public boolean addAll(Collection<? extends CookieFetcher> collectionOfFetchersToAdd) {
    return cookieFetchers.addAll(collectionOfFetchersToAdd);
  }

  /**
   * @see java.util.Collection#clear()
   */
  public void clear() {
    cookieFetchers.clear();
  }

  /**
   * @param fetcher
   * @return whether this profile contains the specified fetcher
   * @see java.util.Collection#contains(java.lang.Object)
   */
  public boolean contains(Object fetcher) {
    return cookieFetchers.contains(fetcher);
  }

  /**
   * @return fetched cookies
   */
  public Collection<Cookie> getFetchedCookies() {
    if (!isInitialized()) {
      getLogger().warn("getting cookies before they are fetched.");
    }
    return fetchedCookies;
  }

  public String getProfileName() {
    return profileName;
  }

  /**
   * Fetches the cookies.
   * @param driver to use for cookie fetching
   */
  public void initialize(WebDriver driver) {
    if (!isInitialized()) {
      clearFetchedCookies();
      fetchCookies(driver);
      setInitialized(true);
    }
  }

  /**
   * @param fetcherToRemove
   * @return whether an element was removed as a result of this call
   * @see java.util.Collection#remove(java.lang.Object)
   */
  public boolean remove(Object fetcherToRemove) {
    return cookieFetchers.remove(fetcherToRemove);
  }

  private void fetchCookies(WebDriver driver) {
    for (CookieFetcher cookieFetcher : cookieFetchers) {
      getLogger().debug("fetching cookies for profile '" + getProfileName() + "': " + cookieFetcher.getFetcherName());
      if (cookieFetcher.fetchCookies()) {
        Set<Cookie> cookies = driver.manage().getCookies();
        Collection<String> cookieNames = cookieFetcher.getCookieNames();
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

  public boolean isInitialized() {
    return initialized;
  }

  private void setInitialized(boolean value) {
    initialized = value;
  }

  private void setProfileName(String profileName) {
    this.profileName = profileName;
  }

  protected boolean addCookie(Cookie cookie) {
    return fetchedCookies.add(cookie);
  }

  protected void clearFetchedCookies() {
    fetchedCookies.clear();
  }

}
