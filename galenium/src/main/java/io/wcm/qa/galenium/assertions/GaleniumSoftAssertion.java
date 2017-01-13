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
package io.wcm.qa.galenium;

import io.wcm.qa.galenium.reporting.GalenReportUtil;

import org.slf4j.Logger;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

/**
 * Soft assertion with Galenium reporting integration.
 */
public class GaleniumSoftAssertion extends SoftAssert {

  private Logger logger;

  /**
   * @param logger reporting delegate
   */
  public GaleniumSoftAssertion(Logger logger) {
    this.setLogger(logger);
  }

  @Override
  public void onAssertSuccess(IAssert<?> assertCommand) {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(GalenReportUtil.MARKER_PASS, "PASSED: {0} (actual: {1})", assertCommand.getMessage(), assertCommand.getActual());
    }
    super.onAssertSuccess(assertCommand);
  }

  @Override
  public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
    getLogger().error(GalenReportUtil.MARKER_FAIL, assertCommand.getMessage());
    super.onAssertFailure(assertCommand, ex);
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

}
