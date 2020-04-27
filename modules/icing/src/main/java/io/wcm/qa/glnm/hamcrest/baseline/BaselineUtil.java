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
package io.wcm.qa.glnm.hamcrest.baseline;

import org.hamcrest.Matcher;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;

final class BaselineUtil {

  private BaselineUtil() {
    // do not instantiate
  }

  static <T> DifferentiatingMatcher<T> differentiate(Matcher<T> matcher) {
    return differentiate(matcher, new MutableDifferences());
  }

  static <T> DifferentiatingMatcher<T> differentiate(Matcher<T> matcher, Differences differences) {
    if (matcher instanceof DifferentiatingMatcher) {
      DifferentiatingMatcher<T> differentiatingMatcher = (DifferentiatingMatcher<T>)matcher;
      for (Difference difference : differences) {
        differentiatingMatcher.add(difference);
      }
      return differentiatingMatcher;
    }
    return new DifferentiatingMatcherWrapper<T>(matcher, differences);
  }

}
