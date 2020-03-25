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

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;

final class BaselineUtil {

  private BaselineUtil() {
    // do not instantiate
  }

  static <T> DifferentiatingMatcher<T> differentiate(Matcher<T> matcher) {
    return differentiate(matcher, new MutableDifferences());
  }

  static <T> DifferentiatingMatcher<T> differentiate(Matcher<T> matcher, Differences differences) {
    if (matcher instanceof DifferentiatingMatcher) {
      DifferentiatingMatcher<T> differentiatingMatcher = (DifferentiatingMatcher<T>)matcher;
      for (Difference difference : differences) {
        differentiatingMatcher.add(difference);
      }
      return differentiatingMatcher;
    }
    return new DifferentiatingMatcherWrapper<T>(matcher, differences);
  }

  private static class DifferentiatingMatcherWrapper<T> implements DifferentiatingMatcher<T> {

    private MutableDifferences differences = new MutableDifferences();

    private final Matcher<T> matcher;

    DifferentiatingMatcherWrapper(Matcher<T> matcher, Differences differences) {
      this.matcher = matcher;
      this.getDifferences().addAll(differences);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
      matcher._dont_implement_Matcher___instead_extend_BaseMatcher_();
    }

    @Override
    public void add(Difference difference) {
      getDifferences().add(difference);
    }

    @Override
    public void describeMismatch(Object actual, Description mismatchDescription) {
      matcher.describeMismatch(actual, mismatchDescription);
    }

    @Override
    public void describeTo(Description description) {
      matcher.describeTo(description);
    }

    @Override
    public void forEach(Consumer<? super Difference> action) {
      getDifferences().forEach(action);
    }

    public MutableDifferences getDifferences() {
      return differences;
    }

    @Override
    public String getKey() {
      return getDifferences().getKey();
    }

    @Override
    public Iterator<Difference> iterator() {
      return getDifferences().iterator();
    }

    @Override
    public boolean matches(Object actual) {
      return matcher.matches(actual);
    }

    @Override
    public void prepend(Difference difference) {
      MutableDifferences newDifferences = new MutableDifferences();
      newDifferences.add(difference);
      newDifferences.addAll(getDifferences());
      setDifferences(newDifferences);
    }

    @Override
    public Spliterator<Difference> spliterator() {
      return getDifferences().spliterator();
    }

    private void setDifferences(MutableDifferences differences) {
      this.differences = differences;
    }
  }
}
