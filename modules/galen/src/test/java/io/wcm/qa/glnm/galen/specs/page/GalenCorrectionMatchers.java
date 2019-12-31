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
package io.wcm.qa.glnm.galen.specs.page;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.CorrectionsRect.Correction;

/**
 * Matchers for tests involving {@link GalenCorrection}.
 */
public final class GalenCorrectionMatchers {

  private static final Matcher<Correction> IS_MINUS = hasProperty("type", is(CorrectionsRect.Type.MINUS));
  private static final Matcher<Correction> IS_PLUS = hasProperty("type", is(CorrectionsRect.Type.PLUS));
  private static final Matcher<Correction> NEUTRAL = allOf(
      hasProperty("value", is(0)),
      anyOf(
          IS_PLUS,
          IS_MINUS));

  private GalenCorrectionMatchers() {
    // do not instantiate
  }

  public static Matcher<GalenCorrection> isNeutral() {
    return new IsNeutral();
  }

  private static class IsNeutral extends TypeSafeMatcher<GalenCorrection> {

    @Override
    public void describeTo(Description description) {
      NEUTRAL.describeTo(description);
    }

    @Override
    protected boolean matchesSafely(GalenCorrection item) {
      return NEUTRAL.matches(item.getCorrection());
    }

  }

}
