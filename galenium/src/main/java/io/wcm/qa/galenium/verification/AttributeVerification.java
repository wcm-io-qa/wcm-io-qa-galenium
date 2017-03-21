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

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.selectors.Selector;

public class AttributeVerification extends VerificationBase {

  private String actualValue;
  private String attributeName;
  protected String expectedValue;

  public AttributeVerification(Selector selector, String attributeName, String expectedValue) {
    super(selector);
    setAttributeName(attributeName);
    setExpectedValue(expectedValue);
  }

  @Override
  protected Boolean doVerification() {
    return StringUtils.equals(getActualValue(), getExpectedValue());
  }

  protected String getActualValue() {
    if (actualValue == null) {
      actualValue = getElement().getAttribute(getAttributeName());
    }
    return actualValue;
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getAttributeName() + ": '" + getExpectedValue() + "'";
  }

  protected String getAttributeName() {
    return attributeName;
  }

  protected String getExpectedValue() {
    return expectedValue;
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + "[" + getAttributeName() + "] should be '" + getExpectedValue() + "', but was '" + getActualValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + "[" + getAttributeName() + "] was '" + getActualValue() + "' as expected";
  }

  protected void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

  protected void setExpectedValue(String expectedValue) {
    this.expectedValue = expectedValue;
  }

}
