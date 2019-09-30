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
package io.wcm.qa.glnm.providers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.testng.annotations.DataProvider;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.sampling.Sampler;

/**
 * Utility class to help with writing {@link DataProvider} code.
 */
public final class TestNgProviderUtil {

  private TestNgProviderUtil() {
    // do not instantiate
  }

  /**
   * Read string parameters from UTF-8 input file. Each line becomes one parameter.
   * @param input UTF-8 encoded text file to read from
   * @return array ready to use in {@link DataProvider}
   */
  public static Object[][] fromFile(File input) {
    return fromFile(input, StandardCharsets.UTF_8);
  }

  /**
   * Read string parameters from input file. Each line becomes one parameter.
   * @param input text file to read from
   * @param charset encoding of file
   * @return array ready to use in {@link DataProvider}
   */
  public static Object[][] fromFile(File input, Charset charset) {

    GaleniumReportUtil.getLogger().debug("data providing from: " + input);

    // null check
    if (input == null) {
      throw new GaleniumException("input file is null when data providing.");
    }

    // existence check
    if (!input.isFile()) {
      throw new GaleniumException("not a file: " + input);
    }

    try {
      List<String> lines = FileUtils.readLines(input, charset);
      return combine(lines);
    }
    catch (IOException ex) {
      throw new GaleniumException("could not read file: " + input);
    }
  }

  /**
   * @param sampler to generate arguments
   * @return arguments based on sampler result
   */
  public static Object[][] fromSampler(Sampler<Iterable> sampler) {
    return combine(sampler.sampleValue());
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
   * Combines argument lists into Object array. Each {@link Iterable} will be used to provide the argument corresponding
   * to its position.
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
