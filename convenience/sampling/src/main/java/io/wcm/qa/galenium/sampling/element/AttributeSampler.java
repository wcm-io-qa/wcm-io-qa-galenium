/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.sampling.element;

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.selectors.Selector;


public class AttributeSampler extends ElementBasedStringSampler {

  private String attributeName;

  public AttributeSampler(Selector selector, String attributeName) {
    this(selector, attributeName, 0);
  }

  public AttributeSampler(Selector selector, String attributeName, int timeOut) {
    super(selector, timeOut);
    setAttributeName(attributeName);
  }

  public String getAttributeName() {
    return attributeName;
  }

  @Override
  protected String sampleValue(WebElement element) {
    return element.getAttribute(getAttributeName());
  }

  protected void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

}
