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
package io.wcm.qa.glnm.exceptions;

/**
 * Wrapper exception for Galen layout problems.
 *
 * @since 1.0.0
 */
public class GalenLayoutException extends GaleniumException {

  private static final long serialVersionUID = -152759653372481359L;

  /**
   * <p>Constructor for GalenLayoutException.</p>
   *
   * @see RuntimeException
   * @param message message
   * @param ex original exception
   */
  public GalenLayoutException(String message, Throwable ex) {
    super(message, ex);
  }

}
