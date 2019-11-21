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
package io.wcm.qa.glnm.verification.base;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.generic.SortedDifferences;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.sampling.CanCache;

/**
 * Common base for  {@link io.wcm.qa.glnm.differences.base.Difference} aware Galenium  {@link io.wcm.qa.glnm.verification.base.Verification}.
 *
 * @param <T> sample type
 * @since 1.0.0
 */
public abstract class VerificationBase<T> implements Verification, CanCache {

  private static final int DEFAULT_MAX_NAME_LENGTH_IN_KEY = 30;
  private static final Logger LOG = LoggerFactory.getLogger(VerificationBase.class);
  private static final String STRING_TO_REMOVE_FROM_CLASS_NAME = "verification";

  private T actualValue;
  private boolean caching;
  private SortedDifferences differences;
  private Throwable exception;
  private T expectedValue;
  private int keyMaxLength = DEFAULT_MAX_NAME_LENGTH_IN_KEY;
  private Verification preVerification;
  private Boolean verified;

  protected VerificationBase() {
    setCaching(true);
  }

  protected VerificationBase(T expectedValue) {
    this();
    setExpectedValue(expectedValue);
  }


  protected String getVerificationName() {
    return toString();
  }

  /**
   * Add a difference to this verification.
   *
   * @param difference to add
   * @return this
   */
  public VerificationBase<T> addDifference(Difference difference) {
    getDifferences().add(difference);
    return this;
  }

  /**
   * <p>getComparator.</p>
   *
   * @return comparator used to sort differences
   */
  public Comparator<Difference> getComparator() {
    return getDifferences().getComparator();
  }

  /** {@inheritDoc} */
  @Override
  public Throwable getException() {
    return exception;
  }

  /**
   * <p>Getter for the field <code>keyMaxLength</code>.</p>
   *
   * @return a int.
   */
  public int getKeyMaxLength() {
    return keyMaxLength;
  }

  /** {@inheritDoc} */
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
   * Pre verification is run before this verification to test whether it makes sense to attempt verification. Verifying
   * an attribute value is futile if the element does not exist, for example.
   *
   * @return pre verification
   */
  public Verification getPreVerification() {
    return preVerification;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCaching() {
    return caching;
  }

  /**
   * <p>isVerified.</p>
   *
   * @return whether verification was successful or null, if verification did not run yet.
   */
  public Boolean isVerified() {
    return verified;
  }

  /** {@inheritDoc} */
  @Override
  public void setCaching(boolean caching) {
    this.caching = caching;
    setCachingInPreVerification(caching);
  }

  /**
   * <p>setComparator.</p>
   *
   * @param comparator to use for sorting associated differences
   */
  public void setComparator(Comparator<Difference> comparator) {
    getDifferences().setComparator(comparator);
  }

  /**
   * <p>Setter for the field <code>exception</code>.</p>
   *
   * @param exception a {@link java.lang.Throwable} object.
   */
  public void setException(Throwable exception) {
    this.exception = exception;
  }

  /**
   * <p>Setter for the field <code>keyMaxLength</code>.</p>
   *
   * @param keyMaxLength a int.
   */
  public void setKeyMaxLength(int keyMaxLength) {
    this.keyMaxLength = keyMaxLength;
  }

  /**
   * <p>Setter for the field <code>preVerification</code>.</p>
   *
   * @param preVerification to verify before attempting main verification
   */
  public void setPreVerification(Verification preVerification) {
    this.preVerification = preVerification;
    setCachingInPreVerification(caching);
  }

  /**
   * <p>Setter for the field <code>verified</code>.</p>
   *
   * @param verified a {@link java.lang.Boolean} object.
   */
  public void setVerified(Boolean verified) {
    this.verified = verified;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getCleanName());
    stringBuilder.append("(");
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
   * {@inheritDoc}
   *
   * Runs pre verification and this verification using built-in text sampling.
   */
  @Override
  public boolean verify() {
    LOG.trace("verifying (" + toString() + ")");
    if (hasPreVerification()) {
      LOG.trace("preverifying (" + getPreVerification().toString() + ")");
      if (!getPreVerification().verify()) {
        return false;
      }
    }
    try {
      setVerified(doVerification());
    }
    catch (GaleniumException ex) {
      LOG.debug(toString() + ": error occured during verification", ex);
      setException(ex);
      setVerified(false);
    }
    afterVerification();
    return isVerified();
  }

  protected String getCleanName() {
    String simpleClassName = getClass().getSimpleName();
    String strippedClassName = simpleClassName;
    if (isStripVerificationFromClassName()) {
      strippedClassName = StringUtils.removeEndIgnoreCase(simpleClassName, STRING_TO_REMOVE_FROM_CLASS_NAME);
    }
    return strippedClassName;
  }

  private void setCachingInPreVerification(boolean cachingForPreverification) {
    Verification pre = getPreVerification();
    if (pre != null && pre instanceof CanCache) {
      ((CanCache)pre).setCaching(cachingForPreverification);
    }
  }

  protected void afterVerification() {
    LOG.trace("looking for '" + getValueForLogging(getExpectedValue()) + "'");
    T cachedValue = getCachedValue();
    LOG.trace("found: '" + getValueForLogging(cachedValue) + "'");
    if (!isVerified() && cachedValue != null) {
      String expectedKey = getExpectedKey();
      persistSample(expectedKey, cachedValue);
    }
    LOG.trace("done verifying (" + toString() + ")");
  }

  /**
   * Override to implement verification.
   * @return whether verification was successful
   */
  protected abstract boolean doVerification();

  /**
   * @return actual value, defaults to {@link VerificationBase#sampleValue()}
   */
  protected T getActualValue() {
    if (!isCaching()) {
      LOG.trace("invalidating cache for: '" + toString() + "'");
      actualValue = null;
    }
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

  protected T getCachedValue() {
    return actualValue;
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
    String expectedKey = getDifferences().asPropertyKey();
    if (StringUtils.isNotBlank(expectedKey)) {
      return getCleanName() + "." + expectedKey;
    }
    return getCleanName();
  }

  /**
   * @return expected value if one was set or is retrievable from sample manager
   */
  protected T getExpectedValue() {
    if (expectedValue == null) {
      expectedValue = initExpectedValue();
    }
    return expectedValue;
  }

  /**
   * @return message for use on failed verification
   */
  protected abstract String getFailureMessage();

  protected int getMaximumStringLengthForLogging() {
    return 800;
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

  protected String getValueForLogging(T value) {
    if (value == null) {
      return "null";
    }
    String escaped = GaleniumReportUtil.escapeHtml(value.toString());
    String abbreviated = StringUtils.abbreviateMiddle(escaped, "...", getMaximumStringLengthForLogging());
    return abbreviated;
  }

  /**
   * @return whether pre verification exists
   */
  protected boolean hasPreVerification() {
    return getPreVerification() != null;
  }

  protected abstract T initExpectedValue();

  /**
   * @return whether to remove "Verification" from name
   */
  protected boolean isStripVerificationFromClassName() {
    return true;
  }

  protected abstract void persistSample(String key, T newValue);

  protected void persistSample(T newValue) {
    persistSample(getExpectedKey(), newValue);
  }

  /**
   * Override to actually sample a value.
   * @return sample value to be used as actual value
   */
  protected abstract T sampleValue();

  /**
   * @param differences replaces differences on this verification
   */
  protected void setDifferences(SortedDifferences differences) {
    this.differences = differences;
  }

  /**
   * Set expected value directly to bypass built-in sampling.
   * @param value to use in verification
   */
  protected void setExpectedValue(T value) {
    this.expectedValue = value;
  }

}
