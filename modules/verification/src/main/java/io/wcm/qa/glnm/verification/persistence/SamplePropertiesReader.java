package io.wcm.qa.glnm.verification.persistence;
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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Differences;

/**
 * <p>SamplePropertiesReader class.</p>
 *
 * @since 4.0.0
 */
public class SamplePropertiesReader extends SamplesForClass implements SampleReader<String> {

  private static final Logger LOG = LoggerFactory.getLogger(SamplePropertiesReader.class);
  private Properties properties;

  protected SamplePropertiesReader(Class clazz) {
    super(clazz);
  }

  /** {@inheritDoc} */
  @Override
  public String readSample(Differences differences) {
    String key = differences.asPropertyKey();

    if (LOG.isTraceEnabled()) {
      LOG.trace("fetching sample for: " + getSamplingClass() + "#" + key);
    }
    if (LOG.isDebugEnabled()) {
      if (getProperties().containsKey(key)) {
        LOG.debug("did not find sample for: " + getSamplingClass() + "#" + key);
      }
    }
    return getProperty(key);
  }

  private String getProperty(String key) {
    return getProperties().getProperty(key);
  }

  private Properties getProperties() {
    if (properties == null) {
      properties = PersistenceUtil.getPropertiesFor(samplingClass);
    }
    return properties;
  }

}
