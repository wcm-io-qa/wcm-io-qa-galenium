/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package org.junit.jupiter.params;

/**
 * Bridge to free JUnit's package visible ParameterizedTestNameFormatter.
 *
 * @since 5.0.0
 */
public class CombinatorialTestNameFormatter extends ParameterizedTestNameFormatter {

  protected CombinatorialTestNameFormatter(String pattern, String displayName) {
    super(pattern, displayName);
  }

  /**
   * <p>formatter.</p>
   *
   * @param pattern formatting pattern
   * @param displayName display name
   * @return new formatter
   * @since 5.0.0
   */
  public static CombinatorialTestNameFormatter formatter(
      String pattern,
      String displayName) {
    return new CombinatorialTestNameFormatter(pattern, displayName);
  }

}
