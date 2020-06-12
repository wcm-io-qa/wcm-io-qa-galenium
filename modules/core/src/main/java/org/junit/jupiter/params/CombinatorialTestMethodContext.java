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
package org.junit.jupiter.params;

import java.lang.reflect.Method;

/**
 * Bridge to free JUnit's package visible ParameterizedTestMethodContext.
 *
 * @since 5.0.0
 */
public class CombinatorialTestMethodContext extends ParameterizedTestMethodContext {

  protected CombinatorialTestMethodContext(Method testMethod) {
    super(testMethod);
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasPotentiallyValidSignature() {
    return super.hasPotentiallyValidSignature();
  }

  /** {@inheritDoc} */
  @Override
  public int getParameterCount() {
    return super.getParameterCount();
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasAggregator() {
    // TODO: Auto-generated method stub
    return super.hasAggregator();
  }

  /**
   * Factory method.
   *
   * @param testMethod to create context for
   * @return new context
   * @since 5.0.0
   */
  public static CombinatorialTestMethodContext forMethod(Method testMethod) {
    return new CombinatorialTestMethodContext(testMethod);
  }
}
