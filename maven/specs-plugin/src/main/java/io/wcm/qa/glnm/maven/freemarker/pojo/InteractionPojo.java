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
package io.wcm.qa.glnm.maven.freemarker.pojo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.maven.freemarker.util.ReflectionUtil;

/**
 * Provides methods from {@link Element} class.
 */
public class InteractionPojo {

  private Class delegatee;

  /**
   * @param delegateeClass to delegate to
   */
  public InteractionPojo(Class delegateeClass) {
    setDelegatee(delegateeClass);
  }

  /**
   * @return methods pojo for use in code generation
   */
  public Collection<InteractionMethodPojo> getMethods() {
    Class delegateClass = getDelegatee();
    Collection<InteractionMethodPojo> methods = new ArrayList<InteractionMethodPojo>();

    Method[] declaredMethods = delegateClass.getDeclaredMethods();
    for (Method method : declaredMethods) {
      if (isStaticSelectorMethod(method)) {
        methods.add(new InteractionMethodPojo(method));
      }
    }

    return methods;
  }

  private static boolean hasSelectorArgument(Method method) {
    Class<?>[] parameterTypes = method.getParameterTypes();
    for (Class<?> type : parameterTypes) {
      if (ReflectionUtil.isSelector(type)) {
        return true;
      }
    }

    return false;
  }

  private static boolean isStaticSelectorMethod(Method method) {
    return ReflectionUtil.isStatic(method) && ReflectionUtil.isPublic(method) && hasSelectorArgument(method);
  }

  public Class getDelegatee() {
    return delegatee;
  }

  public void setDelegatee(Class delegatee) {
    this.delegatee = delegatee;
  }

}
