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

import io.wcm.qa.glnm.sampling.CanCache;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.base.CachingBasedSampler;

/**
 * Abstract base class for transforming samplers.
 * @param <S> type of sampler providing the input
 * @param <I> type of input
 * @param <O> type of output
 */
public abstract class TransformationBasedSampler<S extends Sampler<I>, I, O> extends CachingBasedSampler<O> {

  private S input;

  /**
   * @param inputSampler providing the input sample to transform
   */
  public TransformationBasedSampler(S inputSampler) {
    setInput(inputSampler);
  }

  @Override
  public boolean isCaching() {
    if (isCachingInput()) {
      return super.isCaching() && ((CanCache)getInput()).isCaching();
    }
    return super.isCaching();
  }

  @Override
  public O freshSample() {
    I inputSample = getInput().sampleValue();
    O outputSample = transform(inputSample);
    return outputSample;
  }

  @Override
  public void setCaching(boolean activateCache) {
    super.setCaching(activateCache);
    if (isCachingInput()) {
      ((CanCache)getInput()).setCaching(activateCache);
    }
  }

  protected S getInput() {
    return input;
  }

  protected boolean isCachingInput() {
    return getInput() instanceof CanCache;
  }

  protected void setInput(S input) {
    this.input = input;
  }

  protected abstract O transform(I inputSample);
}
