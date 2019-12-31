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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;

/**
 * Verifies that string is empty.
 *
 * @since 1.0.0
 */
public class EmptyStringVerification extends StringVerification {

  private static final Logger LOG = LoggerFactory.getLogger(EmptyStringVerification.class);

  /**
   * Verify sampled input is empty.
   *
   * @param verificationName name for this check
   * @param sampler provides input sample
   * @since 2.0.0
   */
  public EmptyStringVerification(String verificationName, Sampler<String> sampler) {
    super(verificationName, sampler);
  }

  /**
   * Verify fixed string is empty.
   *
   * @param verificationName name for this check
   * @param sample fixed input sample
   * @since 2.0.0
   */
  public EmptyStringVerification(String verificationName, String sample) {
    this(verificationName, new FixedStringSampler(sample));
  }

  @Override
  protected void afterVerification() {
    String cachedValue = getCachedValue();
    LOG.trace("found: '" + cachedValue + "'");
    LOG.trace("done verifying (" + toString() + ")");
  }

  @Override
  protected boolean doVerification() {
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
