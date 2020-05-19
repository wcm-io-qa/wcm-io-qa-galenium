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

import io.wcm.qa.glnm.sampling.jsoup.JsoupResponseCodeSampler;


/**
 * <p>ResponseCode class.</p>
 *
 * @since 5.0.0
 */
public class ResponseCode extends TypeSafeWrappingMatcher<String, Integer> {

  protected ResponseCode(Matcher<Integer> matcher) {
    super(matcher);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("response code ");
    super.describeTo(description);
  }

  @Override
  protected void describeMismatchSafely(String item, Description mismatchDescription) {
    mismatchDescription.appendText("response code for '");
    mismatchDescription.appendValue(item);
    mismatchDescription.appendText("' ");
    super.describeMismatchSafely(item, mismatchDescription);
  }

  @Override
  protected Integer map(String item) {
    return new JsoupResponseCodeSampler(item).sampleValue();
  }

  /**
   * <p>responseCode.</p>
   *
   * @param matcher a {@link org.hamcrest.Matcher} object.
   * @return a {@link org.hamcrest.Matcher} object.
   */
  public static Matcher<String> responseCode(Matcher<Integer> matcher) {
    return new ResponseCode(matcher);
  }
}
