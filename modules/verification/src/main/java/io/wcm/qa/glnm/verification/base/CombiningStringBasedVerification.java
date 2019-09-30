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
package io.wcm.qa.glnm.verification.base;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.string.StringVerification;

/**
 * Combines multiple verifications based on the same sampled string into a single verification.
 */
public abstract class CombiningStringBasedVerification extends StringVerification {

  private CombinedVerification checks = new CombinedVerification();

  /**
   * @param verificationName name for verification
   * @param sampler to use for sampling
   */
  public CombiningStringBasedVerification(String verificationName, Sampler<String> sampler) {
    super(verificationName, sampler);
  }

  private boolean verifyChecks() {
    return checks.verify();
  }

  protected void addCheck(Verification verification) {
    checks.addVerification(verification);
  }

  @Override
  protected void afterVerification() {
    // do nothing
  }

  @Override
  protected boolean doVerification() {
    populateChecks(getActualValue());
    return verifyChecks();
  }

  @Override
  protected String getFailureMessage() {
    return checks.getMessage();
  }

  @Override
  protected String getSuccessMessage() {
    String message = checks.getMessage();
    if (StringUtils.isBlank(message)) {
      return getSuccessMessageForEmptyCheckMessages();
    }
    return message;
  }

  protected abstract String getSuccessMessageForEmptyCheckMessages();

  @Override
  protected String initExpectedValue() {
    return "NO_EXPECTATIONS_TOWARDS_WHOLE_STRING";
  }

  protected abstract void populateChecks(String sample);

}
