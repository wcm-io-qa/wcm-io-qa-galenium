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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.util.GaleniumContext.getVerificationStrategy;

import io.wcm.qa.galenium.verification.base.Verification;

public class VerificationUtil {

  private VerificationUtil() {
    // do not instantiate
  }

  public static void verify(Verification... verifications) {
    for (Verification verification : verifications) {
      getVerificationStrategy().handle(verification);
    }
  }
}
