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
package io.wcm.qa.glnm.junit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.common.collect.Lists;

public class CartesianProductProviderTest {

  @CartesianProduct
  @CsvSource({ "A, B", "C, D", "A, D" })
  @ValueSource(strings = { "X", "1" })
  @MethodSource
  void checkParameters(String a, String b, String x, String y) {
    assertThat(a, anyOf(is("A"), is("C")));
    assertThat(b, anyOf(is("B"), is("D")));
    assertThat(x, anyOf(is("X"), is("1")));
    assertThat(y, anyOf(is("X"), is("Y")));
  }

  @CartesianProduct
  @CsvSourceAbxCdx
  @MethodSource("checkParameters")
  void checkParametersWithCustomAnnotation(String a, String b, String x, String y) {
    assertThat(a, anyOf(is("A"), is("C")));
    assertThat(b, anyOf(is("B"), is("D")));
    assertThat(x, is("X"));
    assertThat(y, anyOf(is("X"), is("Y")));
  }


  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  void checkParametersWithCustomAnnotations(String a, String b, String x, String y) {
    assertThat(a, anyOf(is("A"), is("C")));
    assertThat(b, anyOf(is("B"), is("D")));
    assertThat(x, is("X"));
    assertThat(y, anyOf(is("X"), is("Y")));
  }

  static Stream<Arguments> checkParameters() {
    return Lists.newArrayList("X", "Y").stream()
        .map(arg -> Arguments.of(arg));
  }

}
