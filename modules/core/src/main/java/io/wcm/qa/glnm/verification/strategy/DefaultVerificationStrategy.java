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
package io.wcm.qa.glnm.verification.strategy;

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_PASS;

import org.slf4j.Logger;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;
import io.wcm.qa.glnm.verification.base.Verification;


/**
 * Uses {@link GaleniumContext#getAssertion()} to fail if {@link Verification} contains an exception.
 */
public class DefaultVerificationStrategy extends VerificationStrategyBase {

  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  @Override
  protected void handleFailure(Verification verification) {
    if (verification.getException() != null) {
      GaleniumContext.getAssertion().fail(verification.getMessage(), verification.getException());
    }
    else {
      GaleniumContext.getAssertion().fail(verification.getMessage());
    }
  }

  @Override
  protected void handleSuccess(Verification verification) {
    getLogger().info(MARKER_PASS, verification.getMessage());
  }

}
