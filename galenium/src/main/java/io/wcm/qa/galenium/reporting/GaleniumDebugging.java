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
package io.wcm.qa.galenium.reporting;


/**
 * Logging and debugging interface.
 */
public interface GaleniumDebugging {

  /**
   * @param category category to add to test in report
   */
  void assignCategory(String category);

  /**
   * Log exception with unknown status.
   * @param ex exception to add to log
   */
  void debugAndIgnoreException(Throwable ex);

  /**
   * Log exception with fail status.
   * @param ex exception to add to log
   */
  void debugException(Throwable ex);

  /**
   * Log with failed status.
   * @param msg message
   */
  void debugFailed(String msg);

  /**
   * Log with info status.
   * @param msg message
   */
  void debugInfo(String msg);

  /**
   * Log with passed status.
   * @param msg message
   */
  void debugPassed(String msg);

  /**
   * Log with skipped status.
   * @param msg message
   */
  void debugSkip(String msg);

  /**
   * Log with unknown status.
   * @param msg message
   */
  void debugUnknown(String msg);

  /**
   * Log with warning status.
   * @param msg message
   */
  void debugWarning(String msg);

  /**
   * @return whether debugging is enabled
   */
  boolean isDebugging();

}
