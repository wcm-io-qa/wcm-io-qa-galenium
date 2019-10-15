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

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import io.wcm.qa.glnm.sampling.Sampler;

/**
 * A {@link StringDiffVerification} that allows to add
 */
public class ScrubbableStringDiffVerification extends StringDiffVerification {

  private List<BiPredicate<String, String>> scrubbers = new ArrayList<BiPredicate<String, String>>();

  /**
   * @param verificationName for logging and reporting
   * @param sampler to fetch sample
   */
  @SuppressWarnings("unchecked")
  public ScrubbableStringDiffVerification(String verificationName, Sampler<String> sampler) {
    super(verificationName, sampler);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected BiPredicate<String, String> getDiffEqualizer() {
    return new ScrubbingEqualizer(super.getDiffEqualizer(), scrubbers);
  }

  /**
   * @param scrubber to append to scrubber list
   * @return this
   */
  public ScrubbableStringDiffVerification withScrubber(BiPredicate<String, String> scrubber) {
    scrubbers.add(scrubber);
    return this;
  }

  private static final class ScrubbingEqualizer implements BiPredicate<String, String> {

    private BiPredicate<String, String> baseEqualizer;
    private List<BiPredicate<String, String>> scrubbers;

    ScrubbingEqualizer(BiPredicate<String, String> diffEqualizer, List<BiPredicate<String, String>> scrubbers) {
      baseEqualizer = diffEqualizer;
      this.scrubbers = scrubbers;
    }

    @Override
    public boolean test(String t, String u) {
      if (baseEqualizer.test(t, u)) {
        return true;
      }
      for (BiPredicate<String, String> scrubber : scrubbers) {
        if (scrubber.test(t, u)) {
          return true;
        }
      }
      return false;
    }
  }

}
