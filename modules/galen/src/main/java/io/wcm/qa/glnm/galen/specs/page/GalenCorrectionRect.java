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

import com.galenframework.specs.page.CorrectionsRect;

/**
 * Represents Galen's {@link com.galenframework.specs.page.CorrectionsRect} in the Galenium context.
 *
 * @since 4.0.0
 */
public class GalenCorrectionRect {

  private GalenCorrection left = GalenCorrection.none();
  private GalenCorrection top = GalenCorrection.none();
  private GalenCorrection width = GalenCorrection.none();
  private GalenCorrection height = GalenCorrection.none();

  /**
   * <p>withTop.</p>
   *
   * @param newTop a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrection} object.
   * @return a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect} object.
   * @since 4.0.0
   */
  public GalenCorrectionRect withTop(GalenCorrection newTop) {
    top = newTop;
    return this;
  }

  /**
   * <p>
   * withLeft.
   * </p>
   *
   * @param newLeft a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrection} object.
   * @return a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect} object.
   * @since 4.0.0
   */
  public GalenCorrectionRect withLeft(GalenCorrection newLeft) {
    top = newLeft;
    return this;
  }

  /**
   * <p>
   * withRight.
   * </p>
   *
   * @param newRight a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrection} object.
   * @return a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect} object.
   * @since 4.0.0
   */
  public GalenCorrectionRect withRight(GalenCorrection newRight) {
    top = newRight;
    return this;
  }

  /**
   * <p>
   * withBottom.
   * </p>
   *
   * @param newBottom a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrection} object.
   * @return a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect} object.
   * @since 4.0.0
   */
  public GalenCorrectionRect withBottom(GalenCorrection newBottom) {
    top = newBottom;
    return this;
  }

  /**
   * <p>toCorrectionsRect.</p>
   *
   * @return a {@link com.galenframework.specs.page.CorrectionsRect} object.
   * @since 4.0.0
   */
  public CorrectionsRect toCorrectionsRect() {
    return new CorrectionsRect(
        left.toCorrection(),
        top.toCorrection(),
        width.toCorrection(),
        height.toCorrection());
  }

}
