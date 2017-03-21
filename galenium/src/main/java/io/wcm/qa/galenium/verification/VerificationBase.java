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

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.MutableDifferences;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.InteractionUtil;

abstract class VerificationBase implements Verification {

  private MutableDifferences differences;
  private Verification preVerification;
  private Selector selector;
  private Boolean verified;

  protected VerificationBase(Selector selector) {
    setSelector(selector);
  }

  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  /**
   * @see io.wcm.qa.galenium.verification.Verification#getMessage()
   */
  @Override
  public String getMessage() {
    if (isVerified() == null) {
      if (hasPreVerification()) {
        return getPreVerification().getMessage();
      }
      return getNotVerifiedMessage();
    }
    if (isVerified()) {
      return getSuccessMessage();
    }
    return getFailureMessage();
  }

  public Verification getPreVerification() {
    return preVerification;
  }

  public Boolean isVerified() {
    return verified;
  }

  public void setPreVerification(Verification preVerification) {
    this.preVerification = preVerification;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.verification.Verification#verify()
   */
  @Override
  public boolean verify() {
    if (hasPreVerification()) {
      if (!getPreVerification().verify()) {
        return false;
      }
    }
    try {
      setVerified(doVerification());
    }
    catch (GaleniumException ex) {
      getLogger().info(GaleniumReportUtil.MARKER_FAIL, getFailureMessage());
      setVerified(false);
    }
    return isVerified();
  }

  protected abstract Boolean doVerification();

  protected MutableDifferences getDifferences() {
    if (differences == null) {
      differences = new MutableDifferences();
    }
    return differences;
  }

  protected WebElement getElement() {
    return InteractionUtil.getElementVisible(getSelector());
  }

  protected String getElementName() {
    return getSelector().elementName();
  }

  protected abstract String getFailureMessage();


  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  protected String getNotVerifiedMessage() {
    return "NOT VERIFIED: " + getElementName();
  }

  protected Selector getSelector() {
    return selector;
  }

  protected abstract String getSuccessMessage();

  protected boolean hasPreVerification() {
    return getPreVerification() != null;
  }

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

  protected void setSelector(Selector selector) {
    this.selector = selector;
  }
}
