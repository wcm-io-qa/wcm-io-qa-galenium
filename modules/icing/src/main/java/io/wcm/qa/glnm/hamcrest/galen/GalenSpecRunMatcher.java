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

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.hamcrest.AllureAwareMatcher;

/**
 * Wraps {@link io.wcm.qa.glnm.galen.specs.GalenSpecRun} into Allure aware matcher.
 *
 * @since 5.0.0
 */
public class GalenSpecRunMatcher extends AllureAwareMatcher<GalenSpecRun> {

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("run is clean");
  }

  @Override
  protected void describeMismatchSafely(GalenSpecRun item, Description mismatchDescription) {
    mismatchDescription.appendText("[");
    mismatchDescription.appendText(StringUtils.join(item.getMessages(), "]["));
    mismatchDescription.appendText("]");
  }

  @Override
  protected boolean matchesWithReporting(GalenSpecRun item) {
    if (item.isClean()) {
      return true;
    }
    return false;
  }

  /**
   * <p>specRun.</p>
   *
   * @return matcher for {@link io.wcm.qa.glnm.galen.specs.GalenSpecRun}
   */
  public static Matcher<GalenSpecRun> specRun() {
    return new GalenSpecRunMatcher();
  }

}
