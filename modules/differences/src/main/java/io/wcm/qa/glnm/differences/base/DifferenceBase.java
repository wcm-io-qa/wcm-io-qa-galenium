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

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.format.NameUtil;

/**
 * Basic functionality needed by most {@link Difference} implementations.
 */
public abstract class DifferenceBase implements Difference {

  private static final String CLASS_NAME_PART_DIFFERENCE = "difference";
  private static final int DEFAULT_MAX_TAG_LENGTH = 20;
  private int maxTagLength = DEFAULT_MAX_TAG_LENGTH;
  private String name;

  public int getMaxTagLength() {
    return maxTagLength;
  }

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

  @Override
  public String getTag() {
    return NameUtil.getSanitized(getRawTag(), getMaxTagLength());
  }

  public void setMaxTagLength(int maxTagLength) {
    this.maxTagLength = maxTagLength;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return getName() + ": '" + getTag() + "'";
  }

  protected abstract String getRawTag();

}
