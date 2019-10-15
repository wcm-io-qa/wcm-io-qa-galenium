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
package io.wcm.qa.glnm.sampling.element;

import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.sampling.element.base.WebElementBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Existence and visibility of element.
 *
 * @since 1.0.0
 */
public class VisibilitySampler extends WebElementBasedSampler<Boolean> {

  /**
   * <p>Constructor for VisibilitySampler.</p>
   *
   * @param selector to identify element
   * @since 3.0.0
   */
  public VisibilitySampler(Selector selector) {
    super(selector);
  }

  private Boolean isDisplayed(WebElement element) {
    return element != null && element.isDisplayed();
  }

  @Override
  protected Boolean freshSample(WebElement element) {
    return isDisplayed(element);
  }
}
