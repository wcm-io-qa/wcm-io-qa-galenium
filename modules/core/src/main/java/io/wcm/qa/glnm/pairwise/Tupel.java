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

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

class Tupel {

  private static final int NOT_SET = -1;
  private int[] indices;

  Tupel(int size) {
    indices = new int[size];
    Arrays.fill(indices, NOT_SET);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "(" + StringUtils.join(indices, ", ") + ")";
  }

  private boolean matches(Value value) {
    return value.getValue() == getValueForDomain(value.getDomain());
  }

  int getValueForDomain(int domain) {
    return indices[domain];
  }

  boolean hasValueForDomain(int domain) {
    return getValueForDomain(domain) != NOT_SET;
  }

  boolean hasValueForDomainOf(Value value) {
    return hasValueForDomain(value.getDomain());
  }

  int[] toArray() {
    return Arrays.copyOf(indices, indices.length);
  }

  boolean isFinished() {
    return !ArrayUtils.contains(indices, NOT_SET);
  }

  boolean satisfies(Requirement req) {
    return matches(req.getValueA()) && matches(req.getValueB());
  }

  void set(Value value) {
    indices[value.getDomain()] = value.getValue();
  }

  /**
   * <p>satisfy.</p>
   *
   * @param requirement a {@link io.wcm.qa.glnm.pairwise.Requirement} object.
   * @return a boolean.
   * @since 5.0.0
   */
  public boolean satisfy(Requirement requirement) {
    if (satisfies(requirement)) {
      return true;
    }
    if (isFinished()) {
      return false;
    }
    Value valueA = requirement.getValueA();
    if (hasValueForDomainOf(valueA) && !matches(valueA)) {
      return false;
    }
    Value valueB = requirement.getValueB();
    if (hasValueForDomainOf(valueB) && !matches(valueB)) {
      return false;
    }
    set(valueA);
    set(valueB);
    return true;
  }

  void finish() {
    for (int i = 0; i < indices.length; i++) {
      if (!hasValueForDomain(i)) {
        indices[i] = 0;
      }
    }
  }

}
