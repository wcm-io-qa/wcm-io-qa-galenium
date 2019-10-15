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
 * Runtime exception for Galenium.
 *
 * @since 1.0.0
 */
public class GaleniumException extends RuntimeException {

  private static final long serialVersionUID = 7561233675534113771L;

  /**
   * <p>Constructor for GaleniumException.</p>
   *
   * @param msg message for exception
   * @since 3.0.0
   */
  public GaleniumException(String msg) {
    super(msg);
  }

  /**
   * <p>Constructor for GaleniumException.</p>
   *
   * @param msg message for exception
   * @param ex optional exception
   * @since 3.0.0
   */
  public GaleniumException(String msg, Throwable ex) {
    super(msg, ex);
  }

}
