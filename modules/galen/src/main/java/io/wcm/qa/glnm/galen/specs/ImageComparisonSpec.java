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

import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.galen.specs.imagecomparison.IcValidationListener;
import io.wcm.qa.glnm.galen.specs.imagecomparison.IcsDefinition;
import io.wcm.qa.glnm.galen.specs.imagecomparison.ImageComparisonProvider;

/**
 * <p>ImageComparisonSpec class.</p>
 *
 * @since 4.0.0
 */
public class ImageComparisonSpec extends AbstractGalenSpec {

  /**
   * <p>
   * Constructor for ImageComparisonSpec.
   * </p>
   *
   * @param specDefinition a {@link io.wcm.qa.glnm.galen.specs.imagecomparison.IcsDefinition} object.
   */
  public ImageComparisonSpec(IcsDefinition specDefinition) {
    super(new ImageComparisonProvider(specDefinition));
  }

  @Override
  protected ValidationListener getValidationListener() {
    return new IcValidationListener();
  }
}
