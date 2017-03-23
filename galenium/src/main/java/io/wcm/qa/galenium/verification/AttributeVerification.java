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
package io.wcm.qa.galenium.verification;

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.selectors.Selector;

public class AttributeVerification extends ElementBasedVerification {

  private String attributeName;

  public AttributeVerification(Selector selector, String attributeName) {
    super(selector);
    setAttributeName(attributeName);
  }

  public AttributeVerification(Selector selector, String attributeName, String expectedValue) {
    super(selector, expectedValue);
    setAttributeName(attributeName);
  }

  public AttributeVerification(String elementName, WebElement element, String attributeName) {
    super(elementName, element);
    setAttributeName(attributeName);
  }

  public AttributeVerification(String elementName, WebElement element, String attributeName, String expectedValue) {
    this(elementName, element, attributeName);
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