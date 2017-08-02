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
import org.testng.annotations.DataProvider;

/**
 * Utility class to help with writing {@link DataProvider} code.
 */
final public class TestNgProviderUtil {

  private TestNgProviderUtil() {
    // do not instantiate
  }

  /**
   * Transforms argument list into Object array. For use with DataProviders for single argument methods and
   * constructors.
   * @param argumentList iterable to turn into object array
   * @return a two dimensional object array containing the arguments
   */
  public static Object[][] combine(Iterable argumentList) {
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Object object : argumentList) {
      combinedArguments.add(new Object[] { object });
    }
    return combinedArguments.toArray(new Object[combinedArguments.size()][]);
  }

  /**
   * Combines argument lists into Object array. Each {@link Iterable} will be used to provide the argument corresponding
   * to its position.
   * @param argumentLists iterables to combine
   * @return a two dimensional object array containing the Cartesian product of the iterable arguments
   */
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
