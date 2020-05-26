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

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * <p>FunctionalWrappingMatcher class.</p>
 *
 * @since 5.0.0
 */
public class FunctionalWrappingMatcher<T, M> extends TypeSafeWrappingMatcher<T, M> {

  private Function<T, M> mappingFunction;

  protected FunctionalWrappingMatcher(Function<T, M> mapping, Matcher<M> matcher) {
    super(matcher);
    setMappingFunction(mapping);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("when mapping ");
    super.describeTo(description);
  }

  @Override
  protected M map(T item) {
    return getMappingFunction().apply(item);
  }

  /**
   * <p>Getter for the field <code>mappingFunction</code>.</p>
   *
   * @return a {@link java.util.function.Function} object.
   * @since 5.0.0
   */
  public Function<T, M> getMappingFunction() {
    return mappingFunction;
  }

  /**
   * <p>Setter for the field <code>mappingFunction</code>.</p>
   *
   * @param mappingFunction a {@link java.util.function.Function} object.
   * @since 5.0.0
   */
  public void setMappingFunction(Function<T, M> mappingFunction) {
    this.mappingFunction = mappingFunction;
  }

  /**
   * Maps input to type needed and then matches.
   *
   * @param <T> input type
   * @param <M> type needed by matcher
   * @param mapping used to map input to matcher type
   * @param matcher used to do actual matching
   * @return matcher that maps and matches
   * @since 5.0.0
   */
  public static <T, M> Matcher<T> whenMapping(Function<T, M> mapping, Matcher<M> matcher) {
    return new FunctionalWrappingMatcher<T, M>(mapping, matcher);
  }

}
