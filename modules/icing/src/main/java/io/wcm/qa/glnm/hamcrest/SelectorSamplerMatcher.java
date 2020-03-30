/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.hamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.collect.ObjectArrays;

import io.wcm.qa.glnm.sampling.element.base.SelectorBasedSampler;
import io.wcm.qa.glnm.sampling.util.SamplerFactory;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Uses underlying matcher on results of Selector based samplers.
 *
 * @param <T> sample type
 * @since 5.0.0
 */
public class SelectorSamplerMatcher<T> extends TypeSafeMatcher<Selector> {

  private Matcher<T> internalMatcher;
  private SelectorBasedSampler<T> sampler;
  private Class<? extends SelectorBasedSampler<T>> samplerClass;
  private Object[] samplerParams;
  private Selector selector;

  /**
   * <p>Constructor for SelectorSamplerMatcher.</p>
   *
   * @param matcher used to match sample
   * @param samplerClass a {@link java.lang.Class} object.
   * @param samplerParams a {@link java.lang.Object} object.
   * @since 5.0.0
   */
  public SelectorSamplerMatcher(
      Matcher<T> matcher,
      Class<? extends SelectorBasedSampler<T>> samplerClass,
      Object... samplerParams) {
    setInternalMatcher(matcher);
    setSamplerClass(samplerClass);
    setSamplerParams(samplerParams);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText(getSelectorName());
    description.appendText(" ");
    getInternalMatcher().describeTo(description);
  }

  private String getSelectorName() {
    Selector s = getSelector();
    if (s != null) {
      return s.elementName();
    }
    return "NULL";
  }

  @Override
  protected void describeMismatchSafely(Selector item, Description mismatchDescription) {
    getInternalMatcher().describeMismatch(sample(item), mismatchDescription);
  }

  @SuppressWarnings("unchecked")
  private SelectorBasedSampler<T> getSampler(Selector item) {
    if (sampler == null || !item.equals(selector)) {
      sampler = (SelectorBasedSampler<T>)SamplerFactory.instance(
          samplerClass,
          ObjectArrays.concat(item, getSamplerParams()));
      selector = item;
    }
    return sampler;
  }

  private T sample(Selector item) {
    return getSampler(item).sampleValue();
  }

  protected Matcher<T> getInternalMatcher() {
    return internalMatcher;
  }

  protected Class<? extends SelectorBasedSampler<T>> getSamplerClass() {
    return samplerClass;
  }

  protected Object[] getSamplerParams() {
    return samplerParams;
  }

  protected Selector getSelector() {
    return selector;
  }

  @Override
  protected boolean matchesSafely(Selector item) {
    setSelector(item);
    return getInternalMatcher().matches(sample(item));
  }

  protected void setInternalMatcher(Matcher<T> internalMatcher) {
    this.internalMatcher = internalMatcher;
  }

  protected void setSamplerClass(Class<? extends SelectorBasedSampler<T>> samplerClass) {
    this.samplerClass = samplerClass;
  }

  protected void setSamplerParams(Object[] samplerParams) {
    this.samplerParams = samplerParams;
  }

  protected void setSelector(Selector selector) {
    this.selector = selector;
  }

}
