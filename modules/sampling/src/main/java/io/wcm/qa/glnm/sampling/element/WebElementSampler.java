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
package io.wcm.qa.glnm.sampling.element;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.sampling.element.base.SelectorBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Samples {@link WebElement} from browser.
 */
public class WebElementSampler extends SelectorBasedSampler<WebElement> {

  /**
   * @param selector to retrieve element
   */
  public WebElementSampler(Selector selector) {
    super(selector);
  }

  @Override
  protected WebElement freshSample() {
    try {
      return findElement();
    }
    catch (StaleElementReferenceException ex) {
      getLogger().debug("caught StaleElementReferencesException: '" + getElementName() + "'");
      return findElement();
    }
  }

  private WebElement findElement() {
    WebElement freshElement = Element.findNow(getSelector());
    getLogger().trace("element sampler (" + getElementName() + ") found: " + freshElement);
    return freshElement;
  }
}
