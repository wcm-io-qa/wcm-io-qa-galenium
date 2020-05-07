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

import org.apache.commons.lang3.ArrayUtils;

class TupelDefinition {

  private DomainDefinition[] domains;

  TupelDefinition(DomainDefinition... domains) {
    if (ArrayUtils.isEmpty(domains)) {
      throw new IllegalArgumentException("Need to supply domain definitions to tupel definition.");
    }
    if (domains.length < 2) {
      throw new IllegalArgumentException("Need to supply at least two domain definitions to generate pairs.");
    }

    setDomains(domains);
  }

  DomainDefinition[] getDomains() {
    return domains;
  }

  void setDomains(DomainDefinition[] domains) {
    this.domains = domains;
  }

  int getIndexFor(DomainDefinition domain) {
    return ArrayUtils.indexOf(getDomains(), domain);
  }

  Tupel newTupel() {
    return new Tupel(domains.length);
  }

  Tupel newTupelFor(Requirement req) {
    Tupel tupel = newTupel();
    tupel.satisfy(req);
    return tupel;
  }
}
