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
package io.wcm.qa.galenium.selectors;

import java.util.Collection;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.selectors.base.NestedSelector;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * {@link io.wcm.qa.galenium.selectors.base.NestedSelector} implementation immediately setting {@link org.openqa.selenium.By}, {@link com.galenframework.specs.page.Locator}, parent and children values on
 * instantiation. This avoids harder to trace errors inherent in lazy evaluation used in other implementations.
 *
 * @since 1.0.0
 */
public class FixedValueNestedSelector extends FixedValueSelector implements NestedSelector {

  private Selector absolute;
  private Collection<NestedSelector> children;
  private NestedSelector parent;
  private Selector relative;

  /**
   * Uses the element name, selector CSS, {@link org.openqa.selenium.By}, {@link com.galenframework.specs.page.Locator}, parent and children from selector.
   *
   * @param selector to extract values from
   */
  public FixedValueNestedSelector(NestedSelector selector) {
    this(selector.elementName(), selector.asString(), selector.asBy(), selector.asLocator(), selector.asAbsolute(), selector.asRelative(),
        selector.getParent(), selector.getChildren());
  }

  /**
   * Uses the parameters as values.
   *
   * @param elementName to use for selector
   * @param css to use for selector
   * @param by to use for selector
   * @param locator to use for selector
   * @param absolute absolute version of selector
   * @param relative relative version of selector
   * @param parent to use for selector
   * @param children to use for selector
   */
  public FixedValueNestedSelector(String elementName, String css, By by, Locator locator, Selector absolute,
      Selector relative, NestedSelector parent, Collection<NestedSelector> children) {
    super(elementName, css, by, locator);
    this.absolute = absolute;
    this.children = children;
    this.parent = parent;
    this.relative = relative;
  }

  /** {@inheritDoc} */
  @Override
  public Selector asAbsolute() {
    return absolute;
  }

  /** {@inheritDoc} */
  @Override
  public Selector asRelative() {
    return relative;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<NestedSelector> getChildren() {
    return children;
  }

  /** {@inheritDoc} */
  @Override
  public NestedSelector getParent() {
    return parent;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasChildren() {
    boolean hasNoChildren = ((children == null) || (children.isEmpty()));
    return !hasNoChildren;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasParent() {
    return parent != null;
  }

}
