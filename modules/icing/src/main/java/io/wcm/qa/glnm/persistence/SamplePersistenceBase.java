/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.persistence;

import java.util.NoSuchElementException;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;

abstract class SamplePersistenceBase<T> implements SamplePersistence<T> {

  private static final Logger LOG = LoggerFactory.getLogger(SamplePersistenceBase.class);

  private Class resourceClass;

  /** {@inheritDoc} */
  @Override
  public T loadFromBaseline(Differences key) {
    try {
      if (LOG.isTraceEnabled()) {
        LOG.trace("loading for " + resourceClass + " with key " + key.getKey());
      }
      return fetchBaseline(keyWithContextDifferences(key));
    }
    catch (NoSuchElementException ex) {
      return handleNotFound();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void storeToBaseline(Differences key, T sample) {
    samples().setProperty(keyWithContextDifferences(key), sample);
  }

  private Class initResourceClass() {
    Class contextResourceClass = BaselinePersistenceExtension.getContextResourceClass();
    if (contextResourceClass != null) {
      return contextResourceClass;
    }
    return getClass();
  }

  protected PropertiesConfiguration baseline() {
    return PersistingCacheUtil.cachedBaselineForProperties(getResourceClass());
  }

  protected Class getResourceClass() {
    if (resourceClass == null) {
      resourceClass = initResourceClass();
    }
    return resourceClass;
  }

  protected T handleNotFound() {
    return null;
  }

  protected String keyWithContextDifferences(Differences key) {
    MutableDifferences differences = new MutableDifferences();
    Differences contextDifferences = BaselinePersistenceExtension.getContextDifferences();
    if (contextDifferences != null) {
      differences.addAll(contextDifferences);
    }
    differences.addAll(key);
    return differences.getKey();
  }

  protected abstract T fetchBaseline(String keyWithContextDifferences);

  protected PropertiesConfiguration samples() {
    return PersistingCacheUtil.cachedSamplesForProperties(getResourceClass());
  }

  protected void setResourceClass(Class resourceClass) {
    this.resourceClass = resourceClass;
  }

}
