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

import io.wcm.qa.galenium.selectors.IndexedSelector;
import io.wcm.qa.galenium.selectors.NestedSelector;

/**
 * Indexed selectors carry an index for each level to have a way to individually address multiple elements matching the
 * same selector.
 */
public class DelegatingIndexedSelector implements IndexedSelector {

  private NestedSelector delegatee;
  private int index;

  /**
   * @param selector to use for selector information
   */
  public DelegatingIndexedSelector(NestedSelector selector) {
    this(selector, 0);
  }

  /**
   * @param selector to use for selector information
   * @param index index for addressing specific element
   */
  public DelegatingIndexedSelector(NestedSelector selector, int index) {
    setDelgatee(selector);
    setIndex(index);
  }

  @Override
  public By asBy() {
    return getDelegatee().asBy();
  }

  @Override
  public Locator asLocator() {
    return getDelegatee().asLocator();
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
    return getDelegatee().getChildren();
  }

  @Override
  public int getIndex() {
    return index;
  }

  @Override
  public NestedSelector getParent() {
    return getDelegatee().getParent();
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

  private void setDelgatee(NestedSelector delgatee) {
    this.delegatee = delgatee;
  }

  protected void setIndex(int index) {
    this.index = index;
  }

}
