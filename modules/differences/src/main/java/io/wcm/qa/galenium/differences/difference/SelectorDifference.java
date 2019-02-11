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
package io.wcm.qa.galenium.differences.difference;

import io.wcm.qa.galenium.differences.base.DifferenceBase;
import io.wcm.qa.galenium.selectors.IndexedSelector;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.util.SelectorFactory;

/**
 * Difference based on selector name.
 */
public class SelectorDifference extends DifferenceBase {

  private IndexedSelector selector;

  /**
   * @param selector to get name from
   */
  public SelectorDifference(Selector selector) {
    this.selector = SelectorFactory.indexedFromSelector(selector);
  }

  @Override
  protected String getRawTag() {
    return getSelectorName(selector);
  }

  private String getSelectorName(IndexedSelector iSel) {
    StringBuilder sb = new StringBuilder();
    if (iSel.hasParent()) {
      IndexedSelector parent = SelectorFactory.indexedFromSelector(iSel.getParent());
      sb.append(getSelectorName(parent));
    }
    sb.append(iSel.elementName());
    if (iSel.getIndex() != 0) {
      sb.append(".");
      sb.append(iSel.getIndex());
    }
    return sb.toString();
  }

}
