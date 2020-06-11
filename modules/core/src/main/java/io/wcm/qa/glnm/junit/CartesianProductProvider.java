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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.Preconditions;

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

  @Override
  protected Class<? extends Annotation> getAnnotationClass() {
    return CartesianProduct.class;
  }

  @Override
  protected String getNamePattern(Method templateMethod) {
    CartesianProduct parameterizedTest = AnnotationSupport.findAnnotation(templateMethod, CartesianProduct.class).get();
    String pattern = Preconditions.notBlank(parameterizedTest.name().trim(),
        () -> String.format(
            "Configuration error: @CartesianProduct on method [%s] must be declared with a non-empty name.",
            templateMethod));
    return pattern;
  }

  private static Stream<? extends Arguments> combineProductTuplesToArguments(List<List<Arguments>> cartesianProduct) {
    Collection<Arguments> result = new ArrayList<Arguments>();
    for (List<Arguments> args : cartesianProduct) {
      result.add(ArgumentsUtil.flattenArgumentsList(args));
    }
    return result.stream();
  }

}
