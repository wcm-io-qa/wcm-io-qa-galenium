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
package io.wcm.qa.galenium.storage.local;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;

import io.wcm.qa.galenium.storage.AbstractProfile;
import io.wcm.qa.galenium.storage.cookies.CookieFetcher;

/**
 * Combines multiple {@link CookieFetcher} into one profile that can be easily selected on a per test case basis.
 */
public class LocalStorageProfile extends AbstractProfile<LocalStorageFetcher, Map.Entry<String, String>> {

  private static final String CATEGORY_PREFIX_FETCHER = "LSF_";

  private Map<String, String> fetchedItems = new HashMap<>();

  /**
   * Constructor.
   * @param profileName identifies profile, should be unique per test run
   */
  public LocalStorageProfile(String profileName) {
    setProfileName(profileName);
  }

  /**
   * Constructor.
   * @param profileName identifies profile, should be unique per test run
   * @param storageFetchersToUse fetcher(s) to add right away
   */
  public LocalStorageProfile(String profileName, LocalStorageFetcher... storageFetchersToUse) {
    this(profileName);
    for (LocalStorageFetcher cookieFetcher : storageFetchersToUse) {
      add(cookieFetcher);
    }
  }

  protected String addStorageItem(String key, String value) {
    return fetchedItems.put(key, value);
  }

  @Override
  protected void clearFetchedItems() {
    fetchedItems.clear();
  }

  @Override
  protected void fetchItems(WebDriver driver) {
    if (!(driver instanceof WebStorage)) {
      getLogger().warn("driver cannot do web storage: " + driver);
      return;
    }
    super.fetchItems(driver);
  }

  @Override
  protected String getCategoryPrefixFetcher() {
    return CATEGORY_PREFIX_FETCHER;
  }

  @Override
  protected void handleFetcher(WebDriver driver, LocalStorageFetcher storageFetcher) {
    WebStorage storage = (WebStorage)driver;
    if (storageFetcher.fetchItems()) {
      LocalStorage localStorage = storage.getLocalStorage();
      Collection<String> itemNames = storageFetcher.getItemNames();
      for (String itemName : itemNames) {
        String itemValue = localStorage.getItem(itemName);
        if (StringUtils.isNotBlank(itemValue)) {
          getLogger().debug("adding item: " + itemName);
          addStorageItem(itemName, itemValue);
        }
        else {
          getLogger().trace("did not get value for item: " + itemName);
        }
      }
    }
    else {
      getLogger().warn("fetching storage items failed in profile '" + getProfileName() + "': " + storageFetcher.getFetcherName());
    }
  }

  @Override
  public Collection<Entry<String, String>> getFetchedItems() {
    return fetchedItems.entrySet();
  }

}
