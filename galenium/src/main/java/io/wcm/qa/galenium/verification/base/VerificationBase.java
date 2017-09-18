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

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.SortedDifferences;
import io.wcm.qa.galenium.sampling.text.TextSampleManager;

/**
 * Common base for {@link Difference} aware Galenium {@link Verification}.
 */
public abstract class VerificationBase implements Verification {

  private String actualValue;
  private SortedDifferences differences;
  private Throwable exception;
  private String expectedValue;
  private Verification preVerification;
  private String verificationName;
  private Boolean verified;

  protected VerificationBase(String verificationName) {
    setVerificationName(verificationName);
  }

  protected VerificationBase(String verificationName, String expectedValue) {
    this(verificationName);
    setExpectedValue(expectedValue);
  }

  /**
   * Add a difference to this verification.
   * @param difference to add
   */
  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  /**
   * @return comparator used to sort differences
   */
  public Comparator<Difference> getComparator() {
    return getDifferences().getComparator();
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

  /**
   * Pre verification is run before this verfication to test whether it makes sense to attempt verification. Verifying
   * an attribute value is futile if the element does not exist, for example.
   * @return pre verification
   */
  public Verification getPreVerification() {
    return preVerification;
  }

  /**
   * @return name of this verification
   */
  public String getVerificationName() {
    return verificationName;
  }

  /**
   * @return whether verification was successful or null, if verification did not run yet.
   */
  public Boolean isVerified() {
    return verified;
  }

  /**
   * @param comparator to use for sorting associated differences
   */
  public void setComparator(Comparator<Difference> comparator) {
    getDifferences().setComparator(comparator);
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
    if (StringUtils.isNotBlank(getDifferences().asPropertyKey())) {
      stringBuilder.append("|");
      stringBuilder.append(getDifferences());
    }
    if (StringUtils.isNotBlank(getAdditionalToStringInfo())) {
      stringBuilder.append(", ");
      stringBuilder.append(getAdditionalToStringInfo());
    }
    stringBuilder.append(")");
    return stringBuilder.toString();
  }

  /**
   * Runs pre verification and this verification using built-in text sampling.
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

  /**
   * Override to use anything that is not String equivalence of actual and expected value.
   * @return whether verification was successful
   */
  protected Boolean doVerification() {
    return StringUtils.equals(getActualValue(), getExpectedValue());
  }

  /**
   * @return actual value, defaults to {@link VerificationBase#sampleValue()}
   */
  protected String getActualValue() {
    if (actualValue == null) {
      actualValue = sampleValue();
    }
    return actualValue;
  }

  /**
   * Override to supply additional info on verification. Default is empty String.
   * @return additional info about this verification
   */
  protected String getAdditionalToStringInfo() {
    return StringUtils.EMPTY;
  }

  protected SortedDifferences getDifferences() {
    if (differences == null) {
      differences = new SortedDifferences();
    }
    return differences;
  }

  /**
   * @return key to use in persisting and retrieving sample values
   */
  protected String getExpectedKey() {
    return getDifferences().asPropertyKey();
  }

  /**
   * @return expected value if one was set or is retrievable from sample manager
   */
  protected String getExpectedValue() {
    if (expectedValue == null) {
      String expectedKey = getExpectedKey();
      if (StringUtils.isNotBlank(expectedKey)) {
        expectedValue = TextSampleManager.getExpectedText(expectedKey);
      }
    }
    return expectedValue;
  }

  /**
   * @return message for use on failed verification
   */
  protected abstract String getFailureMessage();

  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  /**
   * @return message for use when verification was not done yet
   */
  protected String getNotVerifiedMessage() {
    return "NOT VERIFIED";
  }

  /**
   * @return message for use on successful verification
   */
  protected abstract String getSuccessMessage();

  /**
   * @return whether pre verification exists
   */
  protected boolean hasPreVerification() {
    return getPreVerification() != null;
  }

  /**
   * Override to actually sample a value.
   * @return sample value to be used as actual value
   */
  protected String sampleValue() {
    getLogger().debug("trying to sample from " + toString());
    return null;
  }

  /**
   * @param differences replaces differences on this verification
   */
  protected void setDifferences(SortedDifferences differences) {
    this.differences = differences;
  }

  /**
   * Set expected value directly to bypass built-in sampling.
   * @param expectedValue to use in verification
   */
  protected void setExpectedValue(String expectedValue) {
    this.expectedValue = expectedValue;
  }

  protected void setVerificationName(String verificationName) {
    this.verificationName = verificationName;
  }

}
