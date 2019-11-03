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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.galenframework.reports.model.LayoutReport;


public class GalenSpecRunTest {

  LayoutReport cleanReport = Mockito.mock(LayoutReport.class);
  private GalenSpec spec = Mockito.mock(GalenSpec.class);
  GalenSpecRun cleanRun = new GalenSpecRun(spec, cleanReport);

  @Test
  public void testWarnings() {
    MatcherAssert.assertThat(cleanRun, Matchers.hasProperty("clean", Matchers.equalTo(true)));
  }

}
