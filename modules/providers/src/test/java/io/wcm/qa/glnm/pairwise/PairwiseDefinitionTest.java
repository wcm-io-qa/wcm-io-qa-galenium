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
package io.wcm.qa.glnm.pairwise;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PairwiseDefinitionTest {

  @Test
  public void testMinimalRequirements() {
    TupelDefinition simpleDef = tupelDef(1, 1);
    Collection<Requirement> reqs = PairwiseDefinition.generateRequirements(simpleDef);
    assertThat(reqs, hasSize(1));
  }

  @Test
  public void testMinimalTupel() {
    TupelDefinition simpleDef = tupelDef(1, 1);
    Collection<Tupel> tupelsFor = PairwiseDefinition.getTupelsFor(simpleDef);
    assertThat(tupelsFor, hasSize(1));
  }

  @ParameterizedTest
  @MethodSource(value = "provideRequirementCounts")
  public void testRequirementsCount(int size, DomainDefinition... domainDefinitions) {
    TupelDefinition def = tupelDef(domainDefinitions);
    Collection<Requirement> generatedRequirements = PairwiseDefinition.generateRequirements(def);
    assertThat(generatedRequirements, hasSize(size));
  }

  @ParameterizedTest
  @MethodSource(value = "provideTupelCounts")
  public void testTupelCount(int size, TupelDefinition def) {
    Collection<Tupel> tupelsFor = PairwiseDefinition.getTupelsFor(def);
    assertThat(tupelsFor, hasSize(size));
  }

  private static DomainDefinition[] domains(int... domainSizes) {
    DomainDefinition[] domains = new DomainDefinition[domainSizes.length];
    for (int i = 0; i < domainSizes.length; i++) {
      int size = domainSizes[i];
      domains[i] = new DomainDefinition("D" + i, size);
    }
    return domains;
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> provideRequirementCounts() {
    return Stream.of(
        requirementsCount(1, domains(1, 1)),
        requirementsCount(2, domains(2, 1)),
        requirementsCount(2, domains(1, 2)),
        requirementsCount(5, domains(2, 1, 1)),
        requirementsCount(5, domains(1, 2, 1)),
        requirementsCount(5, domains(1, 1, 2)),
        requirementsCount(25, domains(5, 5)),
        requirementsCount(108, domains(10, 3, 6)));
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> provideTupelCounts() {
    return Stream.of(
        Arguments.of(1, tupelDef(1, 1)),
        Arguments.of(1, tupelDef(1, 1, 1)),
        Arguments.of(4, tupelDef(2, 2)),
        Arguments.of(25, tupelDef(5, 5)),
        Arguments.of(27, tupelDef(5, 5, 2)),
        Arguments.of(31, tupelDef(5, 5, 3)),
        Arguments.of(37, tupelDef(5, 5, 4)),
        Arguments.of(45, tupelDef(5, 5, 5)),
        Arguments.of(50, tupelDef(5, 5, 6)),
        Arguments.of(22, tupelDef(10, 2, 2)),
        Arguments.of(66, tupelDef(10, 3, 6)),
        Arguments.of(10, tupelDef(10, 1))
        );
  }

  private static Arguments requirementsCount(int count, DomainDefinition[] domains) {
    return Arguments.of(count, domains);
  }

  private static TupelDefinition tupelDef(DomainDefinition[] domains) {
    return new TupelDefinition(domains);
  }

  private static TupelDefinition tupelDef(int... domains) {
    return tupelDef(domains(domains));
  }
}
