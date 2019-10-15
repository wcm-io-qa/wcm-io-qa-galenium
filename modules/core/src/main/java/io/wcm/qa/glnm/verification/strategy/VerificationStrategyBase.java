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

import io.wcm.qa.glnm.verification.base.Verification;

/**
 * Convenience base class to implement a  {@link io.wcm.qa.glnm.verification.strategy.VerificationStrategy}.
 *
 * @since 1.0.0
 */
public abstract class VerificationStrategyBase implements VerificationStrategy {

  /** {@inheritDoc} */
  @Override
  public void handle(Verification verification) {
    if (verification.verify()) {
      handleSuccess(verification);
    }
    else {
      handleFailure(verification);
    }
  }

  /**
   * Handles a failed verification.
   * @param verification failed verification to handle.
   */
  protected abstract void handleFailure(Verification verification);

  /**
   * Handles a successful verification.
   * @param verification successful verification to handle
   */
  protected abstract void handleSuccess(Verification verification);

}
