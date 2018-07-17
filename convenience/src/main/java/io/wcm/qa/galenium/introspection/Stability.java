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
package io.wcm.qa.galenium.introspection;

import io.wcm.qa.galenium.sampling.Sampler;
import io.wcm.qa.galenium.verification.base.Verifiable;


public abstract class Stability<T> implements Verifiable {

  private Sampler<T> sampler;

  private boolean firstRun = true;

  private T oldSampleValue;

  public Stability(Sampler<T> sampler) {
    setSampler(sampler);
  }

  @Override
  public boolean verify() {
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

    return checkForEquality(getOldSampleValue(), currentSampleValue);
  }

  /**
   * @param value1 old value (guaranteed to not be null)
   * @param value2 new value (guaranteed to not be null)
   * @return whether the two values are equal
   */
  protected abstract boolean checkForEquality(T value1, T value2);

  public Sampler<T> getSampler() {
    return sampler;
  }

  public void setSampler(Sampler<T> sampler) {
    this.sampler = sampler;
  }

  private T getOldSampleValue() {
    return oldSampleValue;
  }

  private void setOldSampleValue(T oldSampleValue) {
    this.oldSampleValue = oldSampleValue;
  }

}
