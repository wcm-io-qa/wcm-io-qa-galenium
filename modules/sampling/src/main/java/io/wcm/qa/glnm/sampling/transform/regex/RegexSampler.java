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
package io.wcm.qa.glnm.sampling.transform.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.RegexBasedSampler;

/**
 * Samples first match of regular expression from input string.
 * @param <S>
 */
public class RegexSampler<S extends Sampler<String>> extends RegexBasedSampler<S, String> {

  /**
   * @param inputSampler providing the input string
   * @param pattern used to transform
   */
  public RegexSampler(S inputSampler, Pattern pattern) {
    super(inputSampler, pattern);
  }

  /**
   * @param inputSampler providing the input string
   * @param regex to build transformation {@link Pattern} from
   */
  public RegexSampler(S inputSampler, String regex) {
    super(inputSampler, regex);
  }

  /**
   * @param inputSampler providing the input string
   * @param regex to build transformation {@link Pattern} from
   * @param flags to regular expression
   */
  public RegexSampler(S inputSampler, String regex, int flags) {
    super(inputSampler, regex, flags);
  }


  @Override
  protected String extractValue(Matcher matcher) {
    if (matcher.find()) {
      getLogger().trace(getClass().getSimpleName() + ": found match for '" + getPattern() + "'");
      return matcher.group();
    }
    getLogger().trace(getClass().getSimpleName() + ": no match found for '" + getPattern() + "'");
    return handleNoMatch();
  }

}
