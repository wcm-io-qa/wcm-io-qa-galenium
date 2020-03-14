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

import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.equalsString;
import static io.wcm.qa.glnm.hamcrest.BaselineMatchers.on;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ValueSource;

import io.wcm.qa.glnm.differences.difference.IntegerDifference;
import io.wcm.qa.glnm.differences.difference.StringDifference;
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
        on(new StringDifference(a),
            on(new StringDifference(b),
                on(new StringDifference(c),
                    on(new StringDifference(x),
                        equalsString())))));
  }

  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  @ValueSource(ints = { 1, 2, Integer.MAX_VALUE, Integer.MIN_VALUE })
  void testPersistingIntegerMatcher(String a, String b, String c, String x, Integer i) {
    MatcherAssert.assertThat(i,
        on(new StringDifference(a),
            on(new StringDifference(b),
                on(new StringDifference(c),
                    on(new StringDifference(x),
                        on(new IntegerDifference(i),
                            BaselineMatchers.equalsInteger()))))));
  }

}
