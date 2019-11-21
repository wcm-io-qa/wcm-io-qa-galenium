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
package io.wcm.qa.glnm.verification.persistence;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Differences;


/**
 * <p>SamplePropertiesWriter class.</p>
 *
 * @since 4.0.0
 */
public class SamplePropertiesWriter extends SamplesForClass implements SampleWriter<String> {

  private static final Logger LOG = LoggerFactory.getLogger(SamplePropertiesWriter.class);

  /**
   * <p>Constructor for SamplePropertiesWriter.</p>
   *
   * @param clazz a {@link java.lang.Class} object.
   */
  public SamplePropertiesWriter(Class clazz) {
    super(clazz);
  }

  private Properties properties = new Properties();

  /** {@inheritDoc} */
  @Override
  public void writeSample(Differences differences, String sample) {
    String key = differences.asPropertyKey();
    if (LOG.isTraceEnabled()) {
      LOG.trace("writing sample for: " + getSamplingClass() + "#" + key);
    }
    getProperties().put(key, sample);
  }

  private Properties getProperties() {
    return properties;
  }

  /**
   * <p>persistSamples.</p>
   */
  public void persistSamples() {
    PersistenceUtil.writePropertiesFor(getProperties(), getSamplingClass());
  }
}
