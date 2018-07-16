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
package io.wcm.qa.galenium.verification.element;

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.selectors.Selector;

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

  /**
   * Constructor for resolved {@link WebElement}.
   * @param verificationName name of element for reporting
   * @param element resolved element to test
   * @param cssClass to verify
   */
  public NoCssClassVerification(String verificationName, WebElement element, String cssClass) {
    super(verificationName, element, cssClass);
  }

  @Override
  protected Boolean doVerification() {
    return !super.doVerification();
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + " should not have CSS class '" + getExpectedValue() + "', but found '" + getActualValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + " does not have CSS class '" + getExpectedValue() + "'";
  }

}