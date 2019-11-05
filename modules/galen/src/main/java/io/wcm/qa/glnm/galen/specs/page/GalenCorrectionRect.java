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

  private GalenCorrection height = GalenCorrection.neutral();
  private GalenCorrection left = GalenCorrection.neutral();
  private GalenCorrection top = GalenCorrection.neutral();
  private GalenCorrection width = GalenCorrection.neutral();

  /**
   * <p>toCorrectionsRect.</p>
   *
   * @return a {@link com.galenframework.specs.page.CorrectionsRect} object.
   * @since 4.0.0
   */
  public CorrectionsRect getCorrectionsRect() {
    return new CorrectionsRect(
        left.getCorrection(),
        top.getCorrection(),
        width.getCorrection(),
        height.getCorrection());
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

}
