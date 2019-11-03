/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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
package io.wcm.qa.glnm.galen.validation;

import io.wcm.qa.glnm.galen.specs.GalenLayout;
import io.wcm.qa.glnm.galen.specs.GalenPageSpecProvider;
import io.wcm.qa.glnm.galen.specs.GalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenSpecParsingProvider;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.galen.specs.IcValidationListener;
import io.wcm.qa.glnm.galen.specs.IcsDefinition;
import io.wcm.qa.glnm.galen.specs.ImageComparisonProvider;

/**
 * Utility methods to run Galen layout checks from Selenium tests. Integration via
 * {@link io.wcm.qa.glnm.util.GaleniumContext}.
 *
 * @since 4.0.0
 */
public final class GalenValidation {

  private GalenValidation() {
    // do not instantiate
  }

  /**
   * <p>imageComparison.</p>
   *
   * @param specDefinition {@link io.wcm.qa.glnm.galen.specs.IcsDefinition} to generate spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun imageComparison(IcsDefinition specDefinition) {
    GalenSpec spec = new GalenSpec();
    ImageComparisonProvider imageComparisonProvider = new ImageComparisonProvider(specDefinition);
    spec.setGalenSpecProvider(imageComparisonProvider);
    return GalenLayout.check(specDefinition.getSectionName(), spec, new IcValidationListener());
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from first section of spec and used in reports
   *
   * @param spec Galen spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun check(GalenSpec spec) {
    return GalenLayout.check(spec.getName(), spec);
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from first section of spec and used in reports
   *
   * @param spec Galen spec to check
   * @return report on spec test
   * @since 4.0.0
   * @param tags a {@link java.lang.String} object.
   */
  public static GalenSpecRun check(GalenSpec spec, String... tags) {
    return GalenLayout.check(spec.getName(), spec, tags);
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param testName test name used in reports
   * @param specPath path to spec file
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun check(String testName, String specPath) {
    GalenPageSpecProvider galenSpecProvider = new GalenSpecParsingProvider(specPath);
    GalenSpec spec = new GalenSpec();
    spec.setGalenSpecProvider(galenSpecProvider);
    return GalenLayout.check(testName, spec);
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param testName test name used in reports
   * @param specPath path to spec file
   * @return report on spec test
   * @since 4.0.0
   * @param tags a {@link java.lang.String} object.
   */
  public static GalenSpecRun check(String testName, String specPath, String... tags) {
    GalenPageSpecProvider galenSpecProvider = new GalenSpecParsingProvider(specPath);
    GalenSpec spec = new GalenSpec();
    spec.setGalenSpecProvider(galenSpecProvider);
    return GalenLayout.check(testName, spec, tags);
  }

}
