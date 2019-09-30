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

import io.wcm.qa.glnm.sampling.element.AttributeSampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.element.base.WebElementBasedStringVerification;

/**
 * Make sure an attribute is set on an element.
 */
public class AttributeVerification extends WebElementBasedStringVerification<AttributeSampler> {

  /**
   * @param selector to identify element
   * @param attributeName name of attribute to check
   */
  public AttributeVerification(Selector selector, String attributeName) {
    super(selector.elementName(), new AttributeSampler(selector, attributeName));
    setPreVerification(new VisibilityVerification(selector));
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

  @Override
  protected void afterVerification() {
    getLogger().trace("looking for '" + getExpectedValue() + "'");
    String cachedValue = getCachedValue();
    getLogger().trace("found: '" + cachedValue + "'");
    if (!isVerified() && cachedValue != null) {
      String expectedKey = getExpectedKey();
      persistSample(expectedKey, cachedValue);
    }
    getLogger().trace("done verifying (" + toString() + ")");
  }

  @Override
  protected boolean doVerification() {
    return StringUtils.equals(getExpectedValue(), getActualValue());
  }

  protected String getAttributeName() {
    return getSampler().getAttributeName();
  }

  protected String getElementWithAttributeName() {
    return getElementName() + "[" + getAttributeName() + "]";
  }

  @Override
  protected String getExpectedKey() {
    return super.getExpectedKey() + "." + getAttributeName();
  }

  @Override
  protected String getFailureMessage() {
    return getElementWithAttributeName() + " should be '" + getExpectedValue() + "', but was '" + getCachedValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementWithAttributeName() + " was '" + getCachedValue() + "' as expected";
  }
}
