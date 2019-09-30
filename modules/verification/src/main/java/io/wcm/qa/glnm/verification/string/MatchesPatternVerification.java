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

import java.util.regex.Pattern;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;
import io.wcm.qa.glnm.verification.string.base.PatternBasedVerification;

/**
 * Verifies pattern does not match whole input sample.
 */
public class MatchesPatternVerification extends PatternBasedVerification {

  /**
   * Pattern against fixed sample.
   * @param verificationName name for this check
   * @param pattern to find in input
   * @param sample fixed sample to verify against
   */
  public MatchesPatternVerification(String verificationName, Pattern pattern, String sample) {
    this(verificationName, pattern, new FixedStringSampler(sample));
  }

  /**
   * Pattern against input provided by sampler.
   * @param verificationName name for this check
   * @param pattern to find in input
   * @param sampler sampler to provide input sample
   */
  public MatchesPatternVerification(String verificationName, Pattern pattern, Sampler<String> sampler) {
    super(verificationName, pattern, sampler);
  }

  /**
   * String pattern against fixed sample.
   * @param verificationName name for this check
   * @param pattern to find in input
   * @param sample fixed sample to verify against
   */
  public MatchesPatternVerification(String verificationName, String pattern, String sample) {
    this(verificationName, Pattern.compile(pattern), new FixedStringSampler(sample));
  }

  /**
   * Pattern against input provided by sampler.
   * @param verificationName name for this check
   * @param pattern to find in input
   * @param sampler sampler to provide input sample
   */
  public MatchesPatternVerification(String verificationName, String pattern, Sampler<String> sampler) {
    this(verificationName, Pattern.compile(pattern), sampler);
  }

  @Override
  protected boolean doVerification() {
    return getActualMatcher().matches();
  }

  @Override
  protected String getFailureMessage() {
    return "(" + getVerificationName() + ") String does not match: '" + getPattern().pattern() + "'";
  }

  @Override
  protected String getSuccessMessage() {
    return "(" + getVerificationName() + ") String matches: '" + getPattern().pattern() + "'";
  }

}
