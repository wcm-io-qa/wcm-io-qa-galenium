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
package io.wcm.qa.glnm.galen.imagecomparison;

import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.galen.specs.GalenSpecProvider;

/**
 * Provides executable Galen {@link com.galenframework.specs.page.PageSpec} given a definition.
 *
 * @since 4.0.0
 */
public class ImageComparisonProvider implements GalenSpecProvider {

  private IcsDefinition definition;

  /**
   * <p>Constructor for ImageComparisonProvider.</p>
   *
   * @param definition for generating the spec
   */
  public ImageComparisonProvider(IcsDefinition definition) {
    this.definition = definition;
  }

  protected IcsDefinition getDefinition() {
    return definition;
  }

  /** {@inheritDoc} */
  @Override
  public PageSpec getPageSpec() {
    return IcsFactory.getPageSpec(definition);
  }

  protected void setDefinition(IcsDefinition definition) {
    this.definition = definition;
  }

}
