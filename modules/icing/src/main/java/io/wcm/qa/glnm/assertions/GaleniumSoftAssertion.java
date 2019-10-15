/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.glnm.assertions;

import org.slf4j.Logger;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Soft assertion with Galenium reporting integration.
 *
 * @since 1.0.0
 */
public class GaleniumSoftAssertion extends SoftAssert {

  /**
   * <p>getLogger.</p>
   *
   * @return a {@link org.slf4j.Logger} object.
   */
  public Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  /** {@inheritDoc} */
  @Override
  public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
    getLogger().error(GaleniumReportUtil.MARKER_FAIL, assertCommand.getMessage());
    super.onAssertFailure(assertCommand, ex);
  }

  /** {@inheritDoc} */
  @Override
  public void onAssertSuccess(IAssert<?> assertCommand) {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, "PASSED: {} (actual: {})", assertCommand.getMessage(), assertCommand.getActual());
    }
    super.onAssertSuccess(assertCommand);
  }

}
