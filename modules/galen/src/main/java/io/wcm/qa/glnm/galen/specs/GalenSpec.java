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
package io.wcm.qa.glnm.galen.specs;

import java.util.Collection;

import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Galenium representation of Galen spec. Can be executed.
 *
 * @since 4.0.0
 */
public interface GalenSpec {

  /**
   * Run the spec.
   *
   * @return representation of the run
   * @since 4.0.0
   */
  GalenSpecRun check();

  /**
   * Run the spec with tags.
   *
   * @return representation of the run
   * @since 4.0.0
   * @param tags a {@link java.lang.String} object.
   */
  GalenSpecRun check(String... tags);

  /**
   * <p>
   * getObjects.
   * </p>
   *
   * @return a {@link java.util.Collection} object.
   */
  Collection<NestedSelector> getObjects();

  /**
   * <p>getName.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 4.0.0
   */
  String getName();

}
