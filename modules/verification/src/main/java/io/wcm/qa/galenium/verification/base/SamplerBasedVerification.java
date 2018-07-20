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
package io.wcm.qa.galenium.verification.base;

import io.wcm.qa.galenium.sampling.CachingSampler;
import io.wcm.qa.galenium.sampling.Sampler;

public abstract class SamplerBasedVerification<S extends Sampler<T>, T> extends VerificationBase<T> {

  private S sampler;

  protected SamplerBasedVerification(String verificationName, S sampler) {
    super(verificationName);
    this.setSampler(sampler);
  }

  @Override
  public void setCaching(boolean activateCaching) {
    super.setCaching(activateCaching);
    if (getSampler() instanceof CachingSampler) {
      ((CachingSampler)getSampler()).setCaching(activateCaching);
    }
  }

  @Override
  public boolean isCaching() {
    if (getSampler() instanceof CachingSampler) {
      return ((CachingSampler)getSampler()).isCaching();
    }

    // if caching cannot be deactivated, sampler is assumed to be caching
    return true;
  }

  public S getSampler() {
    return sampler;
  }

  public void setSampler(S sampler) {
    this.sampler = sampler;
  }

  @Override
  protected T sampleValue() {
    return getSampler().sampleValue();
  }

}
