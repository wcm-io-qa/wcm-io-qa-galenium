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

  /**
   * <p>Constructor for BrowserLogVerification.</p>
   */
  @SuppressWarnings("unchecked")
  public BrowserLogVerification() {
    super(new CountingSampler(new BrowserLogSampler()));
  }

  @Override
  protected boolean doVerification() {
    // TODO: Auto-generated method stub
    return false;
  }

  @Override
  protected String getFailureMessage() {
    return null;
  }

  @Override
  protected String getSuccessMessage() {
    return "no unexpected messages found.";
  }

  @Override
  protected Integer initExpectedValue() {
    return 0;
  }

  @Override
  protected void persistSample(String key, Integer newValue) {
    // no sample to persist
  }
}
