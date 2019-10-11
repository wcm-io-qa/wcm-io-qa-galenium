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

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Unusable sampler which throws an exception when sampled. Awkward workaround to avoid passing a null sampler.
 * @param <T>
 */
public final class UselessSampler<T> implements Sampler<T> {

  @Override
  public T sampleValue() {
    throw new GaleniumException("This sampler should never be used to sample.");
  }
}
