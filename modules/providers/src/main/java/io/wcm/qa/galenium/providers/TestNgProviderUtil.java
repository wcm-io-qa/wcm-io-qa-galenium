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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.DataProvider;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Utility class to help with writing {@link org.testng.annotations.DataProvider} code.
 *
 * @since 1.0.0
 */
public final class TestNgProviderUtil {

  private TestNgProviderUtil() {
    // do not instantiate
  }

  /**
   * Transforms argument list into Object array. For use with DataProviders for single argument methods and
   * constructors.
   *
   * @param argumentList iterable to turn into object array
   * @return a two dimensional object array containing the arguments
   */
  public static Object[][] combine(Iterable argumentList) {
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Object object : argumentList) {
      combinedArguments.add(new Object[] { object });
    }
    int size = combinedArguments.size();
    if (size > 0) {
      trace("after combining: " + size);
    }
    else {
      trace("empty after combining.");
    }
    return combinedArguments.toArray(new Object[size][]);
  }

  /**
   * Combines argument lists into Object array. Each {@link java.lang.Iterable} will be used to provide the argument corresponding
   * to its position.
   *
   * @param argumentLists iterables to combine
   * @return a two dimensional object array containing the Cartesian product of the iterable arguments
   */
  public static Object[][] combine(Iterable... argumentLists) {
    trace("combining " + argumentLists.length + " Iterables");
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Iterable argumentList : argumentLists) {
      trace("adding: " + argumentList);
      combinedArguments = addArguments(combinedArguments, argumentList);
    }

    int size = combinedArguments.size();
    if (size > 0) {
      trace("after combining: " + size);
    }
    else {
      trace("empty after combining.");
    }
    return combinedArguments.toArray(new Object[size][]);
  }

  private static Collection<Object[]> addArguments(Collection<Object[]> initialArguments, Iterable newArguments) {
    Collection<Object[]> combinedArguments = new ArrayList<>();
    for (Object newArgument : newArguments) {
      if (initialArguments.isEmpty()) {
        combinedArguments.add(new Object[] { newArgument });
      }
      else {
        for (Object[] objects : initialArguments) {
          combinedArguments.add(ArrayUtils.add(objects, newArgument));
        }
      }
    }

    if (combinedArguments.isEmpty()) {
      trace("empty after adding.");
    }
    else {
      trace("after adding arguments: " + combinedArguments.size() + "x" + getLengthOfFirstElement(combinedArguments));
    }
    return combinedArguments;
  }

  @SuppressWarnings("deprecation")
  private static int getLengthOfFirstElement(Collection<Object[]> combinedArguments) {
    return CollectionUtils.get(combinedArguments, 0).length;
  }

  private static void trace(String msg) {
    GaleniumReportUtil.getLogger().trace(msg);
  }

}
