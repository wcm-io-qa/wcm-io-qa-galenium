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
package io.wcm.qa.glnm.verification.element;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.element.TextSampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.element.base.WebElementBasedStringVerification;

/**
 * Verifies text of element.
 *
 * @since 1.0.0
 */
public class TextVerification extends WebElementBasedStringVerification<TextSampler> {

  /**
   * <p>Constructor for TextVerification.</p>
   *
   * @param selector to identify element
   * @since 2.0.0
   */
  public TextVerification(Selector selector) {
    super(new TextSampler(selector));
    setPreVerification(new VisibilityVerification(selector));
    addDifference("text");
  }

  /**
   * <p>Constructor for TextVerification.</p>
   *
   * @param selector to identify element
   * @param expectedValue to verify against
   * @since 2.0.0
   */
  public TextVerification(Selector selector, String expectedValue) {
    this(selector);
    setExpectedValue(expectedValue);
  }

  @Override
  protected boolean doVerification() {
    return StringUtils.equals(getExpectedValue(), getActualValue());
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + " should have text '" + getExpectedValue() + "' but found '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + " has text '" + getExpectedValue() + "'";
  }

}
