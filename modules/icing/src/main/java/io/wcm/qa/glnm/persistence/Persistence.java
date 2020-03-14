/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.persistence;

import java.util.List;

/**
 * Factory methods for persistence implementations.
 *
 * @since 5.0.0
 */
public final class Persistence {

  private Persistence() {
    // do not instantiate
  }

  /**
   * <p>
   * Persistence implementation for Boolean samples.
   * </p>
   *
   * @param clazz to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} for boolean samples
   * @since 5.0.0
   */
  public static SamplePersistence<Boolean> forBoolean(Class clazz) {
    return new BooleanPersistence(clazz);
  }

  /**
   * <p>
   * Persistence implementation for Integer samples.
   * </p>
   *
   * @param clazz to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} for integer samples
   * @since 5.0.0
   */
  public static SamplePersistence<Integer> forInteger(Class clazz) {
    return new IntegerPersistence(clazz);
  }

  /**
   * Persistence implementation for samples just using their toString method.
   *
   * @param clazz to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} object.
   * @since 5.0.0
   */
  public static SamplePersistence<Object> forObject(Class clazz) {
    return new ObjectPersistence(clazz);
  }

  /**
   * Persistence implementation for String samples.
   *
   * @param clazz to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} object.
   * @since 5.0.0
   */
  public static SamplePersistence<String> forString(Class clazz) {
    return new StringPersistence(clazz);
  }

  /**
   * <p>
   * Persistence implementation for String array samples.
   * </p>
   *
   * @param clazz to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} for sampled arrays of strings
   * @since 5.0.0
   */
  public static SamplePersistence<String[]> forStringArray(Class clazz) {
    return new StringArrayPersistence(clazz);
  }

  /**
   * <p>
   * Persistence implementation for String list samples.
   * </p>
   *
   * @param clazz to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} for sampled lists of strings
   * @since 5.0.0
   */
  public static SamplePersistence<List<String>> forStringList(Class clazz) {
    return new StringListPersistence(clazz);
  }

  /**
   * <p>
   * Persistence implementation for samples of dynamic type.
   * </p>
   *
   * @param sampleType to fetch and persist resource for
   * @return a {@link io.wcm.qa.glnm.persistence.SamplePersistence} for any
   *         type of object which can be stored in Apache Common PropertiesConfiguration
   * @since 5.0.0
   * @param resourceClass a {@link java.lang.Class} object.
   * @param <T> a T object.
   */
  @SuppressWarnings("unchecked")
  public static <T> SamplePersistence<T> forType(Class resourceClass, Class<? extends T> sampleType) {
    if (String.class.isAssignableFrom(sampleType)) {
      return (SamplePersistence<T>)forString(resourceClass);
    }
    if (Boolean.class.isAssignableFrom(sampleType)) {
      return (SamplePersistence<T>)forBoolean(resourceClass);
    }
    if (Integer.class.isAssignableFrom(sampleType)) {
      return (SamplePersistence<T>)forInteger(resourceClass);
    }
    if (String[].class.isAssignableFrom(sampleType)) {
      return (SamplePersistence<T>)forStringArray(resourceClass);
    }

    return (SamplePersistence<T>)forObject(sampleType);
  }

}
