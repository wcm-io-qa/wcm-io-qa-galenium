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

import com.galenframework.specs.page.CorrectionsRect.Correction;
import com.galenframework.specs.page.CorrectionsRect.Type;

/**
 * <p>
 * GalenCorrection represents Galen's {@link com.galenframework.specs.page.CorrectionsRect.Correction} in the
 * Galenium context.
 * </p>
 *
 * @since 4.0.0
 */
public class GalenCorrection {

  private Type type;
  private int value;

  protected GalenCorrection(Type type, int value) {
    this.type = type;
    this.value = value;
  }

  /**
   * Ignores existing value and just overwrites it with
   * the passed parameter.
   *
   * @param value to use
   * @return a correction that sets the value
   * @since 4.0.0
   */
  public static GalenCorrection fixed(int value) {
    return new GalenCorrection(Type.EQUALS, value);
  }

  /**
   * Adjusts existing value by adding the passed parameter.
   * If the value is negative a {@link com.galenframework.specs.page.CorrectionsRect.Type#MINUS} correction
   * will be generated.
   *
   * @param value to use
   * @return a correction that adjusts the value
   * @since 4.0.0
   */
  public static GalenCorrection adjust(int value) {
    if (value > 0) {
      return new GalenCorrection(Type.PLUS, value);
    }
    return new GalenCorrection(Type.MINUS, -value);
  }

  /**
   * <p>getCorrection.</p>
   *
   * @return a {@link com.galenframework.specs.page.CorrectionsRect.Correction} object.
   * @since 4.0.0
   */
  public Correction getCorrection() {
    return new Correction(getValue(), getType());
  }

  protected int getValue() {
    return value;
  }

  protected Type getType() {
    return type;
  }

  /**
   * <p>none.</p>
   *
   * @return a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrection} object.
   * @since 4.0.0
   */
  public static GalenCorrection neutral() {
    return adjust(0);
  }

}
