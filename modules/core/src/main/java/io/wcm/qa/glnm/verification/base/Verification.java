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
package io.wcm.qa.glnm.verification.base;

/**
 * Unifies different kinds of verification done while UI testing.
 *
 * @since 1.0.0
 */
public interface Verification extends Verifiable {

  /**
   * <p>getException.</p>
   *
   * @return Throwable if one occured during verification
   * @since 3.0.0
   */
  Throwable getException();

  /**
   * <p>getMessage.</p>
   *
   * @return aggregated success or failure message for this verification
   * @since 3.0.0
   */
  String getMessage();

}
