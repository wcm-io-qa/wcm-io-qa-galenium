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

import org.hamcrest.Matcher;

/**
 * Delegates to {@link org.hamcrest.MatcherAssert} methods while decorating with
 * Allure reporting.
 *
 * @since 5.0.0
 */
public class MatcherAssert {

  /**
   * See {@link org.hamcrest.MatcherAssert#assertThat(String, boolean)}.
   *
   * @param reason describing assert
   * @param assertion result of assertion
   * @since 5.0.0
   */
  public void assertThat(String reason, boolean assertion) {
    org.hamcrest.MatcherAssert.assertThat(reason, assertion);
  }


  /**
   * See {@link org.hamcrest.MatcherAssert#assertThat(Object, Matcher)}.
   *
   * @param actual object to match
   * @param matcher doing the actual matching
   * @param <T> a T object.
   * @since 5.0.0
   */
  public <T> void assertThat(T actual, Matcher<T> matcher) {
    org.hamcrest.MatcherAssert.assertThat(actual, matcher);
  }

  /**
   * See {@link org.hamcrest.MatcherAssert#assertThat(String, Object, Matcher)}.
   *
   * @param reason describing assert
   * @param actual object to match
   * @param matcher doing the actual matching
   * @param <T> a T object.
   * @since 5.0.0
   */
  public <T> void assertThat(String reason, T actual, Matcher<T> matcher) {
    org.hamcrest.MatcherAssert.assertThat(reason, actual, matcher);
  }

}
