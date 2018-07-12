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
package io.wcm.qa.galenium.verification.general;

import java.util.regex.Pattern;

import io.wcm.qa.galenium.sampling.FixedStringSampler;
import io.wcm.qa.galenium.sampling.StringSampler;

public class DoesNotContainPatternVerification extends ContainsPatternVerification {

  public DoesNotContainPatternVerification(String verificationName, String pattern, String sample) {
    this(verificationName, Pattern.compile(pattern), new FixedStringSampler(sample));
  }

  public DoesNotContainPatternVerification(String verificationName, Pattern pattern, String sample) {
    this(verificationName, pattern, new FixedStringSampler(sample));
  }

  public DoesNotContainPatternVerification(String verificationName, String pattern, StringSampler sampler) {
    this(verificationName, Pattern.compile(pattern), sampler);
  }

  public DoesNotContainPatternVerification(String verificationName, Pattern pattern, StringSampler sampler) {
    super(verificationName, pattern, sampler);
  }

  @Override
  protected Boolean doVerification() {
    return !super.doVerification();
  }

  @Override
  protected String getSuccessMessage() {
    return super.getFailureMessage();
  }

  @Override
  protected String getFailureMessage() {
    return super.getSuccessMessage();
  }
}
