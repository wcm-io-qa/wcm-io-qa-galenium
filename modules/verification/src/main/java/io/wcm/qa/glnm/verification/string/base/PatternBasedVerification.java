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
package io.wcm.qa.glnm.verification.string.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.wcm.qa.glnm.sampling.Sampler;

/**
 * Abstract base class to verify strings based on regular expression.
 */
public abstract class PatternBasedVerification extends StringSamplerBasedVerification {

  private Matcher matcher;
  private Pattern pattern;

  protected PatternBasedVerification(String verificationName, Pattern pattern, Sampler<String> sampler) {
    super(verificationName, sampler);
    setPattern(pattern);
  }

  public Pattern getPattern() {
    return pattern;
  }

  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  @Override
  protected void afterVerification() {
    getLogger().debug("done checking '" + getVerificationName() + "'");
  }

  protected Matcher getActualMatcher() {
    if (matcher == null || !isCaching()) {
      matcher = getPattern().matcher(getActualValue());
    }
    return matcher;
  }

  protected Matcher getCachedMatcher() {
    return matcher;
  }
}
