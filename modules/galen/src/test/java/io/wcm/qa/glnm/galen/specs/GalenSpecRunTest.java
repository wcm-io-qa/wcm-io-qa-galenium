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

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationError;
import com.galenframework.validation.ValidationResult;


public class GalenSpecRunTest {

  @Mock
  private GalenSpec spec = Mockito.mock(GalenSpec.class);

  @Test
  public void testCleanRun() {
    LayoutReport cleanReport = Mockito.mock(LayoutReport.class);
    MatcherAssert.assertThat(new GalenSpecRun(spec, cleanReport), isCleanRun());
  }

  @Test
  public void testFailedRun() {
    MatcherAssert.assertThat(new GalenSpecRun(spec, mockFailedReport()), isFailedRun());
  }

  @Test
  public void testWarningOnlyRun() {
    MatcherAssert.assertThat(new GalenSpecRun(spec, mockWarnOnlyReport()), isWarnOnlyRun());
  }

  private static Matcher<GalenSpecRun> isCleanRun() {
    return Matchers.allOf(
        Matchers.hasProperty("clean", Matchers.equalTo(true)),
        Matchers.hasProperty("warning", Matchers.equalTo(false)),
        Matchers.hasProperty("failed", Matchers.equalTo(false)));
  }

  private static Matcher<GalenSpecRun> isFailedRun() {
    return Matchers.allOf(
        Matchers.hasProperty("clean", Matchers.equalTo(false)),
        Matchers.hasProperty("warning", Matchers.equalTo(false)),
        Matchers.hasProperty("failed", Matchers.equalTo(true)));
  }

  private static Matcher<GalenSpecRun> isWarnOnlyRun() {
    return Matchers.allOf(
        Matchers.hasProperty("clean", Matchers.equalTo(false)),
        Matchers.hasProperty("warning", Matchers.equalTo(true)),
        Matchers.hasProperty("failed", Matchers.equalTo(false)));
  }

  private static ValidationError mockError() {
    ValidationError trueError = Mockito.mock(ValidationError.class);
    return trueError;
  }

  private static LayoutReport mockFailedReport() {
    ValidationError trueError = mockError();
    LayoutReport failedReport = mockLayoutReportWith(trueError);
    Mockito.when(failedReport.errors()).thenReturn(1);
    return failedReport;
  }

  private static LayoutReport mockLayoutReportWith(ValidationError... errors) {
    LayoutReport report = Mockito.mock(LayoutReport.class);
    List<ValidationResult> mockListWithErrors = mockListWith(errors);
    Mockito.when(report.getValidationErrorResults()).thenReturn(mockListWithErrors);
    return report;
  }

  private static List<ValidationResult> mockListWith(ValidationError... errors) {
    List<ValidationResult> validationList = new ArrayList<ValidationResult>();
    for (ValidationError error : errors) {
      validationList.add(mockValidationResultWith(error));
    }
    return validationList;
  }

  private static ValidationResult mockValidationResultWith(ValidationError error) {
    ValidationResult result = Mockito.mock(ValidationResult.class);
    Mockito.when(result.getError()).thenReturn(error);
    return result;
  }

  private static LayoutReport mockWarnOnlyReport() {
    ValidationError error = warnOnlyError();
    LayoutReport report = mockLayoutReportWith(error);
    Mockito.when(report.warnings()).thenReturn(1);
    return report;
  }

  private static ValidationError warnOnlyError() {
    ValidationError error = new ValidationError().withOnlyWarn(true);
    return error;
  }

}
