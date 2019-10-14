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
package io.wcm.qa.galenium.sampling.element.base;

import io.wcm.qa.galenium.sampling.base.CachingBasedSampler;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Selector based sampling.
 *
 * @param <T> type of sample
 * @since 1.0.0
 */
public abstract class SelectorBasedSampler<T> extends CachingBasedSampler<T> {

  private Selector selector;

  /**
   * <p>Constructor for SelectorBasedSampler.</p>
   *
   * @param selector to identify element(s)
   */
  public SelectorBasedSampler(Selector selector) {
    setSelector(selector);
  }

  /**
   * <p>Getter for the field <code>selector</code>.</p>
   *
   * @return a {@link io.wcm.qa.galenium.selectors.base.Selector} object.
   */
  public Selector getSelector() {
    return selector;
  }

  /**
   * <p>Setter for the field <code>selector</code>.</p>
   *
   * @param selector a {@link io.wcm.qa.galenium.selectors.base.Selector} object.
   */
  public void setSelector(Selector selector) {
    this.selector = selector;
  }

  protected String getElementName() {
    return getSelector().elementName();
  }

}
