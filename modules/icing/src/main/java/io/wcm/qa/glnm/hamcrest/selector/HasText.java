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
package io.wcm.qa.glnm.hamcrest.selector;


import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.sampling.element.TextSampler;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matches if element defined by selector has a text matched by
 * underlying matcher.
 *
 * @since 5.0.0
 */
public class HasText extends SelectorSamplerMatcher<String> {

  /**
   * <p>Constructor for HasText.</p>
   *
   * @param matcher to match elements text
   * @since 5.0.0
   */
  public HasText(Matcher<String> matcher) {
    super(matcher, TextSampler.class);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("text of ");
    super.describeTo(description);
  }

  /**
   * Matches if element defined by selector has a text matched by
   * underlying matcher.
   *
   * @param matcher to match element's text
   * @return matcher retrieving text
   * @since 5.0.0
   */
  public static Matcher<Selector> hasText(Matcher<String> matcher) {
    return new HasText(matcher);
  }

}
