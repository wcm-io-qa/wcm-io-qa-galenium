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
package io.wcm.qa.glnm.junit.seljup;

import static io.wcm.qa.glnm.interaction.Browser.viewportSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import io.github.bonigarcia.seljup.BrowserType;
import io.wcm.qa.glnm.junit.combinatorial.CartesianProduct;


public class ViewportWidthProviderTest {

  @CartesianProduct
  @BrowserTypes(BrowserType.CHROME)
  @ViewportWidths({600, 1000, 2000})
  void testWidthsOnly() {
    assertThat(viewportSize(),
        hasProperty("width",
            anyOf(
                is(600),
                is(1000),
                is(2000))));
  }

  @CartesianProduct
  @BrowserTypes({
      BrowserType.CHROME,
      BrowserType.FIREFOX })
  @ViewportWidths({600, 1000, 2000})
  void testWidthsAndBrowsers() {
    assertThat(viewportSize(),
        hasProperty("width",
            anyOf(
                is(600),
                is(1000),
                is(2000))));
  }

  @CartesianProduct
  @BrowserTypes(BrowserType.CHROME)
  @ViewportWidths(1000)
  void testSingleWidth() {
    assertThat(viewportSize(),
        hasProperty("width",
            is(1000)));
  }

}
