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
package io.wcm.qa.glnm.hamcrest.galen;

import java.util.concurrent.ExecutionException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.galen.specs.imagecomparison.ImageComparisonSpecDefinition;
import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.hamcrest.SelectorMatcher;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matcher doing visual comparison on element defined by Selector.
 *
 * @since 5.0.0
 */
public class VisuallyCompare extends SelectorMatcher<GalenSpecRun> {

  private static final CacheLoader<Selector, GalenSpecRun> CACHE_LOADER = new CacheLoader<Selector, GalenSpecRun>() {

    @Override
    public GalenSpecRun load(Selector key) throws Exception {
      return GalenValidation.imageComparison(new ImageComparisonSpecDefinition(key));
    }
  };
  private final LoadingCache<Selector, GalenSpecRun> comparisons = CacheBuilder.newBuilder().build(CACHE_LOADER);

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    getInternalMatcher().describeTo(description);
  }

  @Override
  protected void describeMismatchSafely(Selector item, Description mismatchDescription) {
    getInternalMatcher().describeMismatch(sample(item), mismatchDescription);
  }

  @Override
  protected Matcher<GalenSpecRun> getInternalMatcher() {
    return GalenSpecRunMatcher.specRun();
  }

  @Override
  protected boolean matchesSelector(Selector item) {
    return getInternalMatcher().matches(sample(item));
  }

  @Override
  protected GalenSpecRun sample(Selector item) {
    try {
      return comparisons.get(item);
    }
    catch (ExecutionException ex) {
      throw new GaleniumException("when executing visual comparison", ex);
    }
  }

  /**
   * <p>visuallyCompare.</p>
   *
   * @return visual comparison matcher
   * @since 5.0.0
   */
  public static VisuallyCompare visuallyCompare() {
    return new VisuallyCompare();
  }
}
