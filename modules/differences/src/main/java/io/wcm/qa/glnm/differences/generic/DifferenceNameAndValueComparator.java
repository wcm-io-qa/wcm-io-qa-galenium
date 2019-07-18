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
package io.wcm.qa.glnm.differences.generic;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.differences.base.Difference;

/**
 * Compares difference name and tag.
 */
public class DifferenceNameAndValueComparator extends DifferenceNameComparator {

  @Override
  public int compare(Difference arg0, Difference arg1) {
    int nameComparison = super.compare(arg0, arg1);
    if (nameComparison != 0) {
      return nameComparison;
    }
    return StringUtils.compare(arg0.getTag(), arg0.getTag());
  }

}
