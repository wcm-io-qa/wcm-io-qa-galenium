/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.verification.browser;

import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.browser.BrowserLogSampler;
import io.wcm.qa.glnm.sampling.transform.CountingSampler;
import io.wcm.qa.glnm.verification.base.SamplerBasedVerification;

/**
 * <p>
 * Checks for messages with high log levels.
 * </p>
 *
 * @since 4.0.0
 */
public class BrowserLogVerification extends SamplerBasedVerification<CountingSampler<String>, Integer> {

  private BrowserLogSampler logSampler;


  /**
   * <p>Constructor for BrowserLogVerification.</p>
   */
  public BrowserLogVerification() {
    this(new BrowserLogSampler());
  }

  /**
   * <p>Constructor for BrowserLogVerification.</p>
   *
   * @param minRelevantLevel a {@link java.util.logging.Level} object.
   */
  public BrowserLogVerification(Level minRelevantLevel) {
    this(new BrowserLogSampler(minRelevantLevel));
  }

  /**
   * <p>Constructor for BrowserLogVerification.</p>
   *
   * @param browserLogSampler a {@link io.wcm.qa.glnm.sampling.browser.BrowserLogSampler} object.
   */
  private BrowserLogVerification(BrowserLogSampler browserLogSampler) {
    super(new CountingSampler<String>(browserLogSampler));
    logSampler = browserLogSampler;
  }

  private Level getLevel() {
    return getLogSampler().getLevel();
  }

  /**
   * <p>Getter for the field <code>logSampler</code>.</p>
   *
   * @return a {@link io.wcm.qa.glnm.sampling.browser.BrowserLogSampler} object.
   */
  public BrowserLogSampler getLogSampler() {
    return logSampler;
  }

  @Override
  protected boolean doVerification() {
    return getActualValue() > 0;
  }

  @Override
  protected String getFailureMessage() {
    StringBuilder stringBuilder = new StringBuilder()
        .append("found messages with at level ")
        .append(getLevel())
        .append(" or higher:\n")
        .append(StringUtils.join(getLogSampler().sampleValue(), '\n'));
    return stringBuilder.toString();
  }

  @Override
  protected String getSuccessMessage() {
    return "no unexpected messages found.";
  }

  @Override
  protected Integer initExpectedValue() {
    return 0;
  }

  /**
   * <p>Setter for the field <code>logSampler</code>.</p>
   *
   * @param logSampler a {@link io.wcm.qa.glnm.sampling.browser.BrowserLogSampler} object.
   */
  public void setLogSampler(BrowserLogSampler logSampler) {
    this.logSampler = logSampler;
  }
}
