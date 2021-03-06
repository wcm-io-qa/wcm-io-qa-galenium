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

import java.util.List;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Samples {@link org.openqa.selenium.WebElement}s from browser.
 */
class WebElementSampler extends SelectorBasedSampler<Iterable<WebElement>> {

  private static final Logger LOG = LoggerFactory.getLogger(WebElementSampler.class);

  /**
   * <p>Constructor for WebElementSampler.</p>
   *
   * @param selector to retrieve element
   */
  WebElementSampler(Selector selector) {
    super(selector);
  }

  private List<WebElement> findElements() {
    List<WebElement> freshElements = Element.findAllNow(getSelector());
    if (LOG.isTraceEnabled()) {
      LOG.trace("element sampler (" + getElementName() + ") found: " + freshElements.size() + " elements");
    }
    return freshElements;
  }

  @Override
  protected Iterable<WebElement> freshSample() {
    try {
      return findElements();
    }
    catch (StaleElementReferenceException ex) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("caught StaleElementReferencesException: '" + getElementName() + "'");
      }
      return findElements();
    }
  }
}
