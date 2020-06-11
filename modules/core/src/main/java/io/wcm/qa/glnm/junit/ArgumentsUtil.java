/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.junit;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.platform.commons.util.ExceptionUtils;

final class ArgumentsUtil {

  private ArgumentsUtil() {
    // do not instantiate
  }

  static Object[] listToArray(List<Arguments> list) {
    Object[] listAsSingleArray = new Object[] {};
    for (Arguments args : list) {
      listAsSingleArray = ArrayUtils.addAll(listAsSingleArray, args.get());
    }
    return listAsSingleArray;
  }

  static Arguments flattenArgumentsList(List<Arguments> args) {
    return Arguments.of(ArgumentsUtil.listToArray(args));
  }

  static List<? extends Arguments> arguments(ArgumentsProvider provider, ExtensionContext context) {
    try {
      return provider
          .provideArguments(context)
          .collect(Collectors.toList());
    }
    catch (Exception e) {
      throw ExceptionUtils.throwAsUncheckedException(e);
    }
  }


}
