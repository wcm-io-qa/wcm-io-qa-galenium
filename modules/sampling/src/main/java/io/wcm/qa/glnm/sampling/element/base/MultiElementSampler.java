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

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Abstract base class for sampling values per element.
 *
 * @param <T> type of sample to extract from each element
 * @since 4.0.0
 */
public abstract class MultiElementSampler<T> extends WebElementBasedSampler<Iterable<T>> {

  protected MultiElementSampler(Selector selector) {
    super(selector);
  }

  @Override
  protected Iterable<T> transform(Iterable<WebElement> inputSample) {
    ArrayList<T> freshSamples = new ArrayList<T>();
    for (WebElement webElement : inputSample) {
      freshSamples.add(freshSample(webElement));
    }
    return freshSamples;
  }

  protected abstract T freshSample(WebElement webElement);

}
