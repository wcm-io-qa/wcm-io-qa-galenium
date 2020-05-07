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
package io.wcm.qa.glnm.hamcrest.baseline;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.difference.sut.SelectorDifference;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.persistence.SamplePersistence;
import io.wcm.qa.glnm.sampling.element.base.SelectorBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

class SelectorSamplerBaselineMatcher<S>
    extends BaselineMatcher<Selector, S> {

  private SelectorBasedSampler<S> sampler;
  private final Class<? extends SelectorBasedSampler<S>> samplerClass;
  private Selector selector;

  @SuppressWarnings("unchecked")
  SelectorSamplerBaselineMatcher(
      Supplier<SamplePersistence<S>> persistenceSupplier,
      Class<? extends SelectorBasedSampler<S>> baselineSamplerClass) {
    super(
        persistenceSupplier,
        item -> ((S)constructSampler(baselineSamplerClass, item).sampleValue()));
    samplerClass = baselineSamplerClass;
  }

  @SuppressWarnings("unchecked")
  private SelectorBasedSampler<S> getSampler(Selector item) {
    if (sampler == null || !item.equals(selector)) {
      sampler = constructSampler(samplerClass, item);
      selector = item;
    }
    return sampler;
  }

  private Matcher<S> internalMatcher() {
    return Matchers.is(baseline());
  }

  private S sampleFrom(Selector item) {
    return getSampler(item).sampleValue();
  }

  @Override
  protected void describeMismatchSafely(Selector item, Description mismatchDescription) {
    internalMatcher().describeMismatch(sampleFrom(item), mismatchDescription);
  }

  @Override
  protected Collection<? extends Difference> differencesFor(Selector item) {
    Collection<Difference> differences = new ArrayList<Difference>();
    differences.add(new SelectorDifference(item));
    return differences;
  }

  @Override
  protected boolean matchesBaseline(Selector item) {
    return internalMatcher().matches(sampleFrom(item));
  }

  private static SelectorBasedSampler constructSampler(
      Class<? extends SelectorBasedSampler> baselineSamplerClass,
      Selector item) {
    try {
      return baselineSamplerClass
          .getConstructor(Selector.class)
          .newInstance(item);
    }
    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
        | SecurityException ex) {
      throw new GaleniumException("When constructing sampler for baselining.", ex);
    }
  }

}
