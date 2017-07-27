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
package io.wcm.qa.galenium.providers;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;

final public class TestNgProviderUtil {

  private TestNgProviderUtil() {
    // do not instantiate
  }

  public static Object[][] combine(Iterable argumentLists) {
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Object object : argumentLists) {
      combinedArguments.add(new Object[] { object });
    }
    return combinedArguments.toArray(new Object[combinedArguments.size()][]);
  }

  public static Object[][] combine(Iterable... argumentLists) {
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Iterable argumentList : argumentLists) {
      combinedArguments = addArguments(combinedArguments, argumentList);
    }

    return combinedArguments.toArray(new Object[combinedArguments.size()][]);
  }

  private static Collection<Object[]> addArguments(Collection<Object[]> initialArguments, Iterable newArguments) {
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Object newArgument : newArguments) {
      for (Object[] objects : initialArguments) {
        combinedArguments.add(ArrayUtils.add(objects, newArgument));
      }
    }

    return combinedArguments;
  }

}
