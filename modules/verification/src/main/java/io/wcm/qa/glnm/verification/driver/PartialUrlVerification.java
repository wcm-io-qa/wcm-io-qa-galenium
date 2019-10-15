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

/**
 * Make sure current URL contains a certain value.
 *
 * @since 1.0.0
 */
public class PartialUrlVerification extends CurrentUrlVerification {


  /**
   * <p>Constructor for PartialUrlVerification.</p>
   *
   * @param verificationName for use in reporting
   * @param expectedUrlPart to look for in URL
   * @since 3.0.0
   */
  public PartialUrlVerification(String verificationName, String expectedUrlPart) {
    super(verificationName);
    setExpectedValue(expectedUrlPart);
  }

  @Override
  protected boolean doVerification() {
    return StringUtils.contains(getActualValue(), getExpectedValue());
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getExpectedValue();
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") Expected URL to contain: '" + getExpectedValue() + "' but found '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") Found pattern in URL: '" + getExpectedValue() + "' in '" + getCachedValue() + "'";
  }

}
