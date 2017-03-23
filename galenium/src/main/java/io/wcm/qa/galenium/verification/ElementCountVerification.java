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

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.InteractionUtil;

public class ElementCountVerification extends ElementBasedVerification {

  protected ElementCountVerification(Selector selector, int expectedCount) {
    super(selector, Integer.toString(expectedCount));
  }

  @Override
  protected Boolean doVerification() {
    String elementCount = sampleValue();
    return StringUtils.equals(elementCount, getExpectedValue());
  }

  @Override
  protected String sampleValue() {
    List<WebElement> elements = InteractionUtil.findElements(getSelector());
    return Integer.toString(elements.size());
  }

  @Override
  protected String getFailureMessage() {
    return "Expected " + getExpectedValue() + " elements matching " + getElementName() + " but found " + getActualValue();
  }

  @Override
  protected String getSuccessMessage() {
    return "Found " + getExpectedValue() + " elements matching " + getElementName();
  }

}
