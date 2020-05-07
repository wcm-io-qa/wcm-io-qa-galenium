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

import org.apache.commons.collections4.IterableUtils;
import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Convenience class to extract sample from first element returned for selector.
 *
 * @param <T> type of sample to extract from element
 * @since 4.0.0
 */
public abstract class SingleElementSampler<T> extends MultiElementSampler<T> {

  protected SingleElementSampler(Selector selector) {
    super(selector);
  }

  @Override
  protected T freshSample(Iterable<WebElement> webElements) {
    if (IterableUtils.isEmpty(webElements)) {
      return handleNullSampling();
    }
    return freshSample(IterableUtils.first(webElements));
  }

  protected abstract T freshSample(WebElement firstElement);
}
