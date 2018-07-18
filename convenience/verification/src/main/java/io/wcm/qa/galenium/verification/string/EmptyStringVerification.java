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
package io.wcm.qa.galenium.verification.string;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.sampling.StringSampler;
import io.wcm.qa.galenium.sampling.string.FixedStringSampler;

public class EmptyStringVerification extends StringVerification {

  public EmptyStringVerification(String verificationName, String sample) {
    this(verificationName, new FixedStringSampler(sample));
  }

  public EmptyStringVerification(String verificationName, StringSampler sampler) {
    super(verificationName, sampler);
  }

  @Override
  protected void afterVerification() {
    String cachedValue = getCachedValue();
    getLogger().trace("found: '" + cachedValue + "'");
    getLogger().trace("done verifying (" + toString() + ")");
  }

  @Override
  protected Boolean doVerification() {
    return StringUtils.isBlank(getActualValue());
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") String is not empty: '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") String is empty.";
  }
}
