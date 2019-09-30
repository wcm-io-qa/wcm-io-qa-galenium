/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.glnm.maven.freemarker.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Convenience methods interacting with Java reflection API.
 */
public final class ReflectionUtil {

  private ReflectionUtil() {
    // do not instantiate
  }

  /**
   * @param type to check
   * @return whether the class implements {@link Selector}
   */
  public static boolean isSelector(Class<?> type) {
    return Selector.class.isAssignableFrom(type);
  }

  /**
   * @param method to check
   * @return whether the method is static
   */
  public static boolean isStatic(Method method) {
    return Modifier.isStatic(method.getModifiers());
  }

  /**
   * @param method to check
   * @return whether the method is static
   */
  public static boolean isPublic(Method method) {
    return Modifier.isPublic(method.getModifiers());
  }

}
