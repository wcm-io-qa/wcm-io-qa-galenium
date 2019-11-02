/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.galen.specs;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.galenframework.specs.page.PageSpec;

final class ProvidesSpecMatcher extends TypeSafeMatcher<GalenSpec> {

  private PageSpec spec;

  ProvidesSpecMatcher(PageSpec expected) {
    this.spec = expected;
  }

  @Override
  protected boolean matchesSafely(GalenSpec item) {
    return item.getPageSpec() == spec;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("did not provide SPEC");
  }

  static Matcher<GalenSpec> providesSpec(PageSpec expected) {
    return new ProvidesSpecMatcher(expected);
  }

}
