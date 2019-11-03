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

import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.galen.specs.FileBasedGalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenLayout;
import io.wcm.qa.glnm.galen.specs.GalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.galen.specs.IcsDefinition;
import io.wcm.qa.glnm.galen.specs.ImageComparisonSpec;

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
   * <p>getNoOpValidationListener.</p>
   *
   * @return a {@link com.galenframework.validation.ValidationListener} object.
   */
  public static ValidationListener getNoOpValidationListener() {
    return new NoOpValidationListener();
  }

  /**
   * <p>getTracingValidationListener.</p>
   *
   * @return a {@link com.galenframework.validation.ValidationListener} object.
   */
  public static ValidationListener getTracingValidationListener() {
    return new TracingValidationListener();
  }

  /**
   * Checks Galen spec against current state of driver.
   * Test name test name will be taken from section name of spec factory and used in reports
   *
   * @param specDefinition {@link io.wcm.qa.glnm.galen.specs.IcsDefinition} to generate spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun imageComparison(IcsDefinition specDefinition) {
    GalenSpec spec = new ImageComparisonSpec(specDefinition);
    return spec.check();
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
    return GalenLayout.check(spec);
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
    return GalenLayout.check(spec, tags);
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
    GalenSpec spec = new FileBasedGalenSpec(specPath);
    return GalenLayout.check(spec);
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
    GalenSpec spec = new FileBasedGalenSpec(specPath);
    return GalenLayout.check(spec, tags);
  }

}
