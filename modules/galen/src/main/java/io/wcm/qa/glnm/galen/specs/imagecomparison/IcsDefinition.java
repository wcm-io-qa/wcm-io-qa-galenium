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
package io.wcm.qa.glnm.galen.specs.imagecomparison;

import java.util.List;

import io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Parameters for constructing image comparison spec.
 *
 * @since 2.0.0
 */
public interface IcsDefinition {

  /**
   * <p>correctForSrollPosition.</p>
   *
   * @param yCorrection amount of scrolling
   * @since 4.0.0
   */
  void correctForSrollPosition(int yCorrection);

  /**
   * <p>getAllowedError.</p>
   *
   * @return allowed error string
   * @since 4.0.0
   */
  String getAllowedError();

  /**
   * <p>getAllowedOffset.</p>
   *
   * @return offset to analyse
   * @since 4.0.0
   */
  int getAllowedOffset();

  /**
   * <p>getCorrections.</p>
   *
   * @return corrections applied to locator
   * @since 4.0.0
   */
  GalenCorrectionRect getCorrections();

  /**
   * <p>getElementName.</p>
   *
   * @return name of element
   * @since 4.0.0
   */
  String getElementName();

  /**
   * <p>getFilename.</p>
   *
   * @return the filename of image sample to compare against
   * @since 4.0.0
   */
  String getFilename();

  /**
   * <p>getFoldername.</p>
   *
   * @return the folder name of image sample to compare against
   * @since 4.0.0
   */
  String getFoldername();

  /**
   * <p>getObjectsToIgnore.</p>
   *
   * @return list of objects to ignore
   * @since 4.0.0
   */
  List<Selector> getObjectsToIgnore();

  /**
   * <p>getSectionName.</p>
   *
   * @return name of Galen spec section
   * @since 4.0.0
   */
  String getSectionName();

  /**
   * <p>getSelector.</p>
   *
   * @return selector of element to check
   * @since 4.0.0
   */
  Selector getSelector();

  /**
   * <p>isCropIfOutside.</p>
   *
   * @return whether to ignore sampling outside of raster
   * @since 4.0.0
   */
  boolean isCropIfOutside();

  /**
   * <p>isZeroToleranceWarning.</p>
   *
   * @return whether to add a zero tolerance check at warning level
   * @since 4.0.0
   */
  boolean isZeroToleranceWarning();

}
