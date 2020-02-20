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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.ReflectionUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;

public class ProvidersUtil {

  private ProvidersUtil() {
    // do not instantiate
  }

  public static Collection<ArgumentsProvider> extractArgumentProviders(ExtensionContext extensionContext) {
    Method testMethod = extensionContext.getRequiredTestMethod();
    Annotation[] declaredAnnotations = getDeclaredAnnotations(testMethod);
    Collection<ArgumentsProvider> result = new ArrayList<ArgumentsProvider>();
    for (Annotation annotation : declaredAnnotations) {
      List<ArgumentsProvider> collected = ReAnnotationUtils.findRepeatableAnnotations(annotation, ArgumentsSource.class)
          .stream()
          .map(source -> ProvidersUtil.providerFromSource(annotation, source))
          .collect(Collectors.toList());
      result.addAll(collected);
    }
    return result;
  }

  private static Annotation[] getDeclaredAnnotations(Method testMethod) {
    Annotation[] declaredAnnotations = testMethod.getDeclaredAnnotations();
    return declaredAnnotations;
  }

  static ArgumentsProvider providerFromSource(Annotation original, ArgumentsSource source) {
    Class<? extends ArgumentsProvider> providerType = source.value();
    ArgumentsProvider providerInstance = instantiateProvider(providerType);
    if (providerInstance instanceof AnnotationConsumer<?>) {
      feedAnnotationConsumer(original, providerInstance);
    }
    return providerInstance;
  }

  @SuppressWarnings("unchecked")
  private static void feedAnnotationConsumer(Annotation original, ArgumentsProvider providerInstance) {
    AnnotationConsumer annotationConsumer = (AnnotationConsumer)providerInstance;
    Class<? extends Annotation> consumedAnnotationType = getConsumedAnnotationType(annotationConsumer);
    Class<? extends Annotation> originalType = original.annotationType();
    if (consumedAnnotationType.isAssignableFrom(originalType)) {
      annotationConsumer.accept(original);
    }
    else {
      Optional<? extends Annotation> annotation = AnnotationSupport.findAnnotation(originalType, consumedAnnotationType);
      annotationConsumer.accept(annotation.orElseThrow(new Supplier<GaleniumException>() {
        @Override
        public GaleniumException get() {
          return new GaleniumException("No annotation found to consume for : " + annotationConsumer);
        }
      }));
    }
  }

  private static Class<? extends Annotation> getConsumedAnnotationType(AnnotationConsumer annotationConsumer) {
    Class<? extends AnnotationConsumer> consumerClass = annotationConsumer.getClass();
    Type[] interfaces = consumerClass.getGenericInterfaces();
    for (Type type : interfaces) {
      if (type instanceof ParameterizedType
          && TypeUtils.isAssignable(type, AnnotationConsumer.class)) {
        ParameterizedType consumerType = (ParameterizedType)type;
        Type consumedAnnotationType = consumerType.getActualTypeArguments()[0];
        return rawType(consumedAnnotationType);
      }
    }
    throw new GaleniumException("Did not find type of consumed annotation: " + annotationConsumer);
  }

  @SuppressWarnings("unchecked")
  private static Class<? extends Annotation> rawType(Type argumentType) {
    return (Class<? extends Annotation>)TypeUtils.getRawType(argumentType, Annotation.class);
  }

  private static ArgumentsProvider instantiateProvider(Class<? extends ArgumentsProvider> providerType) {
    return ReflectionUtils.newInstance(providerType);
  }


}
