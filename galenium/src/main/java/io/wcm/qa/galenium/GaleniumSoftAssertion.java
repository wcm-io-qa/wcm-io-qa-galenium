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

import io.wcm.qa.galenium.reporting.GaleniumLogging;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

/**
 * Soft assertion with Galenium reporting integration.
 */
public class GaleniumSoftAssertion extends SoftAssert {

  private GaleniumLogging logging;

  /**
   * @param logging reporting delegate
   */
  public GaleniumSoftAssertion(GaleniumLogging logging) {
    this.setLogging(logging);
  }

  @Override
  public void onAssertSuccess(IAssert<?> assertCommand) {
    if (getLogging().isDebugging()) {
      StringBuilder message = new StringBuilder();
      message.append("PASSED: ");
      message.append(assertCommand.getMessage());
      message.append(" (actual: ");
      message.append(assertCommand.getActual());
      message.append(")");
      getLogging().debugPassed(message.toString());
    }
    super.onAssertSuccess(assertCommand);
  }

  @Override
  public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
    getLogging().reportFailed(assertCommand.getMessage());
    super.onAssertFailure(assertCommand, ex);
  }

  private GaleniumLogging getLogging() {
    return logging;
  }

  private void setLogging(GaleniumLogging logging) {
    this.logging = logging;
  }

}
