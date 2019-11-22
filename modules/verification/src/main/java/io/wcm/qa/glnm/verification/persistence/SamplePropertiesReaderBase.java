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

abstract class SamplePropertiesReaderBase<T> extends SamplesForClass implements SampleReader<T> {

  private static final Logger LOG = LoggerFactory.getLogger(SamplePropertiesReaderBase.class);
  private Properties properties;

  protected SamplePropertiesReaderBase(Class clazz) {
    super(clazz);
  }

  private Properties getProperties() {
    if (properties == null) {
      properties = PersistenceUtil.getPropertiesFor(samplingClass);
    }
    return properties;
  }

  private String getSample(String key) {
    return getProperties().getProperty(key);
  }

  private boolean hasSample(String key) {
    return getProperties().containsKey(key);
  }

  protected String readSampleAsString(Differences differences) {
    String key = differences.asPropertyKey();

    if (LOG.isTraceEnabled()) {
      LOG.trace("fetching sample for: " + getSamplingClass() + "#" + key);
    }
    if (LOG.isDebugEnabled()) {
      if (hasSample(key)) {
        LOG.debug("did not find sample for: " + getSamplingClass() + "#" + key);
      }
    }
    return getSample(key);
  }

}
