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

import io.wcm.qa.glnm.galen.specs.GalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.galen.specs.imagecomparison.IcsDefinition;
import io.wcm.qa.glnm.galen.specs.imagecomparison.ImageComparisonSpec;

/**
 * Utility methods to run Galen layout checks from Selenium tests. Integration via
 * {@link io.wcm.qa.glnm.context.GaleniumContext}.
 *
 * @since 4.0.0
 */
public final class GalenValidation {

  private GalenValidation() {
    // do not instantiate
  }

  /**
   * Checks Galen spec against current state of driver.
   *
   * @param specPath path to spec file
   * @param tags a {@link java.lang.String} object.
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun check(String specPath, String... tags) {
    GalenSpec spec = readSpec(specPath, tags);
    GalenSpecRun check = spec.check(tags);
    return check;
  }

  /**
   * <p>readSpec.</p>
   *
   * @param specPath a {@link java.lang.String} object.
   * @param tags a {@link java.lang.String} object.
   * @return a {@link io.wcm.qa.glnm.galen.validation.FileBasedGalenSpec} object.
   */
  public static GalenSpec readSpec(String specPath, String... tags) {
    return new FileBasedGalenSpec(specPath, tags);
  }

  /**
   * Performs an image comparison against current state of driver.
   * Test name test name will be taken from section name of spec factory and used in reports
   *
   * @param specDefinition {@link io.wcm.qa.glnm.galen.specs.imagecomparison.IcsDefinition} to generate spec to check
   * @return report on spec test
   * @since 4.0.0
   */
  public static GalenSpecRun imageComparison(IcsDefinition specDefinition) {
    GalenSpec spec = new ImageComparisonSpec(specDefinition);
    return spec.check();
  }

}
