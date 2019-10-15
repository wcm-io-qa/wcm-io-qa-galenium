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
package io.wcm.qa.glnm.differences.difference;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.DifferenceBase;

/**
 * Simple  {@link io.wcm.qa.glnm.differences.base.Difference} using the string assigned in constructor.
 *
 * @since 1.0.0
 */
public class StringDifference extends DifferenceBase {

  private String tag;

  /**
   * <p>Constructor for StringDifference.</p>
   *
   * @param tag to use
   * @since 2.0.0
   */
  public StringDifference(String tag) {
    setTag(tag);
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    return super.getName() + "." + getTag();
  }

  @Override
  protected String getRawTag() {
    return tag;
  }

  protected void setTag(String tag) {
    this.tag = tag;
  }

}
