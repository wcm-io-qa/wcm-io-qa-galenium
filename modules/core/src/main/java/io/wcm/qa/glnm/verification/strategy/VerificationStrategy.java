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

import org.testng.asserts.Assertion;

import io.wcm.qa.glnm.verification.base.Verification;

/**
 * Handles verifications.
 */
public interface VerificationStrategy {

  /**
   * To fail test run, raise an exception or fail with {@link Assertion#fail()}.
   * @param verification to handle
   */
  void handle(Verification verification);

}
