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
package io.wcm.qa.glnm.sampling.util;

import static org.apache.commons.lang3.reflect.ConstructorUtils.getMatchingAccessibleConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.Sampler;

/**
 * Generic sampler construction.
 *
 * @since 5.0.0
 */
public final class SamplerFactory {

  private SamplerFactory() {
    // do not instantiate
  }

  /**
   * Factory method for constructing samplers from class and
   * constructor parameters.
   *
   * @param samplerClass class of sampler
   * @param constructorParams parameters for constructor
   * @return an instance of a sampler
   */
  public static Sampler instance(
      Class<? extends Sampler> samplerClass,
      Object... constructorParams) {
    try {
      Constructor constructor = getConstructor(samplerClass, constructorParams);
      return (Sampler)constructor.newInstance(constructorParams);
    }
    catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
        | SecurityException ex) {
      throw new GaleniumException("When constructing sampler instance.", ex);
    }

  }

  private static Constructor getConstructor(Class<? extends Sampler> samplerClass, Object... constructorParams) {
    Class[] parameterTypes = Arrays.stream(constructorParams)
        .map(param -> param.getClass()).collect(Collectors.toList()).toArray(new Class[0]);
    Constructor constructor = getMatchingAccessibleConstructor(samplerClass, parameterTypes);
    if (constructor == null) {
      StringBuilder msg = new StringBuilder()
          .append("No constructor found for ")
          .append(samplerClass)
          .append(" with ")
          .append(Arrays.toString(parameterTypes));
      throw new GaleniumException(msg.toString());
    }
    return constructor;
  }
}
