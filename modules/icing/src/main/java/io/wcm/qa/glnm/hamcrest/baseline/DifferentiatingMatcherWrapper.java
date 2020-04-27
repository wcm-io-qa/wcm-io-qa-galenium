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

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;

/**
 * Wrapper to differentiate any {@link org.hamcrest.Matcher}.
 *
 * @param <T> type to match
 * @since 5.0.0
 */
public class DifferentiatingMatcherWrapper<T> extends TypeSafeMatcher<T> implements DifferentiatingMatcher<T> {

  private MutableDifferences differences = new MutableDifferences();

  private final Matcher<T> matcher;

  DifferentiatingMatcherWrapper(Matcher<T> matcher, Differences differences) {
    this.matcher = matcher;
    this.getDifferences().addAll(differences);
  }

  /** {@inheritDoc} */
  @Override
  public void add(Difference difference) {
    getDifferences().add(difference);
  }

  /** {@inheritDoc} */
  @Override
  public void describeMismatchSafely(T actual, Description mismatchDescription) {
    matcher.describeMismatch(actual, mismatchDescription);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("on ");
    description.appendText(StringUtils.join(getDifferences(), " and "));
    description.appendText(" ");
    matcher.describeTo(description);
  }

  /** {@inheritDoc} */
  @Override
  public void forEach(Consumer<? super Difference> action) {
    getDifferences().forEach(action);
  }

  /**
   * <p>Getter for the field <code>differences</code>.</p>
   *
   * @return a {@link io.wcm.qa.glnm.differences.generic.MutableDifferences} object.
   */
  public MutableDifferences getDifferences() {
    return differences;
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return getDifferences().getKey();
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  /** {@inheritDoc} */
  @Override
  public boolean matchesSafely(T actual) {
    return matcher.matches(actual);
  }

  /** {@inheritDoc} */
  @Override
  public void prepend(Difference difference) {
    MutableDifferences newDifferences = new MutableDifferences();
    newDifferences.add(difference);
    newDifferences.addAll(getDifferences());
    setDifferences(newDifferences);
  }

  /** {@inheritDoc} */
  @Override
  public Spliterator<Difference> spliterator() {
    return getDifferences().spliterator();
  }

  private void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }
}
