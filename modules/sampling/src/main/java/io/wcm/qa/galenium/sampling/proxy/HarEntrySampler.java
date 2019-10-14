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
package io.wcm.qa.galenium.sampling.proxy;

import java.util.List;

import org.apache.commons.collections4.ListUtils;

import io.wcm.qa.galenium.sampling.transform.base.TransformationBasedSampler;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

/**
 * Samples {@link net.lightbody.bmp.core.har.HarEntry} instances from a {@link io.wcm.qa.galenium.sampling.proxy.HarSampler}.
 *
 * @since 1.0.0
 */
public class HarEntrySampler extends TransformationBasedSampler<HarSampler, Har, List<HarEntry>> {

  /**
   * Constructor.
   */
  public HarEntrySampler() {
    super(new HarSampler());
  }

  private List<HarEntry> handleEmptySample() {
    return ListUtils.emptyIfNull(null);
  }

  @Override
  protected List<HarEntry> transform(Har inputSample) {
    if (inputSample == null) {
      getLogger().debug("Har was null. (means BMP is not recording");
      return handleEmptySample();
    }
    HarLog log = inputSample.getLog();
    if (log == null) {
      getLogger().debug("Har.log was null.");
      return handleEmptySample();
    }
    return log.getEntries();
  }

}
