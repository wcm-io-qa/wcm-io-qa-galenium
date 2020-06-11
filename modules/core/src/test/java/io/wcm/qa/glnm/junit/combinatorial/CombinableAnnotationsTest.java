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

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import io.wcm.qa.glnm.exceptions.GaleniumException;

@Execution(ExecutionMode.CONCURRENT)
class CombinableAnnotationsTest {

  @Test
  void testDeclaredAnnotations() {
    List<Class> annotationTypes = Arrays.stream(declaredAnnotations())
        .map(Annotation::annotationType)
        .collect(toList());
    assertThat(annotationTypes,
        hasItems(
            CartesianProduct.class,
            CsvSourceXY.class,
            CsvSourceAbxCdx.class));
  }

  private Annotation[] declaredAnnotations() {
    try {
      return AnnotatedTestObject.class.getMethod("method").getDeclaredAnnotations();
    }
    catch (NoSuchMethodException | SecurityException ex) {
      throw new GaleniumException("When getting test object method.", ex);
    }
  }

  @Test
  void testGetProviders() {
    Annotation[] declaredAnnotations = declaredAnnotations();
    for (Annotation annotation : declaredAnnotations) {
      if (annotation.annotationType().isAssignableFrom(CartesianProduct.class)) {
        // only checking providers
        continue;
      }
      List<ArgumentsSource> sources = ReAnnotationUtils.findRepeatableAnnotations(annotation, ArgumentsSource.class);
      assertThat(sources, hasSize(1));
      assertThat(sources, everyItem(Matchers.instanceOf(ArgumentsSource.class)));
      ArgumentsProvider provider = ProvidersUtil.providerFromSource(annotation, sources.get(0));
      assertThat(provider, notNullValue());
    }
  }

  private static class AnnotatedTestObject {

    @CartesianProduct
    @CsvSourceXY
    @CsvSourceAbxCdx
    public void method() {
      // just annotations
    }

  }

}
