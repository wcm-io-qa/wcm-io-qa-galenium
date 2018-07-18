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
package io.wcm.qa.galenium.sampling.element;

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.selectors.Selector;


public class VisibilitySampler extends ElementBasedSampler<Boolean> {

  public VisibilitySampler(Selector selector) {
    this(selector, 0);
  }

  public VisibilitySampler(Selector selector, int timeOut) {
    super(selector, timeOut);
  }

  @Override
  public Boolean sampleValue() {
    return isDisplayed(getElement());
  }

  private Boolean isDisplayed(WebElement element) {
    return element != null && element.isDisplayed();
  }

  @Override
  protected Boolean sampleValue(WebElement element) {
    throw new GaleniumException("visibility sampler handles its own sampling and must not invoke sampleValue(WebElement)");
  }

}
