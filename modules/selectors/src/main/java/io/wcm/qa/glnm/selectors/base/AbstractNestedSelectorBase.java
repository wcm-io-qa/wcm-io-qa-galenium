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
package io.wcm.qa.glnm.selectors.base;

import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

/**
 * Abstract base class for {@link NestedSelector} implementations.
 */
public class AbstractNestedSelectorBase extends AbstractSelectorBase implements NestedSelector {

  private Selector absolute;
  private Collection<NestedSelector> children;
  private NestedSelector parent;
  private Selector relative;

  /**
   * @param child to add
   */
  public void addChild(NestedSelector child) {
    getChildren().add(child);
  }

  @Override
  public Selector asAbsolute() {
    if (getAbsolute() == null) {
      setAbsolute(getClone());
    }
    return getAbsolute();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.selectors.AbstractSelectorBase#asBy()
   */
  @Override
  public By asBy() {
    return asAbsolute().asBy();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.selectors.AbstractSelectorBase#asLocator()
   */
  @Override
  public Locator asLocator() {
    return asAbsolute().asLocator();
  }

  @Override
  public Selector asRelative() {
    if (getRelative() == null) {
      setRelative(getClone());
    }
    return getRelative();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.selectors.AbstractSelectorBase#asString()
   */
  @Override
  public String asString() {
    return asAbsolute().asString();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.selectors.AbstractSelectorBase#elementName()
   */
  @Override
  public String elementName() {
    return asAbsolute().elementName();
  }

  @Override
  public Collection<NestedSelector> getChildren() {
    if (children == null) {
      children = new ArrayList<NestedSelector>();
    }
    return children;
  }

  @Override
  public NestedSelector getParent() {
    return parent;
  }

  @Override
  public boolean hasChildren() {
    return !getChildren().isEmpty();
  }

  @Override
  public boolean hasParent() {
    return getParent() != null;
  }

  /**
   * Set parent and update selector state.
   * @param parent new parent selector
   */
  public void setParent(NestedSelector parent) {
    this.parent = parent;
    parentChanged(parent);
  }

  private Selector getClone() {
    return SelectorFactory.fromValues(getName(), getString(), getBy(), getLocator());
  }

  protected Selector getAbsolute() {
    return absolute;
  }

  protected Selector getRelative() {
    return relative;
  }

  protected void parentChanged(NestedSelector newParent) {
    if (newParent != null) {
      setAbsolute(SelectorFactory.relativeToAbsolute(newParent, asRelative()));
    }
    else {
      setAbsolute(null);
    }
  }

  protected void setAbsolute(Selector absolute) {
    this.absolute = absolute;
  }

  protected void setChildren(Collection<NestedSelector> children) {
    this.children = children;
  }

  protected void setRelative(Selector relative) {
    this.relative = relative;
  }

}
