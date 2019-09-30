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

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.sampling.element.base.SelectorBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Counts elements matching the selector.
 */
public class ElementCountSampler extends SelectorBasedSampler<Integer> {

  /**
   * @param selector identifies elements to be counted
   */
  public ElementCountSampler(Selector selector) {
    super(selector);
  }

  @Override
  public Integer freshSample() {
    return Element.findAll(getSelector()).size();
  }

}
