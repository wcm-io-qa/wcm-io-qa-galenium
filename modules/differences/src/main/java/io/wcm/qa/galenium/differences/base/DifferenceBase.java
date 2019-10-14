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
package io.wcm.qa.galenium.differences.base;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.format.NameUtil;

/**
 * Basic functionality needed by most {@link io.wcm.qa.galenium.differences.base.Difference} implementations.
 *
 * @since 1.0.0
 */
public abstract class DifferenceBase implements Difference {

  private static final String CLASS_NAME_PART_DIFFERENCE = "difference";
  private static final int DEFAULT_MAX_TAG_LENGTH = 20;
  private int maxTagLength = DEFAULT_MAX_TAG_LENGTH;
  private String name;

  /**
   * <p>Getter for the field <code>maxTagLength</code>.</p>
   *
   * @return a int.
   */
  public int getMaxTagLength() {
    return maxTagLength;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    if (StringUtils.isBlank(name)) {
      String simpleName = getClass().getSimpleName();
      simpleName = StringUtils.removeStartIgnoreCase(simpleName, CLASS_NAME_PART_DIFFERENCE);
      simpleName = StringUtils.removeEndIgnoreCase(simpleName, CLASS_NAME_PART_DIFFERENCE);
      return simpleName;
    }
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public String getTag() {
    return NameUtil.getSanitized(getRawTag(), getMaxTagLength());
  }

  /**
   * <p>Setter for the field <code>maxTagLength</code>.</p>
   *
   * @param maxTagLength a int.
   */
  public void setMaxTagLength(int maxTagLength) {
    this.maxTagLength = maxTagLength;
  }

  /**
   * <p>Setter for the field <code>name</code>.</p>
   *
   * @param name a {@link java.lang.String} object.
   */
  public void setName(String name) {
    this.name = name;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getName() + ": '" + getTag() + "'";
  }

  protected abstract String getRawTag();

}
