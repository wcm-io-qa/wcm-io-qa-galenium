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

class DomainDefinition {

  private String name;
  private int size;

  DomainDefinition(String domainName, int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("Need to supply positive size to domain: " + domainName);
    }

    setName(domainName);
    setSize(size);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getName() + "(" + getSize() + ")";
  }

  protected void setName(String name) {
    this.name = name;
  }

  protected void setSize(int size) {
    this.size = size;
  }

  String getName() {
    return name;
  }

  int getSize() {
    return size;
  }

}
