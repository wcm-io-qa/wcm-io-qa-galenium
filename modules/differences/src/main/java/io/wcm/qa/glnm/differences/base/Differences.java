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
package io.wcm.qa.glnm.differences.base;


/**
 * Provides Difference based keys for persisting samples and naming tests.
 *
 * @since 1.0.0
 */
public interface Differences extends Iterable<Difference> {

  /**
   * <p>
   * Key as defined by these differences.
   * </p>
   *
   * @return differences' tags concatenated with '.'
   */
  String getKey();

}
