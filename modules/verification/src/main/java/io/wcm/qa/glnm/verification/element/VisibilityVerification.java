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

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.element.VisibilitySampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.element.base.WebElementBasedVerification;

/**
 * Verifies that an element is visible on page.
 */
public class VisibilityVerification extends WebElementBasedVerification<VisibilitySampler, Boolean> {

  /**
   * Constructor.
   * @param selector to identify element
   */
  public VisibilityVerification(Selector selector) {
    super(selector.elementName(), new VisibilitySampler(selector));
  }

  private String getVerboseSelectorInfo() {
    return " (" + getSelector().asString() + ")";
  }

  @Override
  protected void afterVerification() {
    if (isVerified()) {
      getLogger().trace("successfully confirmed visibility of '" + getElementName() + "'");
    }
    else {
      getLogger().trace("could not confirm visibility of '" + getElementName() + "'");
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

  @Override
  protected Boolean initExpectedValue() {
    throw new GaleniumException("no need to init expected value, because always expects visibility");
  }

  @Override
  protected void persistSample(String key, Boolean newValue) {
    throw new GaleniumException("no need to persist expected value, because always expects visibility");
  }
}
