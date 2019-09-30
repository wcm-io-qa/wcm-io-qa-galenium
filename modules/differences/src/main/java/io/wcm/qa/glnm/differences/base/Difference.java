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
 * Encapsulates one difference impacting a sample.
 */
public interface Difference {

  /**
   * @return a descriptive name for this difference type
   */
  String getName();

  /**
   * The current value for the differing part of current difference instance.
   * @return a short, simple string representation to be used in folder names or property keys
   */
  String getTag();

}
