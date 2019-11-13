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

class Value {

  private int domain;
  private int value;

  Value(int domain, int index) {
    setDomain(domain);
    setValue(index);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "(" + getDomain() + ", " + getValue() + ")";
  }

  int getValue() {
    return value;
  }

  int getDomain() {
    return domain;
  }

  protected void setDomain(int domain) {
    this.domain = domain;
  }

  protected void setValue(int index) {
    this.value = index;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int hash = 13;
    hash = 31 * hash + getDomain();
    hash = 31 * hash + getValue();
    return hash;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Value)) {
      return false;
    }
    Value otherValue = (Value)obj;
    if (getDomain() != otherValue.getDomain()) {
      return false;
    }
    if (getValue() != otherValue.getValue()) {
      return false;
    }
    return true;
  }
}
