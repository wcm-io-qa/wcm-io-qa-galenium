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
package org.junit.jupiter.engine.execution;

import static org.junit.platform.commons.support.ReflectionSupport.findMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

import org.junit.jupiter.api.extension.ParameterContext;
import org.openqa.selenium.WebDriver;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Bridge to use JUnit's {@link ParameterContext} base class.
 */
public final class GaleniumDriverParameterContext extends DefaultParameterContext {

  private GaleniumDriverParameterContext(Object target, String methodName) {
    super(driverSetterParam(target.getClass(), methodName), 0, Optional.of(target));
  }

  private static Parameter driverSetterParam(Class type, String methodName) {
    Optional<Method> setDriverMethod = findMethod(type, methodName, WebDriver.class);

    if (setDriverMethod.isPresent()) {
      return setDriverMethod.get().getParameters()[0];
    }

    throw new GaleniumException("No setter for driver found.");
  }

  public static ParameterContext driverParamContext(Object target, String methodName) {
    return new GaleniumDriverParameterContext(target, methodName);
  }

}
