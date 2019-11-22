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
package io.wcm.qa.glnm.verification.element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.sampling.element.VisibilitySampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.element.base.WebElementBasedVerification;

/**
 * Verifies that an element is visible on page.
 *
 * @since 1.0.0
 */
public class VisibilityVerification extends WebElementBasedVerification<VisibilitySampler, Boolean> {

  private static final Logger LOG = LoggerFactory.getLogger(VisibilityVerification.class);

  /**
   * Constructor.
   *
   * @param selector to identify element
   * @since 2.0.0
   */
  public VisibilityVerification(Selector selector) {
    super(new VisibilitySampler(selector));
  }

  private String getVerboseSelectorInfo() {
    return " (" + getSelector().asString() + ")";
  }

  @Override
  protected void afterVerification() {
    if (isVerified()) {
      LOG.trace("successfully confirmed visibility of '" + getElementName() + "'");
    }
    else {
      LOG.trace("could not confirm visibility of '" + getElementName() + "'");
    }
  }

  @Override
  protected boolean doVerification() {
    Boolean actualValue = getActualValue();
    if (actualValue != null) {
      return actualValue;
    }
    return false;
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
