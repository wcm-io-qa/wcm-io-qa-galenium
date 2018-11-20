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
package io.wcm.qa.galenium.selectors.impl;

import io.wcm.qa.galenium.selectors.Selector;

/**
 * Simple wrapper to turn regular selector into nested selector.
 * No parent, no children, and relative is absolute is this.
 */
public class NestedSelectorWrapper extends FixedValueNestedSelector {

  /**
   * @param selector to take values from
   */
  public NestedSelectorWrapper(Selector selector) {
    super(selector.elementName(), selector.asString(), selector.asBy(), selector.asLocator(), selector, selector, null, null);
  }

}
