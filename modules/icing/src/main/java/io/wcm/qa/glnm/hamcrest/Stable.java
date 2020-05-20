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

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matches when called twice in a row with the equal arguments.
 *
 * @param <T> type of argument
 * @since 5.0.0
 */
public class Stable<T> extends TypeSafeMatcher<T> {

  private T currentValue;
  private T oldValue;

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("stable");
  }

  private void updateValues(T item) {
    setOldValue(getCurrentValue());
    setCurrentValue(item);
  }

  @Override
  protected void describeMismatchSafely(T item, Description mismatchDescription) {
    mismatchDescription.appendValue(item);
    mismatchDescription.appendText(" was not ");
    mismatchDescription.appendValue(getOldValue());
  }

  protected T getCurrentValue() {
    return currentValue;
  }

  protected T getOldValue() {
    return oldValue;
  }

  @Override
  protected boolean matchesSafely(T item) {
    updateValues(item);
    return Objects.equals(getCurrentValue(), getOldValue());
  }

  protected void setCurrentValue(T currentValue) {
    this.currentValue = currentValue;
  }

  protected void setOldValue(T oldValue) {
    this.oldValue = oldValue;
  }

  /**
   * <p>
   * Stability matcher.
   * </p>
   *
   * @param <T> type to match
   * @return a matcher matching same value twice in a row
   * @since 5.0.0
   */
  public static <T> Matcher<T> stable() {
    return new Stable<T>();
  }

}
