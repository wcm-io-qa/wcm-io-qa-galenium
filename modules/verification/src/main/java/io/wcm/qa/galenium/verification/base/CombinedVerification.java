/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.sampling.CanCache;

/**
 * Combines multiple verifications into a single verification.
 *
 * @since 1.0.0
 */
public class CombinedVerification implements Verification, CanCache {

  private Collection<String> combinedMessages = new ArrayList<String>();
  private Throwable exception;
  private Collection<Verification> members = new ArrayList<Verification>();
  private String messageSeparator = "<br/>";
  private boolean reportSuccess = !GaleniumConfiguration.isSparseReporting();

  /**
   * <p>Constructor for CombinedVerification.</p>
   *
   * @param verifications initial set of verifications
   */
  public CombinedVerification(Verification... verifications) {
    for (Verification verification : verifications) {
      addVerification(verification);
    }
  }

  /**
   * Add a difference to all verifications being combined in this one.
   *
   * @param difference new difference to add to all member verifications
   * @return a {@link io.wcm.qa.galenium.verification.base.CombinedVerification} object.
   */
  public CombinedVerification addDifference(Difference difference) {
    for (Verification verification : getMembers()) {
      if (verification instanceof VerificationBase) {
        ((VerificationBase)verification).addDifference(difference);
      }
    }
    return this;
  }

  /**
   * Add another check.
   *
   * @param verification to also verify as part of this comined verification
   * @return a {@link io.wcm.qa.galenium.verification.base.CombinedVerification} object.
   */
  public CombinedVerification addVerification(Verification verification) {
    getMembers().add(verification);
    return this;
  }

  /**
   * <p>Getter for the field <code>combinedMessages</code>.</p>
   *
   * @return a {@link java.util.Collection} object.
   */
  public Collection<String> getCombinedMessages() {
    return combinedMessages;
  }

  /** {@inheritDoc} */
  @Override
  public Throwable getException() {
    return exception;
  }

  /** {@inheritDoc} */
  @Override
  public String getMessage() {
    return getCombinedMessage();
  }

  /**
   * <p>Getter for the field <code>messageSeparator</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getMessageSeparator() {
    return messageSeparator;
  }

  /**
   * <p>isReportSuccess.</p>
   *
   * @return a boolean.
   */
  public boolean isReportSuccess() {
    return reportSuccess;
  }

  /**
   * <p>Setter for the field <code>combinedMessages</code>.</p>
   *
   * @param combinedMessages a {@link java.util.Collection} object.
   */
  public void setCombinedMessages(Collection<String> combinedMessages) {
    this.combinedMessages = combinedMessages;
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
   * <p>Setter for the field <code>messageSeparator</code>.</p>
   *
   * @param messageSeparator a {@link java.lang.String} object.
   */
  public void setMessageSeparator(String messageSeparator) {
    this.messageSeparator = messageSeparator;
  }

  /**
   * <p>Setter for the field <code>reportSuccess</code>.</p>
   *
   * @param reportSuccess a boolean.
   */
  public void setReportSuccess(boolean reportSuccess) {
    this.reportSuccess = reportSuccess;
  }

  /** {@inheritDoc} */
  @Override
  public boolean verify() {
    if (hasMembers()) {
      return verifyMembers();
    }
    throw new GaleniumException("no verifications found when verifying combined verification.");
  }

  protected void addMessage(String message) {
    getCombinedMessages().add(message);
  }

  protected void fail(Verification verification) {
    addMessage("failed: " + verification.getMessage());
  }

  protected String getCombinedMessage() {
    return StringUtils.join(getCombinedMessages(), getMessageSeparator());
  }

  protected Collection<Verification> getMembers() {
    return members;
  }

  protected boolean hasMembers() {
    return !getMembers().isEmpty();
  }

  protected void pass(Verification verification) {
    if (isReportSuccess()) {
      addMessage("passed: " + verification.getMessage());
    }
  }

  protected boolean verifyMembers() {
    boolean noFailures = true;
    for (Verification verification : getMembers()) {
      if (verification.verify()) {
        pass(verification);
      }
      else {
        fail(verification);
        noFailures = false;
      }
    }
    return noFailures;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCaching() {
    for (Verification verification : getMembers()) {
      if (verification instanceof CanCache && ((CanCache)verification).isCaching()) {
        return true;
      }
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void setCaching(boolean activateCache) {
    for (Verification verification : getMembers()) {
      if (verification instanceof CanCache) {
        ((CanCache)verification).setCaching(activateCache);
      }
    }
  }

}
