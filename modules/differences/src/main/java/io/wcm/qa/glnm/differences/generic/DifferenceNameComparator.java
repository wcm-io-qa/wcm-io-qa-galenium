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
package io.wcm.qa.glnm.differences.generic;

import java.util.Comparator;

import io.wcm.qa.glnm.differences.base.Difference;

/**
 * Sort differences by their name.
 */
public class DifferenceNameComparator implements Comparator<Difference> {

  @Override
  public int compare(Difference arg0, Difference arg1) {

    if (arg0 == null && arg1 == null || arg0 == arg1) {
      return 0;
    }
    if (arg0 == null) {
      return -1;
    }
    if (arg1 == null) {
      return 1;
    }

    String name0 = arg0.getName();
    String name1 = arg1.getName();

    return name0.compareTo(name1);
  }

}
