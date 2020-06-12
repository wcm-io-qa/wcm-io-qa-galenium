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

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

/**
 * Bridge to free JUnit's package visible ParameterizedTestInvocationContext.
 *
 * @since 5.0.0
 */
public class CombinatorialTestInvocationContext
    extends ParameterizedTestInvocationContext {

  private final List<Extension> extensions;

  protected CombinatorialTestInvocationContext(
      CombinatorialTestNameFormatter formatter,
      CombinatorialTestMethodContext methodContext,
      Object[] arguments, List<Extension> extensions) {
    super(formatter, methodContext, arguments);
    this.extensions = extensions;
  }

  /** {@inheritDoc} */
  @Override
  public List<Extension> getAdditionalExtensions() {
    List<Extension> superExtensions = super.getAdditionalExtensions();
    ArrayList<Extension> combined = newArrayList(superExtensions);
    combined.addAll(extensions);
    return combined;
  }

  /**
   * <p>invocationContext.</p>
   *
   * @param formatter name formatter
   * @param methodContext context of method
   * @param arguments to use for this invocation
   * @return new context
   * @param extensions a {@link java.util.List} object.
   * @since 5.0.0
   */
  public static TestTemplateInvocationContext invocationContext(
      CombinatorialTestNameFormatter formatter,
      CombinatorialTestMethodContext methodContext,
      Object[] arguments,
      List<Extension> extensions) {
    return new CombinatorialTestInvocationContext(formatter, methodContext, arguments, extensions);
  }
}
