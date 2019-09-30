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
package io.wcm.qa.glnm.sampling.transform;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.SplittingSampler;

/**
 * @param <S> type of String sampler used
 */
public class StringToListSampler<S extends Sampler<String>> extends SplittingSampler<S, String, List<String>> {

  private String delimiter;

  /**
   * @param inputSampler to supply string
   */
  public StringToListSampler(S inputSampler) {
    this(inputSampler, null);
  }

  /**
   * @param inputSampler to supply string
   * @param delimiter to use when splitting
   */
  public StringToListSampler(S inputSampler, String delimiter) {
    super(inputSampler);
    setDelimiter(delimiter);
  }

  @Override
  protected List<String> split(String inputSample) {
    String[] split;
    if (getDelimiter() == null) {
      split = StringUtils.split(inputSample);
    }
    else {
      split = StringUtils.split(inputSample, getDelimiter());
    }
    return Arrays.asList(split);
  }

  public String getDelimiter() {
    return delimiter;
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

}
