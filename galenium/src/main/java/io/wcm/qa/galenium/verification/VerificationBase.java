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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_ERROR;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.MutableDifferences;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.InteractionUtil;

abstract class VerificationBase implements Verification {

  private MutableDifferences differences;
  private Throwable exception;
  private Verification preVerification;
  private Selector selector;
  private Boolean verified;

  protected VerificationBase(Selector selector) {
    setSelector(selector);
  }

  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }
  @Override
  public Throwable getException() {
    return exception;
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

  public void setException(Throwable exception) {
    this.exception = exception;
  }

  public void setPreVerification(Verification preVerification) {
    this.preVerification = preVerification;
  }

  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append("(");
    stringBuilder.append(getElementName());
    if (StringUtils.isNotBlank(getAdditionalToStringInfo())) {
      stringBuilder.append(", ");
      stringBuilder.append(getAdditionalToStringInfo());
    }
    stringBuilder.append(")");
    return stringBuilder.toString();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.verification.Verification#verify()
   */
  @Override
  public boolean verify() {
    getLogger().trace("verifying (" + toString() + ")");
    if (hasPreVerification()) {
      getLogger().trace("preverifying (" + getPreVerification().toString() + ")");
      if (!getPreVerification().verify()) {
        return false;
      }
    }
    try {
      setVerified(doVerification());
      getLogger().trace("done verifying (" + toString() + ")");
    }
    catch (Throwable ex) {
      getLogger().debug(MARKER_ERROR, toString() + ": error occured during verification", ex);
      setException(ex);
      setVerified(false);
    }
    return isVerified();
  }

  protected abstract Boolean doVerification();

  protected String getAdditionalToStringInfo() {
    return StringUtils.EMPTY;
  }


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
