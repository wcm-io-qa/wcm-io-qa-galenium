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
package io.wcm.qa.glnm.verification.string;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;

/**
 * Verifies that string is contained in sample.
 *
 * @since 1.0.0
 */
public class ContainsStringVerification extends StringVerification {

  private static final Logger LOG = LoggerFactory.getLogger(ContainsStringVerification.class);

  private String searchString;

  /**
   * Verify against input provided by sampler.
   *
   * @param verificationName name for this check
   * @param searchString to find in input
   * @param sampler sampler to provide input
   * @since 2.0.0
   */
  public ContainsStringVerification(String verificationName, String searchString, Sampler<String> sampler) {
    super(verificationName, sampler);
    setSearchString(searchString);
  }

  /**
   * Verify against fixed string.
   *
   * @param verificationName name for this check
   * @param searchString to find in input
   * @param sample fixed input sample
   * @since 2.0.0
   */
  public ContainsStringVerification(String verificationName, String searchString, String sample) {
    this(verificationName, searchString, new FixedStringSampler(sample));
  }

  /**
   * <p>Getter for the field <code>searchString</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 2.0.0
   */
  public String getSearchString() {
    return searchString;
  }

  /**
   * <p>Setter for the field <code>searchString</code>.</p>
   *
   * @param searchString a {@link java.lang.String} object.
   * @since 2.0.0
   */
  public void setSearchString(String searchString) {
    this.searchString = searchString;
  }

  @Override
  protected void afterVerification() {
    LOG.debug("done checking '" + getVerificationName() + "'");
  }

  @Override
  protected boolean doVerification() {
    return StringUtils.contains(getActualValue(), getSearchString());
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") String does not contain: '" + getSearchString() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") String contains: '" + getSearchString() + "'";
  }
}
