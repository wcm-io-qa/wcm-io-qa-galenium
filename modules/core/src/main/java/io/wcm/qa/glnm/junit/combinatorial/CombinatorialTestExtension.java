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


import static io.wcm.qa.glnm.junit.combinatorial.CombinatorialUtil.extractArguments;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.params.CombinatorialTestInvocationContext.invocationContext;
import static org.junit.jupiter.params.CombinatorialTestNameFormatter.formatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.params.CombinatorialTestMethodContext;
import org.junit.jupiter.params.CombinatorialTestNameFormatter;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.Preconditions;

abstract class CombinatorialTestExtension implements TestTemplateInvocationContextProvider {

  private static final String METHOD_CONTEXT_KEY = "context";

  protected CombinatorialTestExtension() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {

    Method templateMethod = extensionContext.getRequiredTestMethod();
    String displayName = extensionContext.getDisplayName();
    CombinatorialTestMethodContext methodContext = getMethodContext(extensionContext);
    CombinatorialTestNameFormatter formatter = createNameFormatter(templateMethod, displayName);
    AtomicLong invocationCount = new AtomicLong(0);

    return provideCombinations(extensionContext)
        .map(combination -> createInvocationContext(formatter, methodContext, combination))
        .peek(invocationContext -> invocationCount.incrementAndGet())
        .onClose(() -> Preconditions.condition(invocationCount.get() > 0,
            "Configuration error: You must configure at least one multiplier for this combinatorial test"));
  }

  /** {@inheritDoc} */
  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    if (!context.getTestMethod().isPresent()) {
      return false;
    }

    Method testMethod = context.getTestMethod().get();
    if (!supportsTestMethod(testMethod)) {
      return false;
    }

    CombinatorialTestMethodContext methodContext = CombinatorialTestMethodContext.forMethod(testMethod);

    Preconditions.condition(methodContext.hasPotentiallyValidSignature(),
        () -> String.format(
            "@ParameterizedTest method [%s] declares formal parameters in an invalid order: "
                + "argument aggregators must be declared after any indexed arguments "
                + "and before any arguments resolved by another ParameterResolver.",
            testMethod.toGenericString()));

    getStore(context).put(METHOD_CONTEXT_KEY, methodContext);

    return true;
  }

  private List<List<Combinable<?>>> collectExtensions(ExtensionContext context) {
    // TODO: Auto-generated method stub
    return Collections.emptyList();
  }

  private TestTemplateInvocationContext createInvocationContext(
      CombinatorialTestNameFormatter formatter,
      CombinatorialTestMethodContext methodContext,
      Combination combination) {
    Object[] arguments = combination.arguments().get();
    Object[] consumedArguments = CombinatorialUtil.consumedArguments(arguments, methodContext);
    List<Extension> extensions = combination.extensions();
    return invocationContext(formatter, methodContext, consumedArguments, extensions);
  }

  private CombinatorialTestNameFormatter createNameFormatter(Method templateMethod, String displayName) {
    String pattern = getNamePattern(templateMethod);
    return formatter(pattern, displayName);
  }

  private CombinatorialTestMethodContext getMethodContext(ExtensionContext extensionContext) {
    return getStore(extensionContext)//
        .get(METHOD_CONTEXT_KEY, CombinatorialTestMethodContext.class);
  }

  private boolean supportsTestMethod(Method testMethod) {
    return AnnotationSupport.isAnnotated(testMethod, getAnnotationClass());
  }

  protected abstract Stream<Combination> combine(List<List<Combinable<?>>> collectedInputs);

  protected abstract Class<? extends Annotation> getAnnotationClass();

  protected abstract String getNamePattern(Method templateMethod);

  protected Stream<Combination> provideCombinations(ExtensionContext extensionContext) {
    List<List<Combinable<?>>> arguments = extractArguments(extensionContext);
    List<List<Combinable<?>>> extensions = collectExtensions(extensionContext);
    return combine(
        Stream.of(arguments, extensions)
            .flatMap(Collection::stream)
            .collect(toList()));
  }

  ExtensionContext.Store getStore(ExtensionContext context) {
    return context.getStore(Namespace.create(this.getClass(), context.getRequiredTestMethod()));
  }

}
