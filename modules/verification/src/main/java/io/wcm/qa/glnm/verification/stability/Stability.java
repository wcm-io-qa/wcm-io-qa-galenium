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
package io.wcm.qa.glnm.verification.stability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.CanCache;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.base.Verifiable;

/**
 * Abstract base class for implementations verifying the stability of samples. The mechanism is that two consecutive
 * samples have to be equal.
 *
 * @param <T> type of sample
 * @since 1.0.0
 */
public abstract class Stability<T> implements Verifiable {

  private static final Logger LOG = LoggerFactory.getLogger(Stability.class);

  private boolean firstRun = true;

  private T oldSampleValue;

  private Sampler<T> sampler;

  /**
   * <p>Constructor for Stability.</p>
   *
   * @param sampler to use in verification
   * @since 2.0.0
   */
  public Stability(Sampler<T> sampler) {
    setSampler(sampler);
  }

  /**
   * <p>Getter for the field <code>sampler</code>.</p>
   *
   * @return a  {@link io.wcm.qa.glnm.sampling.Sampler} object.
   * @since 2.0.0
   */
  public Sampler<T> getSampler() {
    return sampler;
  }

  /**
   * <p>Setter for the field <code>sampler</code>.</p>
   *
   * @param sampler to use in verification
   * @since 2.0.0
   */
  public void setSampler(Sampler<T> sampler) {
    if (sampler instanceof CanCache) {
      ((CanCache)sampler).setCaching(false);
    }
    this.sampler = sampler;
  }

  /** {@inheritDoc} */
  @Override
  public boolean verify() {
    LOG.debug(getClass().getSimpleName() + ": checking for stability");
    T currentSampleValue = getSampler().sampleValue();
    if (firstRun) {
      setOldSampleValue(currentSampleValue);
      firstRun = false;

      // first run can never succeed
      return false;
    }

    if (checkAgainstOldValue(currentSampleValue)) {
      return true;
    }
    setOldSampleValue(currentSampleValue);
    return false;
  }

  private boolean checkAgainstOldValue(T currentSampleValue) {
    if (getOldSampleValue() == null) {
      return currentSampleValue == null;
    }
    else if (currentSampleValue == null) {
      return false;
    }
    LOG.trace(getClass().getSimpleName() + ": both samples are non-null, now checking for equality.");
    return checkForEquality(getOldSampleValue(), currentSampleValue);
  }

  /**
   * If old and new value are equal, we have verified stability.
   * @param oldValue old value (guaranteed to not be null)
   * @param newValue new value (guaranteed to not be null)
   * @return whether the two values are equal
   */
  protected abstract boolean checkForEquality(T oldValue, T newValue);

  protected T getOldSampleValue() {
    return oldSampleValue;
  }

  protected void setOldSampleValue(T oldSampleValue) {
    this.oldSampleValue = oldSampleValue;
  }

}
