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
package io.wcm.qa.glnm.galen.specs.page;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import com.galenframework.specs.page.CorrectionsRect;


public class GalenCorrectionTest {

  @Test
  public void testNeutral() {
    assertThat(GalenCorrection.neutral().toCorrection(),
        allOf(
            hasProperty("value", is(0)),
            anyOf(
                hasProperty("type", is(CorrectionsRect.Type.PLUS)),
                hasProperty("type", is(CorrectionsRect.Type.MINUS)))));
  }

  @Test
  public void testAdjustZero() {
    assertThat(GalenCorrection.adjust(0).toCorrection(),
        allOf(
            hasProperty("value", is(0)),
            anyOf(
                hasProperty("type", is(CorrectionsRect.Type.PLUS)),
                hasProperty("type", is(CorrectionsRect.Type.MINUS)))));
  }

  @Test
  public void testAdjustPlus() {
    assertThat(GalenCorrection.adjust(42).toCorrection(),
        allOf(
            hasProperty("value", is(42)),
            hasProperty("type", is(CorrectionsRect.Type.PLUS))));
  }

  @Test
  public void testAdjustMinus() {
    assertThat(GalenCorrection.adjust(-42).toCorrection(),
        allOf(
            hasProperty("value", is(42)),
            hasProperty("type", is(CorrectionsRect.Type.MINUS))));
  }
}
