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

import io.wcm.qa.galenium.sampling.base.CachingBasedSampler;
import io.wcm.qa.galenium.util.BrowserMobUtil;
import net.lightbody.bmp.core.har.Har;

/**
 * Samples Har from BrowserMob Proxy.
 *
 * @since 1.0.0
 */
public class HarSampler extends CachingBasedSampler<Har> {

  /** {@inheritDoc} */
  @Override
  public Har sampleValue() {
    return getHar();
  }

  private Har getHar() {
    return BrowserMobUtil.getBrowserMobProxy().getHar();
  }

}
