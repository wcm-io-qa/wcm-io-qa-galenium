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
 * Logging and reporting interface.
 */
public interface GaleniumReporting {

  /**
   * @param category category to add to test in report
   */
  void assignCategory(String category);

  /**
   * Report exception with failed status.
   * @param ex exception to add to log
   */
  void reportException(Throwable ex);

  /**
   * Report with failed status.
   * @param msg message
   */
  void reportFailed(String msg);

  /**
   * Report with fatal status.
   * @param msg message
   */
  void reportFatal(String msg);

  /**
   * Report exception with fatal status.
   * @param ex exception to add to log
   */
  void reportFatalException(Throwable ex);

  /**
   * Report with info status.
   * @param msg message
   */
  void reportInfo(String msg);

  /**
   * Report with passed status.
   * @param msg message
   */
  void reportPassed(String msg);

  /**
   * Report with skipped status.
   * @param msg message
   */
  void reportSkip(String msg);

  /**
   * Report with warning status.
   * @param msg message
   */
  void reportWarning(String msg);

}
