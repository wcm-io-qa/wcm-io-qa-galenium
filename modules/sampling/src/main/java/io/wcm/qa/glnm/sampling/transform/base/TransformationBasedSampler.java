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

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.CanCache;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.base.CachingBasedSampler;

/**
 * Abstract base class for transforming samplers.
 *
 * @param <S> type of sampler providing the input
 * @param <I> type of input
 * @param <O> type of transformed output
 * @since 1.0.0
 */
public abstract class TransformationBasedSampler<S extends Sampler<I>, I, O> extends CachingBasedSampler<O> {

  private S input;
  private boolean nullTolerant;

  /**
   * <p>Constructor for TransformationBasedSampler.</p>
   *
   * @param inputSampler providing the input sample to transform
   * @since 3.0.0
   */
  public TransformationBasedSampler(S inputSampler) {
    setInput(inputSampler);
  }

  /** {@inheritDoc} */
  @Override
  public O freshSample() {
    I inputSample = getInput().sampleValue();
    if (inputSample == null) {
      return handleNullInputSample();
    }
    O outputSample = transform(inputSample);
    return outputSample;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCaching() {
    if (isCachingInput()) {
      return super.isCaching() && ((CanCache)getInput()).isCaching();
    }
    return super.isCaching();
  }

  /**
   * <p>Null tolerant transformations.</p>
   *
   * @return whether transformation can deal with null inputs.
   */
  public boolean isNullTolerant() {
    return nullTolerant;
  }

  /** {@inheritDoc} */
  @Override
  public void setCaching(boolean activateCache) {
    super.setCaching(activateCache);
    if (isCachingInput()) {
      ((CanCache)getInput()).setCaching(activateCache);
    }
  }

  /**
   * <p>
   * Allow null inputs.
   * </p>
   *
   * @param nullTolerant whether transform can deal with null
   */
  public void setNullTolerant(boolean nullTolerant) {
    this.nullTolerant = nullTolerant;
  }

  protected S getInput() {
    return input;
  }

  protected O handleNullInputSample() {
    if (isNullTolerant()) {
      // if transformation can handle it, give it the null input
      return transform((I)null);
    }
    throw new GaleniumException("Not transforming because input was null from: " + getInput());
  }

  protected boolean isCachingInput() {
    return getInput() instanceof CanCache;
  }

  protected void setInput(S input) {
    this.input = input;
  }

  protected abstract O transform(I inputSample);
}
