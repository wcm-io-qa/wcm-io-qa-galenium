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
import java.util.function.BiPredicate;

import org.apache.commons.lang3.StringUtils;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.Patch;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.persistence.util.TextSampleManager;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.diff.base.SamplerBasedDiffVerification;

/**
 * Diff based verification for String based samplers.
 *
 * @param <S> sampler to use
 * @since 3.0.0
 */
public abstract class SamplerBasedStringDiffVerification<S extends Sampler<List<String>>> extends SamplerBasedDiffVerification<S, String, List<String>> {

  private boolean ignoreWhitespace;

  protected SamplerBasedStringDiffVerification(S sampler) {
    super(sampler);
  }

  /**
   * <p>isIgnoreWhitespace.</p>
   *
   * @return a boolean.
   */
  public boolean isIgnoreWhitespace() {
    return ignoreWhitespace;
  }

  /**
   * <p>Setter for the field <code>ignoreWhitespace</code>.</p>
   *
   * @param ignoreWhitespace a boolean.
   */
  public void setIgnoreWhitespace(boolean ignoreWhitespace) {
    this.ignoreWhitespace = ignoreWhitespace;
  }

  @Override
  protected void diff(List<String> expectedValue, List<String> actualValue) {
    Patch<String> diff;
    try {
      diff = DiffUtils.diff(expectedValue, actualValue, getDiffEqualizer());
      setDiffResult(diff);
    }
    catch (DiffException ex) {
      throw new GaleniumException("Malfunctioning diff.", ex);
    }
  }

  @Override
  protected BiPredicate<String, String> getDiffEqualizer() {
    if (isIgnoreWhitespace()) {
      return new WhitespaceIgnoringEqualizer();
    }
    return new WhitespaceSensitiveEqualizer();
  }

  @Override
  protected String getExpectedKey() {
    String expectedKey = getDifferences().asFilePath();
    if (StringUtils.isNotBlank(expectedKey)) {
      return getCleanedClassName() + "/" + expectedKey;
    }
    return getCleanedClassName();
  }

  @Override
  protected List<String> initExpectedValue() {
    return TextSampleManager.getExpectedLines(getExpectedKey());
  }

  @Override
  protected void persistSample(String key, List<String> newValue) {
    TextSampleManager.addNewMultiLineSample(getExpectedKey(), newValue);
  }

  private static final class WhitespaceIgnoringEqualizer implements BiPredicate<String, String> {

    @Override
    public boolean test(String s1, String s2) {
      CharSequence cs1 = StringUtils.strip(s1);
      CharSequence cs2 = StringUtils.strip(s2);
      return StringUtils.equals(cs1, cs2);
    }
  }

  private static final class WhitespaceSensitiveEqualizer implements BiPredicate<String, String> {

    @Override
    public boolean test(String s1, String s2) {
      return StringUtils.equals(s1, s2);
    }
  }
}
