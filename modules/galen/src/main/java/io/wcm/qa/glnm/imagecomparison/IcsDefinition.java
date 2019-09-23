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
package io.wcm.qa.glnm.imagecomparison;

import java.util.List;

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Parameters for constructing image comparison spec.
 */
public interface IcsDefinition {

  /**
   * @param yCorrection amount of scrolling
   */
  void correctForSrollPosition(int yCorrection);

  /**
   * @return allowed error string
   */
  String getAllowedError();

  /**
   * @return offset to analyse
   */
  int getAllowedOffset();

  /**
   * @return corrections applied to locator
   */
  CorrectionsRect getCorrections();

  /**
   * @return name of element
   */
  String getElementName();

  /**
   * @return the filename of image sample to compare against
   */
  String getFilename();

  /**
   * @return the folder name of image sample to compare against
   */
  String getFoldername();

  /**
   * @return selector as locator with corrections
   */
  Locator getLocator();

  /**
   * @return list of objects to ignore
   */
  List<Selector> getObjectsToIgnore();

  /**
   * @return name of Galen spec section
   */
  String getSectionName();

  /**
   * @return selector of element to check
   */
  Selector getSelector();

  /**
   * @return whether to add a zero tolerance check at warning level
   */
  boolean isZeroToleranceWarning();

  /**
   * @return whether to ignore sampling outside of raster
   */
  boolean isCropIfOutside();

}
