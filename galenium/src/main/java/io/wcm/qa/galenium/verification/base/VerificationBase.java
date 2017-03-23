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
package io.wcm.qa.galenium.verification.base;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_ERROR;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.MutableDifferences;
import io.wcm.qa.galenium.sampling.text.TextSampleManager;

public abstract class VerificationBase implements Verification {

  private String actualValue;
  private MutableDifferences differences;
  private Throwable exception;
  private String expectedValue;
  private Verification preVerification;
  private Boolean verified;
  private String verificationName;


  protected VerificationBase(String verificationName) {
    setVerificationName(verificationName);
  }

  protected VerificationBase(String verificationName, String expectedValue) {
    this(verificationName);
    setExpectedValue(expectedValue);
  }

  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  @Override
  public Throwable getException() {
    return exception;
  }

  /**
   * @see io.wcm.qa.galenium.verification.base.Verification#getMessage()
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
    stringBuilder.append(getVerificationName());
    if (StringUtils.isNotBlank(getAdditionalToStringInfo())) {
      stringBuilder.append(", ");
      stringBuilder.append(getAdditionalToStringInfo());
    }
    stringBuilder.append(")");
    return stringBuilder.toString();
  }

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
      if (!isVerified() && getActualValue() != null) {
        TextSampleManager.addNewTextSample(getExpectedKey(), getActualValue());
      }
    }
    catch (Throwable ex) {
      getLogger().debug(MARKER_ERROR, toString() + ": error occured during verification", ex);
      setException(ex);
      setVerified(false);
    }
    return isVerified();
  }

  protected Boolean doVerification() {
    return StringUtils.equals(getActualValue(), getExpectedValue());
  }

  protected String getActualValue() {
    if (actualValue == null) {
      actualValue = sampleValue();
    }
    return actualValue;
  }

  protected String getAdditionalToStringInfo() {
    return StringUtils.EMPTY;
  }

  protected MutableDifferences getDifferences() {
    if (differences == null) {
      differences = new MutableDifferences();
    }
    return differences;
  }

  protected String getExpectedKey() {
    return getDifferences().asPropertyKey();
  }

  protected String getExpectedValue() {
    if (expectedValue == null) {
      String expectedKey = getExpectedKey();
      if (StringUtils.isNotBlank(expectedKey)) {
        expectedValue = TextSampleManager.getExpectedText(expectedKey);
      }
    }
    return expectedValue;
  }

  protected abstract String getFailureMessage();

  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  protected String getNotVerifiedMessage() {
    return "NOT VERIFIED";
  }

  protected abstract String getSuccessMessage();

  protected boolean hasPreVerification() {
    return getPreVerification() != null;
  }

  protected String sampleValue() {
    getLogger().debug("trying to sample from " + toString());
    return null;
  }

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

  protected void setExpectedValue(String expectedValue) {
    this.expectedValue = expectedValue;
  }

  public String getVerificationName() {
    return verificationName;
  }

  protected void setVerificationName(String verificationName) {
    this.verificationName = verificationName;
  }

}
