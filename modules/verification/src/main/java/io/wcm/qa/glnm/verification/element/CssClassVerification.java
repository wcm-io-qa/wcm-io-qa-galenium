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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Make sure a certain CSS class is set on an element.
 */
public class CssClassVerification extends AttributeVerification {

  private static final String ATTRIBUTE_NAME_CLASS = "class";
  private String expectedValue;

  /**
   * Constructor for {@link Selector}.
   * @param selector to identify element
   */
  public CssClassVerification(Selector selector) {
    super(selector, ATTRIBUTE_NAME_CLASS);
  }

  /**
   * Constructor for {@link Selector}.
   * @param selector to identify element
   * @param cssClass to verify against
   */
  public CssClassVerification(Selector selector, String cssClass) {
    super(selector, ATTRIBUTE_NAME_CLASS, cssClass);
    setExpectedValue(cssClass);
  }

  @Override
  protected boolean doVerification() {
    String cssClasses = getActualValue();
    if (StringUtils.isBlank(cssClasses)) {
      return false;
    }
    String[] splitCssClasses = cssClasses.split(" ");
    return ArrayUtils.contains(splitCssClasses, getExpectedValue());
  }

  @Override
  protected String getExpectedValue() {
    return expectedValue;
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + " should have CSS class '" + getExpectedValue() + "', but only found '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + " has CSS class '" + getExpectedValue() + "'";
  }

  @Override
  protected void setExpectedValue(String expectedValue) {
    this.expectedValue = expectedValue;
  }

}
