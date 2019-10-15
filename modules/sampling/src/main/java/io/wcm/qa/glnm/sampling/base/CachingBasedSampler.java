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
package io.wcm.qa.glnm.sampling.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.CachingSampler;

/**
 * Abstract base class for caching samplers.
 *
 * @param <T> type of sample returned by sampler
 * @since 1.0.0
 */
public abstract class CachingBasedSampler<T> implements CachingSampler<T> {

  private static final Logger LOG = LoggerFactory.getLogger(CachingBasedSampler.class);

  private T cachedValue;
  private boolean caching;

  /** {@inheritDoc} */
  @Override
  public boolean isCaching() {
    return caching;
  }

  /** {@inheritDoc} */
  @Override
  public T sampleValue() {
    if (isCaching() && getCachedValue() != null) {
      return getCachedValue();
    }
    invalidateCache();
    try {
      T freshSample = freshSample();
      if (freshSample == null) {
        return handleNullSampling();
      }
      setCachedValue(freshSample);
      return freshSample;
    }
    catch (GaleniumException ex) {
      return handleSamplingException(ex);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setCaching(boolean activateCache) {
    this.caching = activateCache;
  }

  protected abstract T freshSample();

  protected T getCachedValue() {
    return cachedValue;
  }

  protected T getNullValue() {
    return null;
  }

  protected T handleNullSampling() {
    LOG.info("when sampling (" + getClass() + "): value was null");
    T nullValue = getNullValue();
    setCachedValue(nullValue);
    return nullValue;
  }

  protected T handleSamplingException(GaleniumException ex) {
    LOG.info("when sampling (" + getClass() + ")", ex);
    return getNullValue();
  }

  protected void invalidateCache() {
    LOG.debug("invalidating cache: " + getClass().getSimpleName());
    setCachedValue(null);
  }

  protected void setCachedValue(T cachedValue) {
    this.cachedValue = cachedValue;
  }

}
