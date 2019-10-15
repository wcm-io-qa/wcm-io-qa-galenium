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
package io.wcm.qa.glnm.differences.difference.sut;

import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Difference based on selector name.
 *
 * @since 1.0.0
 */
public class SelectorDifference extends StringDifference {

  /**
   * <p>Constructor for SelectorDifference.</p>
   *
   * @param selector to get name from
   * @since 2.0.0
   */
  public SelectorDifference(Selector selector) {
    super(selector.elementName());
  }

}
