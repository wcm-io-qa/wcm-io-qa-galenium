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

import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.WebDriver;

/**
 * @param <F> Fetcher type
 */
public abstract class AbstractProfile<F extends Fetcher, I> {

  private boolean initialized;
  private String profileName;
  protected final Collection<F> fetchers = new ArrayList<F>();


  /**
   * @param fetcherToAdd
   * @return whether fetcher collection changed as a result of the call
   * @see java.util.Collection#add(java.lang.Object)
   */
  public boolean add(F fetcherToAdd) {
    if (fetchers.add(fetcherToAdd)) {
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
  public boolean addFetchers(Collection<? extends F> collectionOfFetchersToAdd) {
    return fetchers.addAll(collectionOfFetchersToAdd);
  }

  /**
   * @see java.util.Collection#clear()
   */
  public void clear() {
    fetchers.clear();
  }

  /**
   * @param fetcher
   * @return whether this profile contains the specified fetcher
   * @see java.util.Collection#contains(java.lang.Object)
   */
  public boolean contains(Object fetcher) {
    return fetchers.contains(fetcher);
  }

  /**
   * @return fetched items
   */
  public abstract Collection<I> getFetchedItems();

  /**
   * @return profile name
   */
  public String getProfileName() {
    return profileName;
  }

  /**
   * Fetches the cookies.
   * @param driver to use for cookie fetching
   */
  public void initialize(WebDriver driver) {
    if (!isInitialized()) {
      clearFetchedItems();
      fetchItems(driver);
      setInitialized(true);
    }
  }

  /**
   * @return whether profile is initialized.
   */
  public boolean isInitialized() {
    return initialized;
  }

  /**
   * @param fetcherToRemove
   * @return whether an element was removed as a result of this call
   * @see java.util.Collection#remove(java.lang.Object)
   */
  public boolean remove(Object fetcherToRemove) {
    return fetchers.remove(fetcherToRemove);
  }

  protected abstract void clearFetchedItems();

  protected void fetchItems(WebDriver driver) {
    for (F storageFetcher : getFetchers()) {
      getLogger().debug("fetching items for profile '" + getProfileName() + "': " + storageFetcher.getFetcherName());
      assignCategory(getCategoryPrefixFetcher() + storageFetcher.getFetcherName());
      handleFetcher(driver, storageFetcher);
    }
  }

  protected abstract String getCategoryPrefixFetcher();

  /**
   * @return the cookieFetchers
   */
  protected Collection<F> getFetchers() {
    return fetchers;
  }

  protected abstract void handleFetcher(WebDriver driver, F fetcher);

  protected void setInitialized(boolean value) {
    initialized = value;
  }

  protected void setProfileName(String profileName) {
    this.profileName = profileName;
  }
}
