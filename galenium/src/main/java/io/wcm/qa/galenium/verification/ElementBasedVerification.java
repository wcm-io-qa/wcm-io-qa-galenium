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
import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.InteractionUtil;

abstract class ElementBasedVerification extends VerificationBase {

  private Selector selector;

  protected ElementBasedVerification(Selector selector) {
    super(selector.elementName());
    setSelector(selector);
  }

  protected ElementBasedVerification(Selector selector, String expectedValue) {
    super(expectedValue);
    setSelector(selector);
  }

  protected WebElement getElement() {
    return InteractionUtil.getElementVisible(getSelector());
  }

  protected String getElementName() {
    return getSelector().elementName();
  }

  @Override
  public String getVerificationName() {
    return getElementName();
  }

  @Override
  protected String getExpectedKey() {
    if (StringUtils.isNotBlank(super.getExpectedKey())) {
      return super.getExpectedKey() + "." + getElementName();
    }
    return getElementName();
  }

  @Override
  protected String getNotVerifiedMessage() {
    return super.getNotVerifiedMessage() + ": " + getElementName();
  }

  protected Selector getSelector() {
    return selector;
  }

  protected void setSelector(Selector selector) {
    this.selector = selector;
  }
}
