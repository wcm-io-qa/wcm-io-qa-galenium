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

import java.util.List;

import org.hamcrest.Matcher;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;

/**
 * <p>Matchers class.</p>
 *
 * @since 5.0.0
 */
public final class BaselineMatchers {

  private BaselineMatchers() {
    // do not instantiate
  }

  /**
   * Matcher using persistence.
   *
   * @return boolean matcher working with baseline
   */
  public static Matcher<Boolean> equalsBoolean() {
    return new BaselineBooleanMatcher();
  }

  /**
   * Matcher using persistence.
   *
   * @return integer matcher working with baseline
   */
  public static Matcher<Integer> equalsInteger() {
    return new BaselineIntegerMatcher();
  }

  /**
   * Matcher using persistence.
   *
   * @return string matcher working with baseline
   */
  public static DifferentiatingMatcher<String> equalsString() {
    return new BaselineStringMatcher();
  }

  /**
   * Matcher using persistence.
   *
   * @return string list matcher working with baseline
   */
  public static Matcher<List<String>> equalsStringList() {
    return new BaselineStringListMatcher();
  }

  /**
   * <p>
   * Adds a difference to following matchers.
   * </p>
   *
   * @param <T> type matcher can handle
   * @param difference a {@link io.wcm.qa.glnm.differences.base.Difference} object.
   * @param matcher a {@link org.hamcrest.Matcher} object.
   * @return a {@link io.wcm.qa.glnm.hamcrest.DifferentiatingMatcher} object.
   */
  public static <T> DifferentiatingMatcher<T> on(Difference difference, Matcher<T> matcher) {
    DifferentiatingMatcher<T> differentiatedMatcher = MatcherUtil.differentiate(matcher);
    differentiatedMatcher.prepend(difference);
    return differentiatedMatcher;
  }

  /**
   * <p>
   * Adds a difference to following matchers.
   * </p>
   *
   * @param <T> type matcher can handle
   * @param differences will be used to differentiate the matcher
   * @param matcher a {@link org.hamcrest.Matcher} to be differentiated.
   * @return a {@link io.wcm.qa.glnm.hamcrest.DifferentiatingMatcher} object.
   */
  public static <T> DifferentiatingMatcher<T> on(Differences differences, Matcher<T> matcher) {
    return MatcherUtil.differentiate(matcher, differences);
  }

}
