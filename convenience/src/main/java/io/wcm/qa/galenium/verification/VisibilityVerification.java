/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.galenium.verification;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

/**
 * Verifies that an element is visible on page.
 */
public class VisibilityVerification extends ElementBasedVerification {

  /**
   * Constructor.
   * @param selector to identify element
   */
  public VisibilityVerification(Selector selector) {
    super(selector);
  }

  private String getVerboseSelectorInfo() {
    return " (" + getSelector().asString() + ")";
  }

  @Override
  protected Boolean doVerification() {
    return getElement() != null;
  }

  @Override
  protected String getFailureMessage() {
    String failureMessage = getElementName() + " is not visible";
    if (GaleniumConfiguration.isSparseReporting() || getSelector() == null) {
      return failureMessage;
    }
    return failureMessage + getVerboseSelectorInfo();
  }

  @Override
  protected String getSuccessMessage() {
    String successMessage = getElementName() + " is visible";
    if (GaleniumConfiguration.isSparseReporting() || getSelector() == null) {
      return successMessage;
    }
    return successMessage + getVerboseSelectorInfo();
  }

}
