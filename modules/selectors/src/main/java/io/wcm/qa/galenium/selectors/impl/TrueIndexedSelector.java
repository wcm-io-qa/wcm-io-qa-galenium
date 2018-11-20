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

import java.util.ArrayList;
import java.util.Collection;

import io.wcm.qa.galenium.selectors.IndexedSelector;
import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.impl.base.AbstractIndexedSelectorBase;
import io.wcm.qa.galenium.selectors.util.SelectorFactory;

/**
 * Non-delegating fully indexed Selector. This means parent and children are also indexed selectors.
 */
public class TrueIndexedSelector extends AbstractIndexedSelectorBase {

  @Override
  public void addChild(NestedSelector child) {
    super.addChild(indexed(child));
  }

  @Override
  public String elementName() {
    if (hasParent()) {
      return getParent().elementName() + "." + asRelative().elementName() + "[" + getIndex() + "]";
    }
    return super.elementName() + "[" + getIndex() + "]";
  }

  @Override
  public IndexedSelector getParent() {
    return (IndexedSelector)super.getParent();
  }

  @Override
  public void setParent(NestedSelector parent) {
    super.setParent(indexed(parent));
  }

  private Collection<NestedSelector> indexed(Collection<? extends NestedSelector> selectors) {
    Collection<NestedSelector> indexedChildren = new ArrayList<NestedSelector>();
    for (NestedSelector selector : selectors) {
      indexedChildren.add(indexed(selector));
    }
    return indexedChildren;
  }

  private IndexedSelector indexed(Selector selector) {
    return SelectorFactory.indexedFromSelector(selector);
  }

  @Override
  protected void setChildren(Collection<NestedSelector> children) {
    super.setChildren(indexed(children));
  }

}
