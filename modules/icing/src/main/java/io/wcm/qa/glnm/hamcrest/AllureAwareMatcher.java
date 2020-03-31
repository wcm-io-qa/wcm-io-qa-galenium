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


import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.internal.ReflectiveTypeFinder;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Type safe matching with Allure reporting
 *
 * @param <T> type to match against
 * @since 5.0.0
 */
public abstract class AllureAwareMatcher<T> extends TypeSafeMatcher<T> {

  protected AllureAwareMatcher() {
    super();
  }

  protected AllureAwareMatcher(Class expectedType) {
    super(expectedType);
  }

  protected AllureAwareMatcher(ReflectiveTypeFinder typeFinder) {
    super(typeFinder);
  }

  @Override
  protected final boolean matchesSafely(T item) {
    String description = "";
    String startStepUuid = GaleniumReportUtil.startStep(description);
    boolean matchResult = matchesWithReporting(item);
    StringDescription stringDescription = new StringDescription();
    describeTo(stringDescription);
    if (matchResult) {
      GaleniumReportUtil.updateStepName(startStepUuid, stringDescription.toString());
      GaleniumReportUtil.passStep(startStepUuid);
      GaleniumReportUtil.stopStep();
      return true;
    }
    describeMismatch(item, stringDescription);
    GaleniumReportUtil.updateStepName(startStepUuid, stringDescription.toString());
    GaleniumReportUtil.failStep(startStepUuid);
    GaleniumReportUtil.stopStep();
    return false;
  }

  protected abstract boolean matchesWithReporting(T item);

}
