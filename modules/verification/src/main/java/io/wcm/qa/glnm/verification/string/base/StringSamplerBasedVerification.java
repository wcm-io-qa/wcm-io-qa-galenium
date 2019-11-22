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
package io.wcm.qa.glnm.verification.string.base;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.base.SamplerBasedVerification;
import io.wcm.qa.glnm.verification.persistence.Persistence;

/**
 * Most sampling is String based.
 *
 * @since 1.0.0
 */
public abstract class StringSamplerBasedVerification extends SamplerBasedVerification<Sampler<String>, String> {

  protected StringSamplerBasedVerification(Sampler<String> sampler) {
    super(sampler);
    setPersistence(Persistence.forString(getClass()));
  }

  @Override
  protected boolean doVerification() {
    return StringUtils.equals(getExpectedValue(), getActualValue());
  }

}
