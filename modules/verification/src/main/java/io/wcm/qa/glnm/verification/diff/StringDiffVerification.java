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

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.persistence.util.TextSampleManager;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.StringToListSampler;

/**
 * Does a straightforward comparison of page source.
 */
public class StringDiffVerification<OS extends Sampler<List<String>>, IS extends Sampler<String>>
    extends SamplerBasedStringDiffVerification<OS> {

  private static final String DELIMITER = "\n";

  /**
   * Splitting at newlines.
   * @param verificationName name to use for reporting and logging
   * @param sampler input sampler
   */
  public StringDiffVerification(String verificationName, IS sampler) {
    this(verificationName, sampler, DELIMITER);
  }

  /**
   * @param verificationName name to use for reporting and logging
   * @param sampler input sampler
   * @param delimiter delimiter string fed to {@link StringUtils#split(String, String)}
   */
  @SuppressWarnings("unchecked")
  public StringDiffVerification(String verificationName, IS sampler, String delimiter) {
    super(
        verificationName,
        (OS)new StringToListSampler<Sampler<String>>(sampler, delimiter));
  }

  @Override
  protected List<String> initExpectedValue() {
    return TextSampleManager.getExpectedLines(getExpectedKey());
  }

  @Override
  protected void persistSample(String key, List<String> newValue) {
    TextSampleManager.addNewMultiLineSample(key, newValue);
  }

}
