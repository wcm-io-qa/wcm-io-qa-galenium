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
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.wcm.qa.glnm.verification.base.Verification;


/**
 * Fail if {@link io.wcm.qa.glnm.verification.base.Verification} contains an exception.
 *
 * @since 1.0.0
 */
public class DefaultVerificationStrategy extends VerificationStrategyBase {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultVerificationStrategy.class);

  @Override
  protected void handleFailure(Verification verification) {
    Allure.step(verification.getMessage(), Status.FAILED);
    if (verification.getException() != null) {
      Assert.fail(verification.getMessage(), verification.getException());
      LOG.info(verification.getMessage(), verification.getException());
    }
    else {
      Assert.fail(verification.getMessage());
      LOG.info(verification.getMessage());
    }
  }

  @Override
  protected void handleSuccess(Verification verification) {
    Allure.step(verification.getMessage());
  }

}
