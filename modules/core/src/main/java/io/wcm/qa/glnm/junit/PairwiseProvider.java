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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.commons.util.Preconditions;

import io.wcm.qa.glnm.pairwise.PairwiseDefinition;


class PairwiseProvider
    extends CombinatorialParameterizedTestExtension
    implements TestTemplateInvocationContextProvider {

  /** {@inheritDoc} */
  @Override
  public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
    Collection<ArgumentsProvider> providers = ProvidersUtil.extractArgumentProviders(context);
    List<List<? extends Arguments>> collectedInputArguments = collectArguments(providers, context);
    return combinePairwise(collectedInputArguments);
  }

  private Arguments argumentsByIndex(List<List<? extends Arguments>> collectedInputArguments, int[] indexTupel) {
    List<Arguments> argumentList = new ArrayList<Arguments>();
     for (int i = 0; i < indexTupel.length; i++) {
      List<? extends Arguments> list = collectedInputArguments.get(i);
      int index = indexTupel[i];
      Arguments arguments = list.get(index);
      argumentList.add(arguments);
    }
    Arguments flattenedArgumentsList = ArgumentsUtil.flattenArgumentsList(argumentList);
    return flattenedArgumentsList;
  }

  private Stream<? extends Arguments> combinePairwise(List<List<? extends Arguments>> collectedInputArguments) {
    int[][] tupelsArray = getIndexTupelsFor(collectedInputArguments);
    ArrayList<Arguments> pairwiseArguments = new ArrayList<Arguments>();
    for (int[] indexTupel : tupelsArray) {
      Arguments argumentsByIndex = argumentsByIndex(collectedInputArguments, indexTupel);
      pairwiseArguments.add(argumentsByIndex);
    }
    return pairwiseArguments.stream();
  }

  private Integer[] getDomainSizes(List<List<? extends Arguments>> collectedInputArguments) {
    Integer[] domainSizes = collectedInputArguments.stream()
        .map(args -> args.size())
        .collect(Collectors.toList())
        .toArray(new Integer[0]);
    return domainSizes;
  }

  private int[][] getIndexTupelsFor(List<List<? extends Arguments>> collectedInputArguments) {
    Integer[] domainSizes = getDomainSizes(collectedInputArguments);
    int[][] tupelsArray = PairwiseDefinition.getTupelsFor(domainSizes);
    return tupelsArray;
  }

  @Override
  protected Class<? extends Annotation> getAnnotationClass() {
    return Pairwise.class;
  }

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

}
