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

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.SortedDifferences;
import io.wcm.qa.glnm.persistence.Persistence;
import io.wcm.qa.glnm.persistence.SamplePersistence;

/**
 * <p>Matchers class.</p>
 *
 * @since 5.0.0
 */
public final class Matchers {

  private Matchers() {
    // do not instantiate
  }

  /**
   * Matcher using persistence.
   *
   * @return string matcher working with baseline
   */
  public static DifferentiatingMatcher<String> asExpected() {
    SortedDifferences differences = new SortedDifferences();
    Matcher<String> matcher = new BaselineStringMatcher(differences);
    return MatcherUtil.differentiate(matcher, differences);
  }

  private static final class BaselineStringMatcher extends TypeSafeMatcher<String> {

    private final Differences differences;
    private final SamplePersistence<String> persistence;

    BaselineStringMatcher(Differences differences) {
      persistence = Persistence.forString(getClass());
      this.differences = differences;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("matches baseline with key '");
      description.appendText(getDifferences().getKey());
      description.appendText("': ");
      description.appendText(baseline());
    }

    private Differences getDifferences() {
      return differences;
    }

    private boolean match(String item) {
      return StringUtils.equals(baseline(), item);
    }

    private String baseline() {
      return getPersistence().reader().readSample(getDifferences());
    }

    private void persist(String item) {
      getPersistence().writer().writeSample(getDifferences(), item);
    }

    private SamplePersistence<String> getPersistence() {
      return persistence;
    }

    @Override
    protected boolean matchesSafely(String item) {
      if (match(item)) {
        return true;
      }
      persist(item);
      return false;
    }
  }

}
