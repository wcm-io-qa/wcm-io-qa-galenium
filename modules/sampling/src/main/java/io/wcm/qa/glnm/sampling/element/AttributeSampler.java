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
package io.wcm.qa.glnm.sampling.element;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.sampling.element.base.SingleElementSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Samples an attribute of a web element.
 *
 * @since 1.0.0
 */
public class AttributeSampler extends SingleElementSampler<String> {

  private String attributeName;

  /**
   * <p>Constructor for AttributeSampler.</p>
   *
   * @param selector identifies element
   * @param attributeName name of attribute to sample
   * @since 3.0.0
   */
  public AttributeSampler(Selector selector, String attributeName) {
    super(selector);
    setAttributeName(attributeName);
  }

  /**
   * <p>Getter for the field <code>attributeName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public String getAttributeName() {
    return attributeName;
  }

  @Override
  protected String freshSample(WebElement element) {
    return element.getAttribute(getAttributeName());
  }

  protected void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }

}
