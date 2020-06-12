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

import static com.google.common.collect.Lists.transform;
import static io.wcm.qa.glnm.junit.combinatorial.ReAnnotationUtils.findRepeatableAnnotations;
import static java.util.stream.Collectors.toList;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.CombinatorialTestMethodContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.ReflectionUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;

final class CombinatorialUtil {

  private CombinatorialUtil() {
    // do not instantiate
  }

  /**
   * <p>
   * extractArgumentProviders.
   * </p>
   *
   * @param context a {@link org.junit.jupiter.api.extension.ExtensionContext} object.
   * @return a list of {@link ArgumentsProvider ArgumentsProviders}
   * @since 5.0.0
   */
  public static List<ArgumentsProvider> extractArgumentProviders(ExtensionContext context) {
    return extractFromAnnotations(context, ArgumentsSource.class, t -> s -> providerFromSource(t, s));
  }

  private static <T, A extends Annotation> List<T> collectFromAnnotations(
      Class<A> annotationType,
      Annotation[] declaredAnnotations,
      Function<Annotation, Function<A, T>> mappingProducer) {

    List<T> result = new ArrayList<T>();

    for (Annotation annotation : declaredAnnotations) {
      List<T> collectedFromAnnotation = findRepeatableAnnotations(annotation, annotationType)
          .stream()
          .map(mappingProducer.apply(annotation))
          .filter(ObjectUtils::allNotNull)
          .collect(toList());
      result.addAll(collectedFromAnnotation);
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private static CombinableProvider extensionFromSource(Annotation original, CombinableSource source) {
    CombinableProvider providerInstance = ReflectionUtils.newInstance(source.value());
    if (providerInstance instanceof AnnotationConsumer<?>) {
      feedAnnotationConsumer(original, (AnnotationConsumer)providerInstance);
    }
    if (providerInstance.providedType().isAssignableFrom(Extension.class)) {
      return providerInstance;
    }
    return null;
  }

  private static <T, A extends Annotation> List<T> extractFromAnnotations(
      ExtensionContext extensionContext,
      Class<A> annotationType,
      Function<Annotation, Function<A, T>> mappingProducer) {
    Method testMethod = extensionContext.getRequiredTestMethod();
    Annotation[] declaredAnnotations = testMethod.getDeclaredAnnotations();
    return collectFromAnnotations(annotationType, declaredAnnotations, mappingProducer);
  }

  @SuppressWarnings("unchecked")
  private static void feedAnnotationConsumer(Annotation annotation, AnnotationConsumer consumer) {
    Class<? extends Annotation> consumedType = getConsumedAnnotationType(consumer);
    Class<? extends Annotation> originalType = annotation.annotationType();
    if (consumedType.isAssignableFrom(originalType)) {
      consumer.accept(annotation);
    }
    else {
      Optional<? extends Annotation> optionalAnnotation = findAnnotation(originalType, consumedType);
      consumer.accept(optionalAnnotation.orElseThrow(new Supplier<GaleniumException>() {

        @Override
        public GaleniumException get() {
          return new GaleniumException("No annotation found to consume for : " + consumer);
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

  private static List<List<Combinable>> providersToArguments(
      List<ArgumentsProvider> providers,
      ExtensionContext context) {
    return transform(
        providers,
        p -> arguments(p, context));
  }

  @SuppressWarnings("unchecked")
  private static Class<? extends Annotation> rawType(Type argumentType) {
    return (Class<? extends Annotation>)TypeUtils.getRawType(argumentType, Annotation.class);
  }

  static List<Combinable> arguments(
      ArgumentsProvider provider,
      ExtensionContext context) {
    try {
      return provider.provideArguments(context)
          .map(Combinable::new)
          .collect(toList());
    }
    catch (Exception e) {
      throw ExceptionUtils.throwAsUncheckedException(e);
    }
  }

  static Object[] consumedArguments(
      Object[] arguments,
      CombinatorialTestMethodContext methodContext) {
    int parameterCount = methodContext.getParameterCount();
    return methodContext.hasAggregator() ? arguments
        : (arguments.length > parameterCount ? Arrays.copyOf(arguments, parameterCount) : arguments);
  }

  static List<List<Combinable>> extractArguments(ExtensionContext context) {
    return providersToArguments(extractArgumentProviders(context), context);
  }

  static List<CombinableProvider> extractExtensionSources(ExtensionContext context) {
    return extractFromAnnotations(context, CombinableSource.class, t -> s -> extensionFromSource(t, s));
  }

  static <T> List<T> filter(Class<T> type, List<Combinable> values) {
    return values.stream()
        .map(Combinable::getValue)
        .filter(v -> type.isInstance(v))
        .map(v -> type.cast(v))
        .collect(toList());
  }

  static Arguments flattenArgumentsList(List<Arguments> args) {
    return Arguments.of(listToArray(args));
  }

  static Object[] listToArray(List<Arguments> list) {
    Object[] listAsSingleArray = new Object[] {};
    for (Arguments args : list) {
      listAsSingleArray = ArrayUtils.addAll(listAsSingleArray, args.get());
    }
    return listAsSingleArray;
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
