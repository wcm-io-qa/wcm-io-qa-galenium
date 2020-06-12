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

import static com.google.common.collect.Lists.newArrayList;
import static io.wcm.qa.glnm.pairwise.PairwiseDefinition.getTupelsFor;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.Preconditions;


class PairwiseProvider
    extends CombinatorialTestExtension
    implements TestTemplateInvocationContextProvider {

  /** {@inheritDoc} */
  @Override
  public String getNamePattern(Method templateMethod) {
    Pairwise parameterizedTest = AnnotationSupport.findAnnotation(templateMethod, Pairwise.class).get();
    String pattern = Preconditions.notBlank(parameterizedTest.name().trim(),
        () -> String.format(
            "Configuration error: @Pairwise on method [%s] must be declared with a non-empty name.",
            templateMethod));
    return pattern;
  }

  private Combination combinationFromTupel(int[] t, List<List<Combinable<?>>> collectedInputs) {
    List<Combinable<?>> combinables = newArrayList();
    for (int tupelIndex = 0; tupelIndex < t.length; tupelIndex++) {
      int inputIndex = t[tupelIndex];
      combinables.add(collectedInputs.get(tupelIndex).get(inputIndex));
    }
    return new Combination(combinables);
  }

  private Integer[] getDomainSizes(List<List<Combinable<?>>> collectedInputArguments) {
    return collectedInputArguments.stream()
        .map(args -> args.size())
        .collect(toList())
        .toArray(new Integer[0]);
  }

  private int[][] getIndexTupelsFor(List<List<Combinable<?>>> collectedInputArguments) {
    Integer[] domainSizes = getDomainSizes(collectedInputArguments);
    return getTupelsFor(domainSizes);
  }

  @Override
  protected Stream<Combination> combine(List<List<Combinable<?>>> collectedInputs) {
    int[][] tupelsArray = getIndexTupelsFor(collectedInputs);
    return Stream.of(tupelsArray)
        .map(t -> combinationFromTupel(t, collectedInputs));
  }

  @Override
  protected Class<? extends Annotation> getAnnotationClass() {
    return Pairwise.class;
  }

}
