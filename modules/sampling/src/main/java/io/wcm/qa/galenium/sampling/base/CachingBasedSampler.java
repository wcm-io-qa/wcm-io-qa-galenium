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
package io.wcm.qa.galenium.sampling.base;

import org.slf4j.Logger;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.CachingSampler;

/**
 * Abstract base class for caching samplers.
 * @param <T>
 */
public abstract class CachingBasedSampler<T> implements CachingSampler<T> {

  private T cachedValue;
  private boolean caching;

  @Override
  public boolean isCaching() {
    return caching;
  }

  @Override
  public void setCaching(boolean activateCache) {
    this.caching = activateCache;
  }

  protected T getCachedValue() {
    return cachedValue;
  }

  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  protected void invalidateCache() {
    getLogger().debug("invalidating cache: " + getClass().getSimpleName());
    setCachedValue(null);
  }

  protected void setCachedValue(T cachedValue) {
    this.cachedValue = cachedValue;
  }

}
