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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.CombinatorialParameterizedTestExtension;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import com.google.common.collect.Lists;


class CartesianProductProvider extends CombinatorialParameterizedTestExtension {

  /** {@inheritDoc} */
  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    Collection<ArgumentsProvider> providers = ProvidersUtil.extractArgumentProviders(context);
    List<List<? extends Arguments>> collectedInputArguments = collectArguments(providers, context);
    List<List<Arguments>> cartesianProduct = Lists.cartesianProduct(collectedInputArguments);
    return combineProductTuplesToArguments(cartesianProduct);
  }

  private List<List<? extends Arguments>> collectArguments(Collection<ArgumentsProvider> providers, ExtensionContext context) {
    List<List<? extends Arguments>> result = new ArrayList<List<? extends Arguments>>();
    for (ArgumentsProvider provider : providers) {
      result.add(arguments(provider, context));
    }
    return result;
  }

  private static Stream<? extends Arguments> combineProductTuplesToArguments(List<List<Arguments>> cartesianProduct) {
    Collection<Arguments> result = new ArrayList<Arguments>();
    for (List<Arguments> args : cartesianProduct) {
      result.add(Arguments.of(listToArray(args)));
    }
    return result.stream();
  }

  private static Object[] listToArray(List<Arguments> list) {
    Object[] listAsSingleArray = new Object[] {};
    for (Arguments args : list) {
      listAsSingleArray = ArrayUtils.addAll(listAsSingleArray, args.get());
    }
    return listAsSingleArray;
  }

}
