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
package io.wcm.qa.glnm.verification.base;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.CanCache;

/**
 * Combines multiple verifications into a single verification.
 */
public class CombinedVerification implements Verification, CanCache {

  private Collection<String> combinedMessages = new ArrayList<String>();
  private Throwable exception;
  private Collection<Verification> members = new ArrayList<Verification>();
  private String messageSeparator = "<br/>";
  private boolean reportSuccess = !GaleniumConfiguration.isSparseReporting();

  /**
   * @param verifications initial set of verifications
   */
  public CombinedVerification(Verification... verifications) {
    for (Verification verification : verifications) {
      addVerification(verification);
    }
  }

  /**
   * Add a difference to all verifications being combined in this one.
   * @param difference new difference to add to all member verifications
   * @return this
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
   * @param verification to also verify as part of this comined verification
   * @return this
   */
  public CombinedVerification addVerification(Verification verification) {
    getMembers().add(verification);
    return this;
  }

  public Collection<String> getCombinedMessages() {
    return combinedMessages;
  }

  @Override
  public Throwable getException() {
    return exception;
  }

  @Override
  public String getMessage() {
    return getCombinedMessage();
  }

  public String getMessageSeparator() {
    return messageSeparator;
  }

  public boolean isReportSuccess() {
    return reportSuccess;
  }

  public void setCombinedMessages(Collection<String> combinedMessages) {
    this.combinedMessages = combinedMessages;
  }

  public void setException(Throwable exception) {
    this.exception = exception;
  }

  public void setMessageSeparator(String messageSeparator) {
    this.messageSeparator = messageSeparator;
  }

  public void setReportSuccess(boolean reportSuccess) {
    this.reportSuccess = reportSuccess;
  }

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

  @Override
  public boolean isCaching() {
    for (Verification verification : getMembers()) {
      if (verification instanceof CanCache && ((CanCache)verification).isCaching()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void setCaching(boolean activateCache) {
    for (Verification verification : getMembers()) {
      if (verification instanceof CanCache) {
        ((CanCache)verification).setCaching(activateCache);
      }
    }
  }

}
