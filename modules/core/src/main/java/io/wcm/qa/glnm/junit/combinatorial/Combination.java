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

import static io.wcm.qa.glnm.junit.combinatorial.CombinatorialUtil.filter;
import static io.wcm.qa.glnm.junit.combinatorial.CombinatorialUtil.flattenArgumentsList;

import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.params.provider.Arguments;

class Combination {

  private final List<Combinable<?>> values;

  Combination(List<Combinable<?>> values) {
    this.values = values;
  }

  private List<Combinable<?>> values() {
    return values;
  }

  Arguments arguments() {
    return flattenArgumentsList(
        filter(Arguments.class, values));
  }

  List<Extension> extensions() {
    return filter(Extension.class, values());
  }
}
