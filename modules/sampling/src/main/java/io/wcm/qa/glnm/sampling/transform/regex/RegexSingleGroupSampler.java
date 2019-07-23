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
 * Samples a specific group from first match of regular expression.
 * @param <S>
 */
public class RegexSingleGroupSampler<S extends Sampler<String>> extends RegexBasedSampler<S, String> {

  /** index of 0 means returning whole match */
  private int groupIndex;

  /**
   * @param inputSampler providing the input string
   * @param pattern used to transform
   */
  public RegexSingleGroupSampler(S inputSampler, Pattern pattern) {
    super(inputSampler, pattern);
  }

  /**
   * @param inputSampler providing the input string
   * @param pattern used to transform
   * @param groupIndex index of group to sample
   */
  public RegexSingleGroupSampler(S inputSampler, Pattern pattern, int groupIndex) {
    this(inputSampler, pattern);
    setGroupIndex(groupIndex);
  }


  /**
   * @param inputSampler providing the input string
   * @param regex to build transformation {@link Pattern} from
   */
  public RegexSingleGroupSampler(S inputSampler, String regex) {
    super(inputSampler, regex);
  }

  /**
   * @param inputSampler providing the input string
   * @param regex to build transformation {@link Pattern} from
   * @param groupIndex index of group to sample
   */
  public RegexSingleGroupSampler(S inputSampler, String regex, int groupIndex) {
    this(inputSampler, regex);
    setGroupIndex(groupIndex);
  }

  protected int getGroupIndex() {
    return groupIndex;
  }

  protected void setGroupIndex(int groupIndex) {
    this.groupIndex = groupIndex;
  }

  @Override
  protected String extractValue(Matcher matcher) {
    if (matcher.find()) {
      return matcher.group(getGroupIndex());
    }
    return handleNoMatch();
  }

}
