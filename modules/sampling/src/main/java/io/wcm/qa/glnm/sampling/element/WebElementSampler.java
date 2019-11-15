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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.sampling.element.base.SelectorBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Samples {@link org.openqa.selenium.WebElement} from browser.
 *
 * @since 3.0.0
 */
public class WebElementSampler extends SelectorBasedSampler<WebElement> {

  private static final Logger LOG = LoggerFactory.getLogger(WebElementSampler.class);

  /**
   * <p>Constructor for WebElementSampler.</p>
   *
   * @param selector to retrieve element
   * @since 3.0.0
   */
  public WebElementSampler(Selector selector) {
    super(selector);
  }

  private WebElement findElement() {
    WebElement freshElement = Element.findNow(getSelector());
    LOG.trace("element sampler (" + getElementName() + ") found: " + freshElement);
    return freshElement;
  }

  @Override
  protected WebElement freshSample() {
    try {
      return findElement();
    }
    catch (StaleElementReferenceException ex) {
      LOG.debug("caught StaleElementReferencesException: '" + getElementName() + "'");
      return findElement();
    }
  }
}
