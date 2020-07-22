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
package io.wcm.qa.glnm.galen.specs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class ParsingUtilTest {

  @ParameterizedTest
  @ValueSource(
      strings = {
          "layout/test.gspec",
          "/layout/test.gspec",
          "galen/specs/layout/test.gspec",
          "/galen/specs/layout/test.gspec",
          "./target/test-classes/galen/specs/layout/test.gspec"
      })
  void testSourceLines(String specPath) {
    assertThat(
        ParsingUtil.getSourceLines(specPath), hasSize(36));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "layout/test.gspec",
          "/layout/test.gspec",
          "galen/specs/layout/test.gspec",
          "/galen/specs/layout/test.gspec",
          "./target/test-classes/galen/specs/layout/test.gspec"
      })
  void testSelectors(String specPath) {
    assertThat(
        ParsingUtil.getSelectorsFromSpec(specPath), hasSize(11));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
          "layout/test.gspec",
          "/layout/test.gspec",
          "galen/specs/layout/test.gspec",
          "/galen/specs/layout/test.gspec",
          "./target/test-classes/galen/specs/layout/test.gspec"
      })
  void testTags(String specPath) {
    assertThat(
        ParsingUtil.getTags(specPath), hasItems("entry", "exit"));
  }

}
