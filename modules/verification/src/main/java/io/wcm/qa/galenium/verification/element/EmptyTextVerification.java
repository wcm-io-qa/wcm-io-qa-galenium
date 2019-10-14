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
package io.wcm.qa.galenium.verification.element;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Verifies that element has no text.
 *
 * @since 1.0.0
 */
public class EmptyTextVerification extends TextVerification {

  /**
   * <p>Constructor for EmptyTextVerification.</p>
   *
   * @param selector identifies element
   * @since 2.0.0
   */
  public EmptyTextVerification(Selector selector) {
    super(selector);
  }

  @Override
  protected void afterVerification() {
    getLogger().trace("looking for empty text");
    getLogger().trace("found: '" + getCachedValue() + "'");
    getLogger().trace("done verifying (" + toString() + ")");
  }

  @Override
  protected boolean doVerification() {
    return StringUtils.isBlank(getActualValue());
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") Text is not empty: '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") Text is empty.";
  }
}
