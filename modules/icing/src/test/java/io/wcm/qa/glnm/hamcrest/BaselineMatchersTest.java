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
package io.wcm.qa.glnm.hamcrest;


import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.baselineBoolean;
import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.baselineInteger;
import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.baselineString;
import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.baselineStringList;
import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.on;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ValueSource;

import io.wcm.qa.glnm.differences.difference.IntegerDifference;
import io.wcm.qa.glnm.junit.CartesianProduct;
import io.wcm.qa.glnm.junit.CsvSourceAbxCdx;
import io.wcm.qa.glnm.junit.CsvSourceXY;
import io.wcm.qa.glnm.persistence.BaselinePersistenceExtension;

@ExtendWith(BaselinePersistenceExtension.class)
class BaselineMatchersTest {

  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  void testPersistingStringMatcher(String a, String b, String c, String x) {
    MatcherAssert.assertThat(x,
        on(a, on(b, on(c, on(x, baselineString())))));
  }

  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  @ValueSource(ints = { 1, 2, Integer.MAX_VALUE, Integer.MIN_VALUE })
  void testPersistingIntegerMatcher(String a, String b, String c, String x, Integer i) {
    assertThat(i,
        on(a, on(b, on(c, on(x,
            on(new IntegerDifference(i),
                baselineInteger()))))));
  }

  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  @ValueSource(booleans = { true, false })
  void testPersistingBooleanMatcher(String a, String b, String c, String x, Boolean bool) {
    assertThat(bool,
        on(a, on(b, on(c, on(x,
            on(bool.toString(),
                baselineBoolean()))))));
  }

  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  void testPersistingStringListMatcher(String a, String b, String c, String x) {
    assertThat(
        Arrays.asList(a, b, c, x),
        on(a, on(b, on(c, on(x, baselineStringList())))));
  }

}
