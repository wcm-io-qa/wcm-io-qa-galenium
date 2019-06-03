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
package io.wcm.qa.galenium.imagecomparison;

import java.util.List;

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.galenium.differences.base.Differences;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 *
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

  CorrectionsRect getCorrections();

  Differences getDifferences();

  String getElementName();

  String getFilename();

  /**
   * @return the set folder name or one constructed from differences
   */
  String getFoldername();

  Locator getLocator();

  List<Selector> getObjectsToIgnore();

  String getSectionName();

  Selector getSelector();

  ValidationListener getValidationListener();

  boolean isZeroToleranceWarning();

}
