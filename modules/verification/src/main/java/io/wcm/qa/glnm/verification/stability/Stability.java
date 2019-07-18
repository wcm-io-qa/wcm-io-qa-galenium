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

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.sampling.CanCache;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.base.Verifiable;

/**
 * Abstract base class for implementations verifying the stability of samples. The mechanism is that two consecutive
 * samples have to be equal.
 * @param <T> type of sample
 */
public abstract class Stability<T> implements Verifiable {

  private boolean firstRun = true;

  private T oldSampleValue;

  private Sampler<T> sampler;

  /**
   * @param sampler to use in verification
   */
  public Stability(Sampler<T> sampler) {
    setSampler(sampler);
  }

  public Sampler<T> getSampler() {
    return sampler;
  }

  /**
   * @param sampler to use in verification
   */
  public void setSampler(Sampler<T> sampler) {
    if (sampler instanceof CanCache) {
      ((CanCache)sampler).setCaching(false);
    }
    this.sampler = sampler;
  }

  @Override
  public boolean verify() {
    getLogger().debug(getClass().getSimpleName() + ": checking for stability");
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
    getLogger().trace(getClass().getSimpleName() + ": both samples are non-null, now checking for equality.");
    return checkForEquality(getOldSampleValue(), currentSampleValue);
  }

  /**
   * If old and new value are equal, we have verified stability.
   * @param oldValue old value (guaranteed to not be null)
   * @param newValue new value (guaranteed to not be null)
   * @return whether the two values are equal
   */
  protected abstract boolean checkForEquality(T oldValue, T newValue);

  protected Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  protected T getOldSampleValue() {
    return oldSampleValue;
  }

  protected void setOldSampleValue(T oldSampleValue) {
    this.oldSampleValue = oldSampleValue;
  }

}
