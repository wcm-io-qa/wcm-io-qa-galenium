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

import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.platform.commons.JUnitException;
import org.junit.platform.commons.util.Preconditions;
import org.junit.platform.commons.util.ReflectionUtils;

import jersey.repackaged.com.google.common.collect.Lists;


final class ReAnnotationUtils {

  private static final ConcurrentHashMap<Class<? extends Annotation>, Boolean> REPEATABLE_CONTAINER_CACHE = //
      new ConcurrentHashMap<>(16);

  private ReAnnotationUtils() {
    // do not instantiate
  }

  /**
   * @param <A> type of annotation to look for
   * @param candidate annotation to check
   * @param annotationType type to look for
   * @return annotations found on this annotation including meta
   */
  public static <A extends Annotation> List<A> findRepeatableAnnotations(
      Annotation candidate,
      Class<A> annotationType) {
    Class<? extends Annotation> containerType = getContainerType(annotationType);
    boolean inherited = isInherited(containerType);
    return findRepeatableAnnotations(candidate, annotationType, containerType, inherited);
  }

  private static <A extends Annotation> Optional<A> findAnnotation(AnnotatedElement element, Class<A> annotationType,
      boolean inherited, Set<Annotation> visited) {

    Preconditions.notNull(annotationType, "annotationType must not be null");

    if (element == null) {
      return Optional.empty();
    }

    // Directly present?
    A annotation = element.getDeclaredAnnotation(annotationType);
    if (annotation != null) {
      return Optional.of(annotation);
    }

    // Meta-present on directly present annotations?
    Optional<A> directMetaAnnotation = findMetaAnnotation(annotationType, element.getDeclaredAnnotations(),
        inherited, visited);
    if (directMetaAnnotation.isPresent()) {
      return directMetaAnnotation;
    }

    if (element instanceof Class) {
      Class<?> clazz = (Class<?>)element;

      // Search on interfaces
      for (Class<?> ifc : clazz.getInterfaces()) {
        if (ifc != Annotation.class) {
          Optional<A> annotationOnInterface = findAnnotation(ifc, annotationType, inherited, visited);
          if (annotationOnInterface.isPresent()) {
            return annotationOnInterface;
          }
        }
      }

      // Indirectly present?
      // Search in class hierarchy
      if (inherited) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class) {
          Optional<A> annotationOnSuperclass = findAnnotation(superclass, annotationType, inherited, visited);
          if (annotationOnSuperclass.isPresent()) {
            return annotationOnSuperclass;
          }
        }
      }
    }

    // Meta-present on indirectly present annotations?
    return findMetaAnnotation(annotationType, element.getAnnotations(), inherited, visited);
  }

  private static <A extends Annotation> Optional<A> findMetaAnnotation(Class<A> annotationType,
      Annotation[] candidates, boolean inherited, Set<Annotation> visited) {

    for (Annotation candidateAnnotation : candidates) {
      Class<? extends Annotation> candidateAnnotationType = candidateAnnotation.annotationType();
      if (!isInJavaLangAnnotationPackage(candidateAnnotationType) && visited.add(candidateAnnotation)) {
        Optional<A> metaAnnotation = findAnnotation(candidateAnnotationType, annotationType, inherited,
            visited);
        if (metaAnnotation.isPresent()) {
          return metaAnnotation;
        }
      }
    }
    return Optional.empty();
  }

  @SuppressWarnings("unchecked")
  private static <A extends Annotation> void findRepeatableAnnotation(Annotation candidate, Class<A> annotationType, Class<? extends Annotation> containerType,
      boolean inherited, Set<A> found, Set<Annotation> visited) {

    Class<? extends Annotation> candidateAnnotationType = candidate.annotationType();
    if (!isInJavaLangAnnotationPackage(candidateAnnotationType) && visited.add(candidate)) {
      // Exact match?
      if (candidateAnnotationType.equals(annotationType)) {
        found.add(annotationType.cast(candidate));
      }
      // Container?
      else if (candidateAnnotationType.equals(containerType)) {
        // Note: it's not a legitimate containing annotation type if it doesn't declare
        // a 'value' attribute that returns an array of the contained annotation type.
        // See https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.6.3
        Method method = ReflectionUtils.tryToGetMethod(containerType, "value").getOrThrow(
            cause -> new JUnitException(String.format(
                "Container annotation type '%s' must declare a 'value' attribute of type %s[].",
                containerType, annotationType), cause));

        Annotation[] containedAnnotations = (Annotation[])ReflectionUtils.invokeMethod(method, candidate);
        found.addAll((Collection<? extends A>)asList(containedAnnotations));
      }
      // Nested container annotation?
      else if (isRepeatableAnnotationContainer(candidateAnnotationType)) {
        Method method = ReflectionUtils.tryToGetMethod(candidateAnnotationType, "value").toOptional().get();
        Annotation[] containedAnnotations = (Annotation[])ReflectionUtils.invokeMethod(method, candidate);

        for (Annotation containedAnnotation : containedAnnotations) {
          findRepeatableAnnotations(containedAnnotation.getClass(), annotationType, containerType,
              inherited, found, visited);
        }
      }
      // Otherwise search recursively through the meta-annotation hierarchy...
      else {
        findRepeatableAnnotations(candidateAnnotationType, annotationType, containerType, inherited, found,
            visited);
      }
    }
  }

  private static <A extends Annotation> void findRepeatableAnnotations(AnnotatedElement element,
      Class<A> annotationType, Class<? extends Annotation> containerType, boolean inherited, Set<A> found,
      Set<Annotation> visited) {

    if (element instanceof Class) {
      Class<?> clazz = (Class<?>)element;

      // Recurse first in order to support top-down semantics for inherited, repeatable annotations.
      if (inherited) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && superclass != Object.class) {
          findRepeatableAnnotations(superclass, annotationType, containerType, inherited, found, visited);
        }
      }

      // Search on interfaces
      for (Class<?> ifc : clazz.getInterfaces()) {
        if (ifc != Annotation.class) {
          findRepeatableAnnotations(ifc, annotationType, containerType, inherited, found, visited);
        }
      }
    }

    // Find annotations that are directly present or meta-present on directly present annotations.
    findRepeatableAnnotations(element.getDeclaredAnnotations(), annotationType, containerType, inherited, found,
        visited);

    // Find annotations that are indirectly present or meta-present on indirectly present annotations.
    findRepeatableAnnotations(element.getAnnotations(), annotationType, containerType, inherited, found, visited);
  }

  private static <A extends Annotation> List<A> findRepeatableAnnotations(Annotation candidate, Class<A> annotationType,
      Class<? extends Annotation> containerType,
      boolean inherited) {
    Set<A> found = new LinkedHashSet<>(16);

    findRepeatableAnnotation(candidate, annotationType, containerType, inherited, found, new HashSet<>(16));
    return Lists.newArrayList(found);
  }

  private static <A extends Annotation> void findRepeatableAnnotations(Annotation[] candidates,
      Class<A> annotationType, Class<? extends Annotation> containerType, boolean inherited, Set<A> found,
      Set<Annotation> visited) {

    for (Annotation candidate : candidates) {
      findRepeatableAnnotation(candidate, annotationType, containerType, inherited, found, visited);
    }
  }

  private static <A extends Annotation> Class<? extends Annotation> getContainerType(Class<A> annotationType) {
    Preconditions.notNull(annotationType, "annotationType must not be null");
    Repeatable repeatable = annotationType.getAnnotation(Repeatable.class);
    Preconditions.notNull(repeatable, () -> annotationType.getName() + " must be @Repeatable");
    Class<? extends Annotation> containerType = repeatable.value();
    return containerType;
  }

  private static boolean isInherited(Class<? extends Annotation> containerType) {
    return containerType.isAnnotationPresent(Inherited.class);
  }


  private static boolean isInJavaLangAnnotationPackage(Class<? extends Annotation> annotationType) {
    return (annotationType != null && annotationType.getName().startsWith("java.lang.annotation"));
  }

  /**
   * Determine if the supplied annotation type is a container for a repeatable
   * annotation.
   */
  private static boolean isRepeatableAnnotationContainer(Class<? extends Annotation> candidateContainerType) {
    return REPEATABLE_CONTAINER_CACHE.computeIfAbsent(candidateContainerType, candidate -> {
      // @formatter:off
        Repeatable repeatable = Arrays.stream(candidate.getMethods())
            .filter(attribute -> attribute.getName().equals("value") && attribute.getReturnType().isArray())
            .findFirst()
            .map(attribute -> attribute.getReturnType().getComponentType().getAnnotation(Repeatable.class))
            .orElse(null);
        // @formatter:on

      return repeatable != null && candidate.equals(repeatable.value());
    });
  }

}
