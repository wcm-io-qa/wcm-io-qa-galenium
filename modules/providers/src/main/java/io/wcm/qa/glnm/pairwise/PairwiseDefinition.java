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

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper to reduce test cases by applying pairwise approach.
 * Scientific background in this paper:
 * https://www.microsoft.com/en-us/research/wp-content/uploads/2017/01/160.pdf
 *
 * @since 4.0.0
 */
public final class PairwiseDefinition {

  private static final Logger LOG = LoggerFactory.getLogger(PairwiseDefinition.class);

  private PairwiseDefinition() {
    // do not instantiate
  }

  /**
   * <p>getTupelsFor.</p>
   *
   * @param def a {@link io.wcm.qa.glnm.pairwise.TupelDefinition} object.
   * @return a {@link java.util.Collection} object.
   */
  public static Collection<Tupel> getTupelsFor(TupelDefinition def) {

    Collection<Requirement> requirements = generateRequirements(def);

    Collection<Tupel> tupels = generateTupels(def, requirements);

    return tupels;
  }

  private static boolean ensureWithExistingTupels(Collection<Tupel> tupels, Requirement requirement) {
    for (Tupel tupel : tupels) {
      if (tupel.satisfy(requirement)) {
        return true;
      }
    }
    return false;
  }


  private static void finish(Collection<Tupel> tupels) {
    for (Tupel tupel : tupels) {
      if (!tupel.isFinished()) {
        tupel.finish();
      }
    }
  }

  private static Collection<Requirement> generateRequirements(int indexForDomainA, int sizeOfDomainA, int indexForDomainB, int sizeOfDomainB) {
    Collection<Requirement> requirementsAB = new ArrayList<Requirement>();
    for (int i = 0; i < sizeOfDomainA; i++) {
      Value a = new Value(indexForDomainA, i);
      for (int j = 0; j < sizeOfDomainB; j++) {
        Value b = new Value(indexForDomainB, j);
        Requirement requirement = new Requirement(a, b);
        if (LOG.isTraceEnabled()) {
          LOG.trace("requirement: " + requirement);
        }
        requirementsAB.add(requirement);
      }
    }
    return requirementsAB;
  }

  private static Collection<Tupel> generateTupels(TupelDefinition def, Collection<Requirement> requirements) {
    Collection<Tupel> tupels = new ArrayList<Tupel>();
    for (Requirement req : requirements) {
      if (!ensureWithExistingTupels(tupels, req)) {
        tupels.add(def.newTupelFor(req));
      }
    }
    finish(tupels);
    return tupels;
  }

  static Collection<Requirement> generateRequirements(TupelDefinition def) {
    Collection<Requirement> requirements = new ArrayList<Requirement>();
    DomainDefinition[] domains = def.getDomains();
    for (int i = 0; i < domains.length - 1; i++) {
      DomainDefinition domainA = domains[i];
      int indexA = def.getIndexFor(domainA);
      int sizeA = domainA.getSize();
      if (LOG.isTraceEnabled()) {
        LOG.trace("generate requirements for " + domainA);
      }
      for (int j = i + 1; j < domains.length; j++) {
        DomainDefinition domainB = domains[j];
        int indexB = def.getIndexFor(domainB);
        int sizeB = domainB.getSize();
        if (LOG.isTraceEnabled()) {
          LOG.trace("generate requirements for " + domainA + " combined with " + domainB);
        }
        requirements.addAll(generateRequirements(indexA, sizeA, indexB, sizeB));
      }
    }
    return requirements;
  }

}
