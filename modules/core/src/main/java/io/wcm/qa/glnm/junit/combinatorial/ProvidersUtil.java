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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

/**
 * <p>ProvidersUtil class.</p>
 *
 * @since 5.0.0
 */
public final class ProvidersUtil {

  private ProvidersUtil() {
    // do not instantiate
  }

  /**
   * <p>extractArgumentProviders.</p>
   *
   * @param context a {@link org.junit.jupiter.api.extension.ExtensionContext} object.
   * @return a {@link java.util.Collection} object.
   * @since 5.0.0
   */
  public static Collection<ArgumentsProvider> extractArgumentProviders(ExtensionContext context) {
    return extractAnnotations(context, ArgumentsSource.class, t -> s -> providerFromSource(t, s));
  }

  private static <T, A extends Annotation> Collection<T> collectFromAnnotations(
      Class<A> annotationType,
      Annotation[] declaredAnnotations,
      Function<Annotation, Function<A, T>> mappingProducer) {

    Collection<T> result = new ArrayList<T>();

    for (Annotation annotation : declaredAnnotations) {
      Function<A, T> annotationMapping = mappingProducer.apply(annotation);
      List<T> collectedFromAnnotation = ReAnnotationUtils.findRepeatableAnnotations(annotation, annotationType)
          .stream()
          .map(annotationMapping)
          .collect(Collectors.toList());
      result.addAll(collectedFromAnnotation);
    }

    return result;
  }

  private static <T, A extends Annotation> Collection<T> extractAnnotations(
      ExtensionContext extensionContext,
      Class<A> annotationType,
      Function<Annotation, Function<A, T>> mappingProducer) {
    Method testMethod = extensionContext.getRequiredTestMethod();
    Annotation[] declaredAnnotations = testMethod.getDeclaredAnnotations();
    return collectFromAnnotations(annotationType, declaredAnnotations, mappingProducer);
  }

  @SuppressWarnings("unchecked")
  private static void feedAnnotationConsumer(Annotation annotation, AnnotationConsumer consumer) {
    AnnotationConsumer annotationConsumer = consumer;
    Class<? extends Annotation> consumedAnnotationType = getConsumedAnnotationType(annotationConsumer);
    Class<? extends Annotation> originalType = annotation.annotationType();
    if (consumedAnnotationType.isAssignableFrom(originalType)) {
      annotationConsumer.accept(annotation);
    }
    else {
      Optional<? extends Annotation> optionalAnnotation = AnnotationSupport.findAnnotation(originalType, consumedAnnotationType);
      annotationConsumer.accept(optionalAnnotation.orElseThrow(new Supplier<GaleniumException>() {
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

  static ArgumentsProvider providerFromSource(Annotation original, ArgumentsSource source) {
    Class<? extends ArgumentsProvider> providerType = source.value();
    ArgumentsProvider providerInstance = ReflectionUtils.newInstance(providerType);
    if (providerInstance instanceof AnnotationConsumer<?>) {
      feedAnnotationConsumer(original, (AnnotationConsumer)providerInstance);
    }
    return providerInstance;
  }


}
