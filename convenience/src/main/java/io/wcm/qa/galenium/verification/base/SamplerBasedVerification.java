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

import io.wcm.qa.galenium.sampling.Sampler;

public abstract class SamplerBasedVerification<S extends Sampler<T>, T> extends VerificationBase<T> {

  private Sampler<T> sampler;

  protected SamplerBasedVerification(String verificationName, Sampler<T> sampler) {
    super(verificationName);
    this.setSampler(sampler);
  }

  public Sampler<T> getSampler() {
    return sampler;
  }

  public void setSampler(Sampler<T> sampler) {
    this.sampler = sampler;
  }

  @Override
  protected T sampleValue() {
    return getSampler().sampleValue();
  }

}
