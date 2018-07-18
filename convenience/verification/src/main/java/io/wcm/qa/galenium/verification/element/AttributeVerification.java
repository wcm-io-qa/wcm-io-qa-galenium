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
 * Make sure an attribute is set on an element.
 */
public class AttributeVerification extends ElementBasedVerification {

  private String attributeName;

  /**
   * @param selector to identify element
   * @param attributeName name of attribute to check
   */
  public AttributeVerification(Selector selector, String attributeName) {
    super(selector);
    setAttributeName(attributeName);
    setPreVerification(new VisibilityVerification(getSelector()));
  }

  /**
   * @param selector to identify element
   * @param attributeName name of attribute to check
   * @param expectedValue to verify against
   */
  public AttributeVerification(Selector selector, String attributeName, String expectedValue) {
    this(selector, attributeName);
    setExpectedValue(expectedValue);
  }

  /**
   * @param verificationName name of element for reporting
   * @param element resolved element to test
   * @param attributeName name of attribute to check
   */
  public AttributeVerification(String verificationName, WebElement element, String attributeName) {
    super(verificationName, element);
    setAttributeName(attributeName);
  }

  /**
   * @param verificationName name of element for reporting
   * @param element resolved element to test
   * @param attributeName name of attribute to check
   * @param expectedValue to verify against
   */
  public AttributeVerification(String verificationName, WebElement element, String attributeName, String expectedValue) {
    this(verificationName, element, attributeName);
    setExpectedValue(expectedValue);
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getElementName() + "[" + getAttributeName() + "]: '" + getExpectedValue() + "'";
  }

  protected String getAttributeName() {
    return attributeName;
  }

  @Override
  protected String getExpectedKey() {
    return super.getExpectedKey() + "." + getAttributeName();
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + "[" + getAttributeName() + "] should be '" + getExpectedValue() + "', but was '" + getActualValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + "[" + getAttributeName() + "] was '" + getActualValue() + "' as expected";
  }

  @Override
  protected String sampleValue() {
    return getElement().getAttribute(getAttributeName());
  }

  protected void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
}
