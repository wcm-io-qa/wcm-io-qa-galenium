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

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Make sure a certain CSS class is not set on an element.
 */
public class NoCssClassVerification extends CssClassVerification {

  /**
   * Constructor for {@link Selector}.
   * @param selector to identify element
   * @param cssClass to verify against
   */
  public NoCssClassVerification(Selector selector, String cssClass) {
    super(selector, cssClass);
  }

  @Override
  protected boolean doVerification() {
    return !super.doVerification();
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + " should not have CSS class '" + getExpectedValue() + "', but found '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + " does not have CSS class '" + getExpectedValue() + "'";
  }

}