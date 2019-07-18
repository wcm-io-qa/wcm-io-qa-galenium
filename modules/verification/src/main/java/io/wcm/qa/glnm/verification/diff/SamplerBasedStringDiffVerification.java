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
package io.wcm.qa.glnm.verification.diff;

import java.util.List;

import io.wcm.qa.glnm.persistence.util.TextSampleManager;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.diff.base.SamplerBasedDiffVerification;

/**
 * Diff based verification for String based samplers.
 * @param <S> sampler to use
 */
public abstract class SamplerBasedStringDiffVerification<S extends Sampler<List<String>>> extends SamplerBasedDiffVerification<S, String, List<String>> {

  protected SamplerBasedStringDiffVerification(String verificationName, S sampler) {
    super(verificationName, sampler);
  }

  @Override
  protected List<String> initExpectedValue() {
    return TextSampleManager.getExpectedLines(getExpectedKey());
  }

  @Override
  protected void persistSample(String key, List<String> newValue) {
    TextSampleManager.addNewMultiLineSample(key, newValue);
  }
}
