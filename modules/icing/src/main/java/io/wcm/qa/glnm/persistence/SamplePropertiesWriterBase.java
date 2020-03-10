/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Differences;


/**
 * <p>SamplePropertiesWriter class.</p>
 *
 * @since 4.0.0
 */
abstract class SamplePropertiesWriterBase<T> extends SamplesForClass implements SampleWriter<T> {

  private static final Logger LOG = LoggerFactory.getLogger(SamplePropertiesWriterBase.class);

  private final Properties properties = new Properties();

  private boolean propertiesWritten;
  /**
   * <p>Constructor for SamplePropertiesWriter.</p>
   *
   * @param clazz a {@link java.lang.Class} object.
   */
  SamplePropertiesWriterBase(Class clazz) {
    super(clazz);
  }

  /**
   * <p>persistSamples.</p>
   *
   * @since 4.0.0
   */
  protected void persistSamples() {
    PersistenceUtil.writePropertiesFor(getProperties(), getSamplingClass(), true);
  }

  private Properties getProperties() {
    return properties;
  }

  protected void writeSampleAsString(Differences differences, String sample) {
    String key = differences.getKey();
    if (LOG.isTraceEnabled()) {
      LOG.trace("writing sample for: " + getSamplingClass() + "#" + key);
    }
    getProperties().put(key, sample);
    persistSamples();
  }
}
