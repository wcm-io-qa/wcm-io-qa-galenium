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
package io.wcm.qa.glnm.sampling.element.base;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.sampling.element.WebElementSampler;
import io.wcm.qa.glnm.sampling.transform.base.TransformationBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Base class for element sampling.
 *
 * @since 3.0.0
 */
public abstract class WebElementBasedSampler<O>
    extends TransformationBasedSampler<WebElementSampler, WebElement, O> {

  /**
   * <p>Constructor for WebElementBasedSampler.</p>
   *
   * @param selector to identify element
   * @since 3.0.0
   */
  public WebElementBasedSampler(Selector selector) {
    super(new WebElementSampler(selector));
  }

  @Override
  protected O transform(WebElement inputSample) {
    return freshSample(inputSample);
  }

  /**
   * <p>getSelector.</p>
   *
   * @return a {@link io.wcm.qa.glnm.selectors.base.Selector} object.
   * @since 3.0.0
   */
  public Selector getSelector() {
    return getInput().getSelector();
  }

  protected abstract O freshSample(WebElement inputSample);
}
