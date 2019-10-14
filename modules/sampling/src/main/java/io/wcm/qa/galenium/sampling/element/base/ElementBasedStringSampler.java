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
package io.wcm.qa.galenium.sampling.element.base;

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Base class for String sampling from elements.
 *
 * @since 1.0.0
 */
public abstract class ElementBasedStringSampler extends ElementBasedSampler<String> {

  /**
   * <p>Constructor for ElementBasedStringSampler.</p>
   *
   * @param selector identifies element
   */
  public ElementBasedStringSampler(Selector selector) {
    super(selector);
  }

  @Override
  protected abstract String sampleValue(WebElement element);

}
