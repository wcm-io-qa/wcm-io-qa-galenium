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
package io.wcm.qa.glnm.galen;

import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.galen.validation.GalenValidation;

/**
 * Provide access to everything about this Galen spec.
 *
 * @since 4.0.0
 */
public class GalenSpec {

  private GalenSpecProvider galenSpecProvider;
  private PageSpec pageSpec;

  /**
   * Run the spec.
   *
   * @return representation of the run
   */
  public GalenSpecRun check() {
    return new GalenSpecRun(this, GalenValidation.check(getPageSpec()));
  }

  public GalenSpecProvider getGalenSpecProvider() {
    return galenSpecProvider;
  }

  public void setGalenSpecProvider(GalenSpecProvider galenSpecProvider) {
    this.galenSpecProvider = galenSpecProvider;
  }

  protected PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = initPageSpec();
    }
    return pageSpec;
  }

  protected PageSpec initPageSpec() {
    return getGalenSpecProvider().getPageSpec();
  }
}
