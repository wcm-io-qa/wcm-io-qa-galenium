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
package io.wcm.qa.glnm.maven.freemarker.pojo;

import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.selectors.base.NestedSelector;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Wrapper for NestedSelector plus references to spec.
 *
 * @since 1.0.0
 */
public class SelectorPojo implements NestedSelector {

  private NestedSelector delegatee;
  private SpecPojo spec;

  /**
   * Constructor.
   *
   * @param spec selector is from
   * @param selector to delegate to
   */
  public SelectorPojo(SpecPojo spec, NestedSelector selector) {
    setSpec(spec);
    setDelegatee(selector);
  }

  /** {@inheritDoc} */
  @Override
  public Selector asAbsolute() {
    return getDelegatee().asAbsolute();
  }

  /** {@inheritDoc} */
  @Override
  public By asBy() {
    return getDelegatee().asBy();
  }

  /** {@inheritDoc} */
  @Override
  public Locator asLocator() {
    return getDelegatee().asLocator();
  }

  /** {@inheritDoc} */
  @Override
  public Selector asRelative() {
    return getDelegatee().asRelative();
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return getDelegatee().asString();
  }

  /** {@inheritDoc} */
  @Override
  public String elementName() {
    return getDelegatee().elementName();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<NestedSelector> getChildren() {
    Collection<NestedSelector> children = new ArrayList<>();
    for (NestedSelector child : getDelegatee().getChildren()) {
      children.add(wrap(child));
    }
    return children;
  }

  /** {@inheritDoc} */
  @Override
  public NestedSelector getParent() {
    return wrap(getDelegatee().getParent());
  }

  /**
   * <p>Getter for the field <code>spec</code>.</p>
   *
   * @return a  {@link io.wcm.qa.glnm.maven.freemarker.pojo.SpecPojo} object.
   */
  public SpecPojo getSpec() {
    return spec;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasChildren() {
    return getDelegatee().hasChildren();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasParent() {
    return getDelegatee().hasParent();
  }

  private NestedSelector getDelegatee() {
    return delegatee;
  }

  private void setDelegatee(NestedSelector delegatee) {
    this.delegatee = delegatee;
  }

  private void setSpec(SpecPojo spec) {
    this.spec = spec;
  }

  private SelectorPojo wrap(NestedSelector selector) {
    return new SelectorPojo(getSpec(), selector);
  }
}
