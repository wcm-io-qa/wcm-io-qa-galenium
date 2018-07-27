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
package io.wcm.qa.galenium.differences.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.differences.base.Difference;

public final class DifferenceUtil {

  private DifferenceUtil() {
    // do not instantiate
  }

  public static String joinNamesWith(Iterable<Difference> differences, String separator) {
    List<String> list = new ArrayList<String>();
    for (Difference difference : differences) {
      list.add(difference.getName());
    }
    return StringUtils.join(list, separator);
  }

  public static String getCleanTag(Difference difference) {
    String tag = difference.getTag();
    return tag.replaceAll("[^\\w\\d_]+", "_");
  }

  public static String joinTagsWith(Iterable<Difference> differences, String separator) {
    List<String> list = new ArrayList<String>();
    for (Difference difference : differences) {
      list.add(getCleanTag(difference));
    }
    return StringUtils.join(list, separator);
  }

}
