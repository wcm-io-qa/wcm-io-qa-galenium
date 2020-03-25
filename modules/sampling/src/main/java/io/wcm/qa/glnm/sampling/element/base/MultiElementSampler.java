/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.sampling.element.base;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Abstract base class for sampling values per element.
 *
 * @param <T> type of sample to extract from each element
 * @since 4.0.0
 */
public abstract class MultiElementSampler<T> extends SelectorBasedSampler<T> {

  private final WebElementSampler elementSampler;

  protected MultiElementSampler(Selector selector) {
    super(selector);
    elementSampler = new WebElementSampler(getSelector());
  }

  @Override
  protected T freshSample() {
    return freshSample(elementSampler.sampleValue());
  }

  protected abstract T freshSample(Iterable<WebElement> webElements);

}
