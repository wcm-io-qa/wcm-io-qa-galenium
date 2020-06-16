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
package io.wcm.qa.glnm.junit.combinatorial;

import java.util.List;

/**
 * Provides combinables.
 *
 * @since 5.0.0
 */
public interface CombinableProvider {

  /**
   * <p>combinables.</p>
   *
   * @return combinable inputs for combinatorial tests
   * @since 5.0.0
   */
  List<Combinable> combinables();

  /**
   * Used for filtering.
   *
   * @return type of provided combinable value
   * @since 5.0.0
   */
  Class providedType();
}
