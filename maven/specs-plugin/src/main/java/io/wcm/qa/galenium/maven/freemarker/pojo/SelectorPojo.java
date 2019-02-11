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
package io.wcm.qa.galenium.maven.freemarker.pojo;

import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.By;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.util.SelectorFactory;

/**
 * Wrapper for NestedSelector plus references to spec.
 */
public class SelectorPojo implements NestedSelector {

  private NestedSelector delegatee;
  private SpecPojo spec;

  /**
   * Constructor.
   * @param selector to delegate to
   */
  public SelectorPojo(SpecPojo spec, NestedSelector selector) {
    setSpec(spec);
    setDelegatee(selector);
  }

  @Override
  public By asBy() {
    return getDelegatee().asBy();
  }

  @Override
  public Locator asLocator() {
    return getDelegatee().asLocator();
  }

  /**
   * @return selector relative to its parent
   */
  public Selector asRelative() {
    return SelectorFactory.relativeFromAbsolute(getParent(), getDelegatee());
  }

  @Override
  public String asString() {
    return getDelegatee().asString();
  }

  @Override
  public String elementName() {
    return getDelegatee().elementName();
  }

  @Override
  public Collection<NestedSelector> getChildren() {
    Collection<NestedSelector> children = new ArrayList<>();
    for (NestedSelector child : getDelegatee().getChildren()) {
      children.add(wrap(child));
    }
    return children;
  }

  @Override
  public NestedSelector getParent() {
    return wrap(getDelegatee().getParent());
  }

  public SpecPojo getSpec() {
    return spec;
  }

  @Override
  public boolean hasChildren() {
    return getDelegatee().hasChildren();
  }

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
    if (selector == null) {
      return null;
    }
    return new SelectorPojo(getSpec(), selector);
  }
}
