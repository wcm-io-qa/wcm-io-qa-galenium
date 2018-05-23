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

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.sampling.differences.DifferentiatedDifferences;
import io.wcm.qa.galenium.sampling.differences.SelectorDifference;
import io.wcm.qa.galenium.sampling.differences.StringDifference;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.verification.base.VerificationBase;

/**
 * Base class encapsulating common functionality to verify aspects of elements.
 */
abstract class ElementBasedVerification extends VerificationBase {

  private WebElement element;
  private Selector selector;
  private int timeout;

  /**
   * @param selector to identify element
   */
  protected ElementBasedVerification(Selector selector) {
    super(selector.elementName());
    setSelector(selector);
  }

  /**
   * @param selector to identify element
   * @param alternativeName name to use for verification
   */
  protected ElementBasedVerification(Selector selector, String alternativeName) {
    super(alternativeName);
    setSelector(selector);
  }

  /**
   * @param verificationName for reporting
   * @param element resolved element
   */
  protected ElementBasedVerification(String verificationName, WebElement element) {
    super(verificationName);
    setElement(element);
  }

  public WebElement getElement() {
    if (element == null) {
      element = Element.find(getSelector(), getTimeout());
    }
    return element;
  }

  /**
   * @return how long to wait in seconds
   */
  public int getTimeout() {
    return timeout;
  }

  @Override
  public String getVerificationName() {
    return getElementName();
  }

  public void setElement(WebElement element) {
    this.element = element;
  }

  protected String getElementName() {
    if (getSelector() != null) {
      return getSelector().elementName();
    }
    return super.getVerificationName();
  }

  @Override
  protected String getExpectedKey() {
    DifferentiatedDifferences differentiatedDifferences = new DifferentiatedDifferences();
    differentiatedDifferences.addAll(getDifferences().getDifferences());
    if (getSelector() != null) {
      differentiatedDifferences.add(new SelectorDifference(getSelector()));
    }
    else {
      StringDifference elementNameDifference = new StringDifference(getElementName());
      elementNameDifference.setName("elementname");
      differentiatedDifferences.add(elementNameDifference);
    }
    differentiatedDifferences.setCutoff(1);
    return differentiatedDifferences.asPropertyKey();
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

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }
}
