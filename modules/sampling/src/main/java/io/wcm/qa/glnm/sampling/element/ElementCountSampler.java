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

import org.apache.commons.collections4.IterableUtils;
import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.sampling.element.base.MultiElementSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Counts elements matching the selector.
 *
 * @since 1.0.0
 */
public class ElementCountSampler extends MultiElementSampler<Integer> {

  /**
   * <p>Constructor for ElementCountSampler.</p>
   *
   * @param selector identifies elements to be counted
   * @since 3.0.0
   */
  public ElementCountSampler(Selector selector) {
    super(selector);
  }

  @Override
  protected Integer freshSample(Iterable<WebElement> webElements) {
    return IterableUtils.size(webElements);
  }

}
