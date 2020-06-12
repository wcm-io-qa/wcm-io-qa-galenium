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
package io.wcm.qa.glnm.junit.combinatorial;

import static com.google.common.collect.Lists.cartesianProduct;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.Preconditions;

class CartesianProductProvider extends CombinatorialTestExtension {

  @Override
  protected Stream<Combination> combine(List<List<Combinable>> collectedInputs) {
    return cartesianProduct(collectedInputs)
        .stream()
        .map(Combination::new);
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

}
