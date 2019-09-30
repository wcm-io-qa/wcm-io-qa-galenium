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

import java.util.Collection;

/**
 * A nested {@link Selector} can have a parent and children. If it has a parent, its standard representation will have
 * CSS relative to parent.
 */
public interface NestedSelector extends Selector {

  /**
   * Cloned selector with CSS not relative to potential parent, but as absolute in page.
   * @return a clone relative to parent
   */
  Selector asAbsolute();

  /**
   * Cloned selector with CSS not relative to potential parent, but as absolute in page.
   * @return a clone relative to parent
   */
  Selector asRelative();

  /**
   * @return a list containing this selector's child selectors, empty list if no children
   */
  Collection<NestedSelector> getChildren();

  /**
   * @return this selector's parent or null
   */
  NestedSelector getParent();

  /**
   * Children are optional. A leaf selector has no children.
   * @return whether this selector has child selectors
   */
  boolean hasChildren();

  /**
   * Parents are optional. A root selector has no parents.
   * @return whether this selector has a parent
   */
  boolean hasParent();
}
