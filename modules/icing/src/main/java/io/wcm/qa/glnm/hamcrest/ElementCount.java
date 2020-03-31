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

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.sampling.element.ElementCountSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matches how many visible elements are defined by selector.
 *
 * @since 5.0.0
 */
public class ElementCount extends SelectorSamplerMatcher<Integer> {

  /**
   * Constructor.
   *
   * @param matcher integer matcher
   * @since 5.0.0
   */
  public ElementCount(Matcher<Integer> matcher) {
    super(matcher, ElementCountSampler.class);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("number of elements for ");
    super.describeTo(description);
  }

  /**
   * How many elements defined by selector are visible
   *
   * @return matcher
   * @param matcher a {@link org.hamcrest.Matcher} object.
   * @since 5.0.0
   */
  public static Matcher<Selector> elementCount(Matcher<Integer> matcher) {
    return new ElementCount(matcher);
  }
}
