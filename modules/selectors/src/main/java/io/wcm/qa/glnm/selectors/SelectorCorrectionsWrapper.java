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
package io.wcm.qa.glnm.selectors;

import org.openqa.selenium.By;

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.selectors.base.Selector;


/**
 * Convenience wrapper to add corrections to Galen {@link com.galenframework.specs.page.Locator} returned by this  {@link io.wcm.qa.glnm.selectors.base.Selector}.
 *
 * @since 1.0.0
 */
public class SelectorCorrectionsWrapper implements Selector {

  private CorrectionsRect correctionsRect;
  private Selector delegate;

  /**
   * <p>Constructor for SelectorCorrectionsWrapper.</p>
   *
   * @param selector Selector to wrap
   * @param corrections corrections to add to locator
   */
  public SelectorCorrectionsWrapper(Selector selector, CorrectionsRect corrections) {
    delegate = selector;
    correctionsRect = corrections;
  }

  /** {@inheritDoc} */
  @Override
  public By asBy() {
    return delegate.asBy();
  }

  /** {@inheritDoc} */
  @Override
  public Locator asLocator() {
    return new LocatorCorrectionsWrapper(delegate.asLocator(), correctionsRect);
  }

  /** {@inheritDoc} */
  @Override
  public String asString() {
    return delegate.asString();
  }

  /** {@inheritDoc} */
  @Override
  public String elementName() {
    return delegate.elementName();
  }

}
