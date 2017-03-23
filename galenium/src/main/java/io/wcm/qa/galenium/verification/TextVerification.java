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

public class TextVerification extends ElementBasedVerification {

  public TextVerification(Selector selector) {
    super(selector);
    setPreVerification(new VisibilityVerification(getSelector()));
  }

  public TextVerification(Selector selector, String value) {
    super(selector, value);
    setPreVerification(new VisibilityVerification(getSelector()));
  }

  public TextVerification(String elementName, WebElement element) {
    super(elementName, element);
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + " should have text '" + getExpectedValue() + "' but found '" + getActualValue() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + " has text '" + getExpectedValue() + "'";
  }

  @Override
  protected String sampleValue() {
    return getElement().getText();
  }

}
