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
package io.wcm.qa.galenium.verification.util;

import static io.wcm.qa.galenium.util.GaleniumContext.getVerificationStrategy;

import io.wcm.qa.galenium.util.GaleniumContext;
import io.wcm.qa.galenium.verification.base.Verification;
import io.wcm.qa.galenium.verification.strategy.VerificationStrategy;

/**
 * Handles verification using the strategy from {@link io.wcm.qa.galenium.util.GaleniumContext#getVerificationStrategy()}.
 *
 * @since 1.0.0
 */
public final class Check {

  private Check() {
    // do not instantiate
  }

  /**
   * Uses {@link io.wcm.qa.galenium.verification.strategy.VerificationStrategy} from {@link io.wcm.qa.galenium.util.GaleniumContext} to handle a list of verifications.
   *
   * @param verifications to verify
   */
  public static void verify(Verification... verifications) {
    for (Verification verification : verifications) {
      getVerificationStrategy().handle(verification);
    }
  }
}
