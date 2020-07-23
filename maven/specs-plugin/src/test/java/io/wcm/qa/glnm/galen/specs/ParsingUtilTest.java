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

import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import io.wcm.qa.glnm.junit.combinatorial.CartesianProduct;


class ParsingUtilTest {

  @CartesianProduct
  @ValueSource(
      strings = {
          "layout/",
          "/layout/",
          "galen/specs/layout/",
          "/galen/specs/layout/",
          "./target/test-classes/galen/specs/layout/"
      })
  @CsvSource({
      "test.gspec,36",
      "importing.gspec,25",
  })
  void testSourceLines(String folderName, String specName, int lineCount) {
    String specPath = folderName + specName;
    assertThat(
        GalenParsing.getSourceLines(specPath), hasSize(lineCount));
  }

  @CartesianProduct
  @ValueSource(
      strings = {
          "layout/",
          "/layout/",
          "galen/specs/layout/",
          "/galen/specs/layout/",
          "./target/test-classes/galen/specs/layout/"
      })
  @CsvSource({
      "test.gspec",
      "importing.gspec",
  })
  void testSelectors(String folderName, String specName) {
    String specPath = folderName + specName;
    assertThat(
        ParsingUtil.getSelectorsFromSpec(specPath), hasSize(11));
  }

  @CartesianProduct
  @ValueSource(
      strings = {
          "layout/",
          "/layout/",
          "galen/specs/layout/",
          "/galen/specs/layout/",
          "./target/test-classes/galen/specs/layout/"
      })
  @CsvSource({
      "test.gspec",
      "importing.gspec",
  })
  void testTags(String folderName, String specName) {
    String specPath = folderName + specName;
    assertThat(
        ParsingUtil.getTags(specPath), hasItems("entry", "exit"));
  }

}
