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

import static io.wcm.qa.glnm.hamcrest.Matchers.asExpected;
import static io.wcm.qa.glnm.hamcrest.Matchers.dependingOn;

import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.junit.CartesianProduct;
import io.wcm.qa.glnm.junit.CsvSourceAbxCdx;
import io.wcm.qa.glnm.junit.CsvSourceXY;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;


class MatchersTest {

  @CartesianProduct
  @CsvSourceAbxCdx
  @CsvSourceXY
  void testPersistingStringMatcher(String a, String b, String c, String x) {
    GaleniumAssert.assertString(new FixedStringSampler(x),
        dependingOn(new StringDifference(a),
            dependingOn(new StringDifference(b),
                dependingOn(new StringDifference(c),
                    dependingOn(new StringDifference(x),
                        asExpected())))));
  }

}
