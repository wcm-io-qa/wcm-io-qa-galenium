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
package io.wcm.qa.glnm.hamcrest.selector;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.google.common.collect.ObjectArrays;

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.sampling.element.base.SelectorBasedSampler;
import io.wcm.qa.glnm.sampling.util.SamplerFactory;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Uses underlying matcher on results of Selector based samplers.
 *
 * @param <T> sample type
 * @since 5.0.0
 */
public class SelectorSamplerMatcher<T> extends SelectorMatcher<T> {

  private Matcher<T> internalMatcher;
  private SelectorBasedSampler<T> sampler;
  private Class<? extends SelectorBasedSampler<T>> samplerClass;
  private Object[] samplerParams;
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
    describeSelector(description);
    getInternalMatcher().describeTo(description);
  }

  @SuppressWarnings("unchecked")
  private SelectorBasedSampler<T> getSampler(Selector item) {
    if (sampler == null || !item.equals(getSelector())) {
      sampler = (SelectorBasedSampler<T>)SamplerFactory.instance(
          samplerClass,
          ObjectArrays.concat(item, getSamplerParams()));
      setSelector(item);
    }
    return sampler;
  }

  @Override
  protected void describeMismatchSafely(Selector item, Description mismatchDescription) {
    if (Element.findNow(item) == null) {
      mismatchDescription.appendText("no element found for ");
      mismatchDescription.appendValue(item.elementName());
      return;
    }
    getInternalMatcher().describeMismatch(sample(item), mismatchDescription);
  }


  protected void describeSelector(Description description) {
    description.appendText(getSelectorName());
    description.appendText(" ");
  }

  @Override
  protected Matcher<T> getInternalMatcher() {
    return internalMatcher;
  }

  protected Class<? extends SelectorBasedSampler<T>> getSamplerClass() {
    return samplerClass;
  }

  protected Object[] getSamplerParams() {
    return samplerParams;
  }

  @Override
  protected boolean matchesSelector(Selector item) {
    return getInternalMatcher().matches(sample(item));
  }

  protected T sample(Selector item) {
    return getSampler(item).sampleValue();
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

}
