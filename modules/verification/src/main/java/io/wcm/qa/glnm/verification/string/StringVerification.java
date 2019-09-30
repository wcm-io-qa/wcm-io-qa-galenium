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
package io.wcm.qa.glnm.verification.string;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;
import io.wcm.qa.glnm.verification.string.base.StringSamplerBasedVerification;

/**
 * Generic sampler based string equality verification.
 */
public class StringVerification extends StringSamplerBasedVerification {

  /**
   * Verify against sampled input.
   * @param verificationName name for this check
   * @param sampler to provide input
   */
  public StringVerification(String verificationName, Sampler<String> sampler) {
    super(verificationName, sampler);
  }

  /**
   * Verify against fixed sample.
   * @param verificationName name for this check
   * @param sample to verify
   */
  public StringVerification(String verificationName, String sample) {
    this(verificationName, new FixedStringSampler(sample));
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") String does not match: '" + getCachedValue() + "' should be '" + getExpectedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") String matches: '" + getCachedValue() + "'";
  }

}
