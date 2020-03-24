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

import java.util.function.Function;
import java.util.function.Supplier;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.persistence.SamplePersistence;

abstract class BaseliningMatcher<M, S> extends BaselineMatcher<M, S> {

  private Matcher<M> internalMatcher;
  private Function<S, Matcher<M>> matcherProducer;

  protected BaseliningMatcher(
      Function<S, Matcher<M>> matcherProducer,
      Supplier<SamplePersistence<S>> persistenceSupplier,
      Function<M, S> baselineTransformer) {
    super(persistenceSupplier, baselineTransformer);
    setMatcherProducer(matcherProducer);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    getInternalMatcher().describeTo(description);
  }

  protected void setMatcherProducer(Function<S, Matcher<M>> matcherProducer) {
    this.matcherProducer = matcherProducer;
  }

  @Override
  protected void describeMismatchSafely(M item, Description mismatchDescription) {
    getInternalMatcher().describeMismatch(item, mismatchDescription);
  }

  protected Matcher<M> getInternalMatcher() {
    if (internalMatcher == null) {
      internalMatcher = produceMatcher();
    }
    return internalMatcher;
  }

  protected Function<S, Matcher<M>> getMatcherProducer() {
    return matcherProducer;
  }

  @Override
  protected boolean matchesBaseline(M item) {
    return getInternalMatcher().matches(item);
  }

  protected Matcher<M> produceMatcher() {
    return matcherProducer.apply(baseline());
  }
}
