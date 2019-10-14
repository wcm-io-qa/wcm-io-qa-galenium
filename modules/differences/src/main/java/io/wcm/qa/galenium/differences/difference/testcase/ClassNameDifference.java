/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.differences.difference.testcase;

import io.wcm.qa.galenium.differences.difference.StringDifference;

/**
 * Uses simple name of class (without package) as difference.
 *
 * @since 1.0.0
 */
public class ClassNameDifference extends StringDifference {

  /**
   * <p>Constructor for ClassNameDifference.</p>
   *
   * @param clazz to get name from
   * @since 2.0.0
   */
  public ClassNameDifference(Class clazz) {
    super(clazz.getSimpleName());
  }

}
