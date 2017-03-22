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
import io.wcm.qa.galenium.util.InteractionUtil;

abstract class ElementBasedVerification extends VerificationBase {

  private Selector selector;

  protected ElementBasedVerification(Selector selector, String expectedValue) {
    super(expectedValue);
    setSelector(selector);
  }

  @Override
  protected String getAdditionalToStringInfo() {
    return getElementName();
  }

  protected WebElement getElement() {
    return InteractionUtil.getElementVisible(getSelector());
  }

  protected String getElementName() {
    return getSelector().elementName();
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
