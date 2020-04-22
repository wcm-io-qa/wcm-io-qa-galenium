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
package io.wcm.qa.glnm.hamcrest.baseline;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.qa.glnm.persistence.BaselinePersistenceExtension;

@ExtendWith(BaselinePersistenceExtension.class)
class BaseliningMatcherTest {

  @Test
  void testMapWithSizeTwo() {
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put("a", "b");
    map.put("c", "d");
    MatcherAssert.assertThat(map, BaselineMatchers.aMapWithSize());
  }

  @Test
  void testMapWithSizeFour() {
    Map<Object, Object> map = new HashMap<Object, Object>();
    map.put("a", "b");
    map.put("c", "d");
    map.put("e", "f");
    map.put("g", "h");
    MatcherAssert.assertThat(map, BaselineMatchers.aMapWithSize());
  }
}
