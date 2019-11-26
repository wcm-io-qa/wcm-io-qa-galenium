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
package io.wcm.qa.glnm.differences.difference;

import io.wcm.qa.glnm.differences.base.DifferenceBase;


/**
 * <p>SystemPropertyDifference class.</p>
 *
 * @since 4.0.0
 */
public class SystemPropertyDifference extends DifferenceBase {


  private String key;

  /**
   * <p>Constructor for SystemPropertyDifference.</p>
   *
   * @param propertyName a {@link java.lang.String} object.
   */
  public SystemPropertyDifference(String propertyName) {
    this.setKey(propertyName);
  }

  @Override
  protected String getRawTag() {
    return System.getProperty(getKey());
  }

  protected String getKey() {
    return key;
  }

  protected void setKey(String key) {
    this.key = key;
  }

}
