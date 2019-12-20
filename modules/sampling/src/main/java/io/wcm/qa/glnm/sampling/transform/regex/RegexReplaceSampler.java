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
package io.wcm.qa.glnm.sampling.transform.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.RegexBasedSampler;

/**
 * Uses Java's regular expressions to replace either first or all matches of a Pattern.
 *
 * @param <S> type of string input sampler
 * @since 4.0.0
 */
public class RegexReplaceSampler<S extends Sampler<String>> extends RegexBasedSampler<S, String> {

  private boolean replaceAll;
  private String replacement;

  /**
   * Constructor with all parameters.
   *
   * @param inputSampler provides input to be matched against
   * @param pattern used to identify matches to replace
   * @param replacement replacement string
   * @param replaceAll whether to replace all or just the first occurence
   * @since 4.0.0
   */
  public RegexReplaceSampler(S inputSampler, Pattern pattern, String replacement, boolean replaceAll) {
    super(inputSampler, pattern);
    setReplacement(replacement);
    setReplaceAll(replaceAll);
  }

  /**
   * Constructor to replace first match.
   *
   * @param inputSampler provides input to be matched against
   * @param pattern used to identify matches to replace
   * @param replacement replacement string
   * @since 4.0.0
   */
  public RegexReplaceSampler(S inputSampler, Pattern pattern, String replacement) {
    this(inputSampler, pattern, replacement, false);
  }

  /**
   * Constructor to remove first match. Uses empty string as replacement. Use setters to modify behavior.
   *
   * @param inputSampler provides input to be matched against
   * @param pattern used to identify matches to replace
   * @since 4.0.0
   */
  public RegexReplaceSampler(S inputSampler, Pattern pattern) {
    this(inputSampler, pattern, StringUtils.EMPTY);
  }

  /**
   * <p>Getter for the field <code>replacement</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 4.0.0
   */
  public String getReplacement() {
    return replacement;
  }

  /**
   * <p>isReplaceAll.</p>
   *
   * @return a boolean.
   * @since 4.0.0
   */
  public boolean isReplaceAll() {
    return replaceAll;
  }

  /**
   * <p>Setter for the field <code>replaceAll</code>.</p>
   *
   * @param replaceAll a boolean.
   * @since 4.0.0
   */
  public void setReplaceAll(boolean replaceAll) {
    this.replaceAll = replaceAll;
  }

  /**
   * <p>Setter for the field <code>replacement</code>.</p>
   *
   * @param replacement a {@link java.lang.String} object.
   * @since 4.0.0
   */
  public void setReplacement(String replacement) {
    this.replacement = replacement;
  }

  @Override
  protected String extractValue(Matcher matcher) {
    if (isReplaceAll()) {
      return matcher.replaceAll(getReplacement());
    }
    return matcher.replaceFirst(getReplacement());
  }

}
