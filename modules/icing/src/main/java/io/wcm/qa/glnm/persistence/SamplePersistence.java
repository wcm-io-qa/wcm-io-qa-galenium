/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.persistence;

import io.wcm.qa.glnm.differences.base.Differences;

/**
 * <p>
 * Generic sample persistence.
 * </p>
 *
 * @since 5.0.0
 */
public interface SamplePersistence<T> {

  /**
   * <p>
   * Get sample from existing baseline.
   * </p>
   *
   * @param key identifies sample
   * @return a baseline sample
   * @since 5.0.0
   */
  T loadFromBaseline(Differences key);

  /**
   * <p>
   * Store sample to potential new baseline.
   * </p>
   *
   * @param key identifies sample
   * @param sample a fresh sample from this test run
   * @since 5.0.0
   */
  void storeToBaseline(Differences key, T sample);

}
