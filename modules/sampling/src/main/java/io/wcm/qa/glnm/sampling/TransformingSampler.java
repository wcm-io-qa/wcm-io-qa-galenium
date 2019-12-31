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
package io.wcm.qa.glnm.sampling;

/**
 * Generates sample based on sample from input sampler.
 *
 * @param <IS> input sampler to get input from
 * @param <I> type of input
 * @param <O> type of output
 * @since 4.0.0
 */
public interface TransformingSampler<IS extends Sampler<I>, I, O> extends Sampler<O> {

  /**
   * <p>getInput.</p>
   *
   * @return the input sampler
   * @since 4.0.0
   */
  IS getInput();

}
