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
package io.wcm.qa.glnm.sampling.transform.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.Sampler;

/**
 * Abstract base class for implementations that transform string samples based on a regular expression.
 *
 * @param <S>
 * @param <O>
 * @since 1.0.0
 */
public abstract class RegexBasedSampler<S extends Sampler<String>, O> extends TransformationBasedSampler<S, String, O> {

  private static final int DEFAULT_FLAGS = 0;
  private Pattern pattern;

  /**
   * <p>Constructor for RegexBasedSampler.</p>
   *
   * @param inputSampler providing the input string
   * @param pattern used to transform
   */
  public RegexBasedSampler(S inputSampler, Pattern pattern) {
    super(inputSampler);
    setPattern(pattern);
  }

  /**
   * <p>Constructor for RegexBasedSampler.</p>
   *
   * @param inputSampler providing the input string
   * @param regex to build transformation {@link java.util.regex.Pattern} from
   */
  public RegexBasedSampler(S inputSampler, String regex) {
    this(inputSampler, regex, DEFAULT_FLAGS);
  }

  /**
   * <p>Constructor for RegexBasedSampler.</p>
   *
   * @param inputSampler providing the input string
   * @param regex to build transformation {@link java.util.regex.Pattern} from
   * @param flags to regular expression
   */
  public RegexBasedSampler(S inputSampler, String regex, int flags) {
    this(inputSampler, Pattern.compile(regex, flags));
  }

  /**
   * <p>Getter for the field <code>pattern</code>.</p>
   *
   * @return a {@link java.util.regex.Pattern} object.
   */
  public Pattern getPattern() {
    return pattern;
  }

  /**
   * <p>Setter for the field <code>pattern</code>.</p>
   *
   * @param pattern a {@link java.util.regex.Pattern} object.
   */
  public void setPattern(Pattern pattern) {
    this.pattern = pattern;
  }

  protected Matcher getMatcher(String inputSample) {
    return getPattern().matcher(inputSample);
  }

  protected String handleNoMatch() {
    return StringUtils.EMPTY;
  }

  protected abstract O extractValue(Matcher matcher);

  @Override
  protected O transform(String inputSample) {
    Matcher matcher = getMatcher(inputSample);
    return extractValue(matcher);
  }
}
