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

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.wcm.qa.glnm.exceptions.GaleniumException;

final class PersistingCacheUtil {

  private static final LoadingCache<Class, PropertiesConfiguration> CACHES_BASELINE_PER_CLASS = cacheWith(new BaselineCacheLoader());

  private static final LoadingCache<Class, PropertiesConfiguration> CACHES_SAMPLES_PER_CLASS = cacheWith(new SampleCacheLoader());
  private static final Logger LOG = LoggerFactory.getLogger(PersistingCacheUtil.class);

  private PersistingCacheUtil() {
    // do not instantiate
  }

  private static LoadingCache<Class, PropertiesConfiguration> cacheWith(CacheLoader<Class, PropertiesConfiguration> cacheLoader) {
    return CacheBuilder.newBuilder().build(cacheLoader);
  }

  static PropertiesConfiguration cachedBaselineForProperties(Class clazz) {
    try {
      return CACHES_BASELINE_PER_CLASS.get(clazz);
    }
    catch (ExecutionException ex) {
      throw new GaleniumException("when retrieving baseline for " + clazz, ex);
    }
  }

  static PropertiesConfiguration cachedSamplesForProperties(Class clazz) {
    try {
      return CACHES_SAMPLES_PER_CLASS.get(clazz);
    }
    catch (ExecutionException ex) {
      throw new GaleniumException("When retrieving samples storage for " + clazz, ex);
    }
  }

  static void persistNewBaseline() {
    Set<Entry<Class, PropertiesConfiguration>> allSamplesForAllClasses = CACHES_SAMPLES_PER_CLASS.asMap().entrySet();
    if (allSamplesForAllClasses.isEmpty()) {
      LOG.info("no new baseline to persist");
      return;
    }
    LOG.info("persisting baseline");
    if (LOG.isDebugEnabled()) {
      LOG.debug("persisting baseline for " + allSamplesForAllClasses.size() + " classes");
    }
    for (Entry<Class, PropertiesConfiguration> samplesForClass : allSamplesForAllClasses) {
      PropertiesConfiguration samples = samplesForClass.getValue();
      Class clazz = samplesForClass.getKey();
      if (LOG.isDebugEnabled()) {
        LOG.debug("persisting " + samples.size() + " samples baseline for " + clazz);
      }
      PersistenceUtil.writeSamplesForClass(samples, clazz);
    }
  }

  private static final class BaselineCacheLoader extends CacheLoader<Class, PropertiesConfiguration> {
    @Override
    public PropertiesConfiguration load(Class key) throws Exception {
      if (LOG.isDebugEnabled()) {
        LOG.debug("initializing baseline for " + key);
      }
      return PersistenceUtil.getPropertiesFor(key);
    }
  }

  private static final class SampleCacheLoader extends CacheLoader<Class, PropertiesConfiguration> {
    @Override
    public PropertiesConfiguration load(Class key) throws Exception {
      if (LOG.isDebugEnabled()) {
        LOG.debug("initializing samples for " + key);
      }
      PropertiesConfiguration samples = new PropertiesConfiguration();
      samples.append(cachedBaselineForProperties(key));
      return samples;
    }
  }

}
