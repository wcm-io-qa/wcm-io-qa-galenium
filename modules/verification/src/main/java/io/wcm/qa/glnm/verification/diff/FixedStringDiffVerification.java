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
package io.wcm.qa.glnm.verification.diff;

import java.util.List;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.string.FixedStringSampler;

/**
 * Diffing a String not fetched with sampler.
 *
 * @since 3.0.0
 */
public class FixedStringDiffVerification extends StringDiffVerification<Sampler<List<String>>, Sampler<String>> {

  /**
   * <p>Constructor for FixedStringDiffVerification.</p>
   *
   * @param verificationName name to use in logging
   * @param actualValue fixed string to diff
   */
  public FixedStringDiffVerification(String verificationName, String actualValue) {
    super(verificationName, new FixedStringSampler(actualValue));
  }

}
