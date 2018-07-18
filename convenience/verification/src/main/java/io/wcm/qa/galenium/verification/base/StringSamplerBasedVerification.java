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

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.sampling.StringSampler;
import io.wcm.qa.galenium.verification.util.TextSampleManager;

public abstract class StringSamplerBasedVerification extends SamplerBasedVerification<StringSampler, String> {

  private static final String NO_EXPECTED_VALUE_SET = "NO_EXPECTED_VALUE_SET";

  protected StringSamplerBasedVerification(String verificationName, StringSampler sampler) {
    super(verificationName, sampler);
  }

  @Override
  protected Boolean doVerification() {
    return StringUtils.equals(getExpectedValue(), getActualValue());
  }

  @Override
  protected String initExpectedValue() {
    String expectedKey = getExpectedKey();
    if (StringUtils.isNotBlank(expectedKey)) {
      return TextSampleManager.getExpectedText(expectedKey);
    }
    return NO_EXPECTED_VALUE_SET;
  }

  @Override
  protected void persistSample(String key, String newValue) {
    TextSampleManager.addNewTextSample(key, newValue);
  }

}
