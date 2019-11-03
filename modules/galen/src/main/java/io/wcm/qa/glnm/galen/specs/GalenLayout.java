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

import java.io.IOException;

import com.galenframework.api.Galen;
import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.galen.util.GalenHelperUtil;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * <p>GalenLayout class.</p>
 *
 * @since 4.0.0
 */
public final class GalenLayout {

  private GalenLayout() {
    // do not instantiate
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param spec Galen spec to check
   * @param tags a {@link java.lang.String} object.
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun check(GalenSpec spec, String... tags) {
    return check(spec, null, GalenSpecUtil.asSectionFilter(tags));
  }

  static GalenSpecRun check(
      GalenSpec spec,
      ValidationListener validationListener,
      SectionFilter tags) {
    return spec.check();
  }

  static LayoutReport check(
      String testName,
      PageSpec pageSpec,
      SectionFilter tags,
      ValidationListener validationListener) {
    try {
      LayoutReport layoutReport;
      layoutReport = Galen.checkLayout(GalenHelperUtil.getBrowser(), pageSpec, tags, validationListener);
      // Creating an object that will contain the information about the test
      GalenTestInfo test = GalenTestInfo.fromString(testName);

      // Adding layout report to the test report
      test.getReport().layout(layoutReport, testName);

      GaleniumReportUtil.addGalenResult(test);

      return layoutReport;
    }
    catch (IOException ex) {
      throw new GaleniumException("Specification check failed", ex);
    }
  }

}
