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
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Assertion with Galenium reporting integration.
 */
public class GaleniumAssertion extends Assertion {

  private static final Marker MARKER_ASSERTION = MarkerFactory.getMarker("galenium.assertion");

  public Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER_ASSERTION);
  }

  @Override
  public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
    super.onAssertFailure(assertCommand, ex);
  }

  @Override
  public void onAssertSuccess(IAssert<?> assertCommand) {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(GaleniumReportUtil.MARKER_PASS, "PASSED: {} (actual: {})", assertCommand.getMessage(), assertCommand.getActual());
    }
    super.onAssertSuccess(assertCommand);
  }

}
