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

import io.wcm.qa.glnm.persistence.util.TextSampleManager;
import io.wcm.qa.glnm.sampling.element.ElementCountSampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.element.base.SelectorBasedVerification;

/**
 * Make sure a certain number of elements are present.
 */
public class ElementCountVerification extends SelectorBasedVerification<ElementCountSampler, Integer> {

  /**
   * @param selector identifies elements to count
   */
  public ElementCountVerification(Selector selector) {
    super("count(" + selector.elementName() + ")", new ElementCountSampler(selector));
  }

  /**
   * @param selector to identify element
   * @param expectedCount to verify against
   */
  public ElementCountVerification(Selector selector, int expectedCount) {
    this(selector);
    setExpectedValue(expectedCount);
  }

  @Override
  protected boolean doVerification() {
    return sampleValue() == getExpectedValue();
  }

  protected String getElementName() {
    return getSampler().getSelector().elementName();
  }

  @Override
  protected String getFailureMessage() {
    return "Expected " + getExpectedValue() + " elements matching " + getElementName() + " but found " + getCachedValue();
  }

  @Override
  protected String getSuccessMessage() {
    return "Found " + getExpectedValue() + " elements matching " + getElementName();
  }

  @Override
  protected Integer initExpectedValue() {
    String expectedKey = getExpectedKey();
    if (StringUtils.isNotBlank(expectedKey)) {
      return Integer.parseInt(TextSampleManager.getExpectedText(expectedKey));
    }
    return 0;
  }

  @Override
  protected void persistSample(String key, Integer newValue) {
    TextSampleManager.addNewTextSample(key, Integer.toString(newValue));
  }

}
