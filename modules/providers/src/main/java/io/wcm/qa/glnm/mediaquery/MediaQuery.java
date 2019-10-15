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
package io.wcm.qa.glnm.mediaquery;

/**
 * Media Queries or Break Points are a concept in responsive design and define different behaviors for different page
 * widths.
 *
 * @since 1.0.0
 */
public interface MediaQuery {

  /**
   * <p>getLowerBound.</p>
   *
   * @return lower bound in pixels
   * @since 3.0.0
   */
  int getLowerBound();

  /**
   * <p>getName.</p>
   *
   * @return name of media query
   * @since 3.0.0
   */
  String getName();

  /**
   * <p>getUpperBound.</p>
   *
   * @return upper bound in pixels
   * @since 3.0.0
   */
  int getUpperBound();

}
