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

import org.apache.commons.collections4.IterableUtils;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.TransformationBasedSampler;

/**
 * Counts number of elements in input sample.
 *
 * @since 4.0.0
 */
public class CountingSampler<I> extends TransformationBasedSampler<Sampler<Iterable<I>>, Iterable<I>, Integer> {

  /**
   * <p>Constructor for CountingSampler.</p>
   *
   * @param inputSampler used to fetch elements to count
   * @since 4.0.0
   */
  public CountingSampler(Sampler<Iterable<I>> inputSampler) {
    super(inputSampler);
  }

  @Override
  protected Integer transform(Iterable inputSample) {
    return IterableUtils.size(inputSample);
  }

}
