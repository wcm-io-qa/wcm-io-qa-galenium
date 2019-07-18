/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.glnm.verification.driver;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.driver.CurrentUrlSampler;
import io.wcm.qa.glnm.verification.string.base.StringSamplerBasedVerification;

/**
 * Make sure current URL is a certain value.
 */
public class CurrentUrlVerification extends StringSamplerBasedVerification {

  private static final String KEY_PART_URL = "url";

  /**
   * @param verificationName for use in reporting
   */
  public CurrentUrlVerification(String verificationName) {
    super(verificationName, new CurrentUrlSampler());
  }

  /**
   * @param verificationName for use in reporting
   * @param expectedUrl to match against
   */
  public CurrentUrlVerification(String verificationName, String expectedUrl) {
    this(verificationName);
    setExpectedValue(expectedUrl);
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getExpectedValue();
  }

  @Override
  protected String getExpectedKey() {
    if (StringUtils.isNotBlank(super.getExpectedKey())) {
      return super.getExpectedKey() + "." + KEY_PART_URL;
    }
    return KEY_PART_URL;
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") Expected URL: '" + getExpectedValue() + "' but found '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") Found URL: '" + getExpectedValue() + "'";
  }

}
