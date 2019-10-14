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
package io.wcm.qa.galenium.sampling.element.base;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Base class for element sampling.
 *
 * @param <T> type to return as sample
 * @since 1.0.0
 */
public abstract class ElementBasedSampler<T> extends SelectorBasedSampler<T> {

  private WebElement cachedElement;

  /**
   * <p>Constructor for ElementBasedSampler.</p>
   *
   * @param selector to identify element
   */
  public ElementBasedSampler(Selector selector) {
    super(selector);
  }

  /** {@inheritDoc} */
  @Override
  public T sampleValue() {
    try {
      return attemptSampling();
    }
    catch (StaleElementReferenceException ex) {
      getLogger().debug("caught StaleElementReferencesException: '" + getElementName() + "'");
      invalidateCache();
      return attemptSampling();
    }
  }

  protected T attemptSampling() {
    if (isCaching()) {
      if (getCachedValue() == null) {
        sampleWithCaching();
      }
      return getCachedValue();
    }
    WebElement element = getElement();
    if (element == null) {
      return handleNoElementFound();
    }
    return sampleValue(element);
  }

  protected WebElement findElement() {
    WebElement freshElement = Element.findNow(getSelector());
    getLogger().trace("element based sampler found: " + freshElement);
    return freshElement;
  }

  protected WebElement getElement() {
    if (isCaching()) {
      if (cachedElement == null) {
        cachedElement = findElement();
      }
      getLogger().trace("returning cached element: " + cachedElement);
      return cachedElement;
    }
    return findElement();
  }

  protected T getNoSampleFoundValue() {
    return null;
  }

  protected T handleNoElementFound() {
    T noSampleFoundValue = getNoSampleFoundValue();
    getLogger().debug("did not find '" + getSelector().elementName() + "' when trying to sample. Returning: '" + noSampleFoundValue + "'");
    return noSampleFoundValue;
  }

  @Override
  protected void invalidateCache() {
    super.invalidateCache();
    cachedElement = null;
  }

  protected abstract T sampleValue(WebElement elementToSampleFrom);

  protected void sampleWithCaching() {
    WebElement elementToSampleFrom = getElement();
    if (elementToSampleFrom != null) {
      T sampledValue = sampleValue(elementToSampleFrom);
      setCachedValue(sampledValue);
    }
  }
}
