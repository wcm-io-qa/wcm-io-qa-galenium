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

import java.util.Collection;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.selectors.NestedSelector;

/**
 * {@link NestedSelector} implementation immediately setting {@link By}, {@link Locator}, parent and children values on
 * instantiation. This avoids harder to trace errors inherent in lazy evaluation used in other implementations.
 */
public class FixedValueNestedSelector extends FixedValueSelector implements NestedSelector {

  private Collection<NestedSelector> children;
  private NestedSelector parent;

  /**
   * Uses the element name, selector CSS, {@link By}, {@link Locator}, parent and children from selector.
   * @param selector to extract values from
   */
  public FixedValueNestedSelector(NestedSelector selector) {
    this(selector.elementName(),
        selector.asString(),
        selector.asBy(),
        selector.asLocator(),
        selector.getParent(),
        selector.getChildren());
  }

  /**
   * Uses the parameters as values.
   * @param elementName to use for selector
   * @param css to use for selector
   * @param by to use for selector
   * @param locator to use for selector
   * @param parent to use for selector
   * @param children to use for selector
   */
  public FixedValueNestedSelector(
      String elementName,
      String css,
      By by,
      Locator locator,
      NestedSelector parent,
      Collection<NestedSelector> children) {
    super(elementName, css, by, locator);
    this.children = children;
    this.parent = parent;
  }

  @Override
  public Collection<NestedSelector> getChildren() {
    return children;
  }

  @Override
  public NestedSelector getParent() {
    return parent;
  }

  @Override
  public boolean hasChildren() {
    if (children == null) {
      return false;
    }
    if (children.isEmpty()) {
      return false;
    }
    return true;
  }

  @Override
  public boolean hasParent() {
    return parent != null;
  }

}
