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

import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.join;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.galen.specs.GalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.hamcrest.FunctionalWrappingMatcher;


/**
 * <p>GalenSpecMatcher class.</p>
 *
 * @since 5.0.0
 */
public class GalenSpecMatcher extends FunctionalWrappingMatcher<GalenSpec, GalenSpecRun> {

  private String[] tags;

  protected GalenSpecMatcher(Matcher<GalenSpecRun> matcher, String... includeTags) {
    super(item -> item.check(includeTags), matcher);
    this.setTags(includeTags);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("Galen spec ");
    if (!isAllBlank(getTags())) {
      description.appendText("[");
      description.appendText(join(getTags(), ", "));
      description.appendText("] ");
    }
    super.describeTo(description);
  }

  @Override
  protected void describeMismatchSafely(GalenSpec item, Description mismatchDescription) {
    mismatchDescription.appendText("with spec ");
    mismatchDescription.appendValue(item.getName());
    mismatchDescription.appendText(" ");
    super.describeMismatchSafely(item, mismatchDescription);
  }

  private String[] getTags() {
    return tags;
  }

  private void setTags(String[] tags) {
    this.tags = tags;
  }

  /**
   * <p>galenSpec.</p>
   *
   * @param matcher a {@link org.hamcrest.Matcher} object.
   * @param includeTags a {@link java.lang.String} object.
   * @return a {@link org.hamcrest.Matcher} object.
   * @since 5.0.0
   */
  public static Matcher<GalenSpec> galenSpec(
      Matcher<GalenSpecRun> matcher,
      String... includeTags) {
    return new GalenSpecMatcher(matcher, includeTags);
  }
}
