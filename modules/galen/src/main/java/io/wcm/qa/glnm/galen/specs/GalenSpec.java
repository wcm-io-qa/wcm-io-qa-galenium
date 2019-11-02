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
package io.wcm.qa.glnm.galen.specs;

import java.util.Collection;

import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Provide access to everything about this Galen spec.
 *
 * @since 4.0.0
 */
public class GalenSpec {

  private GalenPageSpecProvider galenSpecProvider;
  private PageSpec pageSpec;

  /**
   * Run the spec.
   *
   * @return representation of the run
   * @since 4.0.0
   */
  public GalenSpecRun check() {
    return GalenValidation.check(this);
  }

  /**
   * <p>
   * Getter for the field <code>galenSpecProvider</code>.
   * </p>
   *
   * @return a {@link io.wcm.qa.glnm.galen.specs.GalenPageSpecProvider} object.
   * @since 4.0.0
   */
  public GalenPageSpecProvider getGalenSpecProvider() {
    return galenSpecProvider;
  }


  /**
   * <p>
   * getObjects.
   * </p>
   *
   * @return a {@link java.util.Collection} object.
   */
  public Collection<NestedSelector> getObjects() {
    return GalenSpecUtil.getObjects(getPageSpec());
  }

  /**
   * <p>
   * Setter for the field <code>galenSpecProvider</code>.
   * </p>
   *
   * @param galenSpecProvider a {@link io.wcm.qa.glnm.galen.specs.GalenPageSpecProvider} object.
   * @since 4.0.0
   */
  public void setGalenSpecProvider(GalenPageSpecProvider galenSpecProvider) {
    this.galenSpecProvider = galenSpecProvider;
  }

  /**
   * <p>Getter for the field <code>pageSpec</code>.</p>
   *
   * @return a {@link com.galenframework.specs.page.PageSpec} object.
   */
  public PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = initPageSpec();
    }
    return pageSpec;
  }

  protected PageSpec initPageSpec() {
    return getGalenSpecProvider().getPageSpec();
  }

  /**
   * <p>getName.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 4.0.0
   */
  public String getName() {
    return getPageSpec().getSections().get(0).getName();
  }
}
