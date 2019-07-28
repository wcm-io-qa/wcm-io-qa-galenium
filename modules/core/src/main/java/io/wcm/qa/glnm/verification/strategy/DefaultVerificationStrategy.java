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

import org.slf4j.Logger;
import org.testng.Assert;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.verification.base.Verification;


/**
 * Fail if {@link Verification} contains an exception.
 */
public class DefaultVerificationStrategy extends VerificationStrategyBase {

  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  @Override
  protected void handleFailure(Verification verification) {
    if (verification.getException() != null) {
      Assert.fail(verification.getMessage(), verification.getException());
    }
    else {
      Assert.fail(verification.getMessage());
    }
  }

  @Override
  protected void handleSuccess(Verification verification) {
    getLogger().info(verification.getMessage());
  }

}
