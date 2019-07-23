/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.glnm.verification.element.base;

import io.wcm.qa.glnm.sampling.element.base.WebElementBasedSampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.base.SamplerBasedVerification;

/**
 * Base class encapsulating common functionality to verify aspects of elements.
 */
public abstract class WebElementBasedVerification<S extends WebElementBasedSampler<T>, T> extends SamplerBasedVerification<S, T> {

  protected WebElementBasedVerification(String verificationName, S sampler) {
    super(verificationName, sampler);
  }

  protected String getElementName() {
    return getSelector().elementName();
  }

  protected Selector getSelector() {
    return getSampler().getSelector();
  }
}
