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
package io.wcm.qa.glnm.galen.specs;

import static io.wcm.qa.glnm.galen.specs.ProvidesSpecMatcher.providesSpec;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.galenframework.specs.page.PageSpec;


public class GalenSpecTest {

  static final PageSpec SPEC = new PageSpec();
  private static final GalenPageSpecProvider PROVIDER = new GalenPageSpecProvider() {

    @Override
    public PageSpec getPageSpec() {
      return SPEC;
    }
  };

  @Test
  public void testInitializationViaProvider() {
    GalenSpec galenSpecUnderTest = new GalenSpec();
    galenSpecUnderTest.setGalenSpecProvider(PROVIDER);
    assertThat(galenSpecUnderTest, providesSpec(SPEC));
  }

}
