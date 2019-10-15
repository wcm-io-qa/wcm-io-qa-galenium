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
package io.wcm.qa.glnm.sampling.transform.base;

import java.util.Collection;

import io.wcm.qa.glnm.sampling.Sampler;

/**
 * Abstract Sampler to split single Sample into multiple objects. For example, splitting a String into multiple lines or
 * words.
 * @param <S> type input sampler
 * @param <I>
 * @param <O>
 */
public abstract class SplittingSampler<S extends Sampler<I>, I, O extends Collection<I>> extends TransformationBasedSampler<S, I, O> {

  protected SplittingSampler(S inputSampler) {
    super(inputSampler);
  }

  @Override
  protected O transform(I inputSample) {
    return split(inputSample);
  }

  protected abstract O split(I inputSample);

}
