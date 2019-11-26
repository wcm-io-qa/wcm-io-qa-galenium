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

import com.galenframework.specs.page.PageSpec;

/**
 * Parses Galen spec from file.
 *
 * @since 4.0.0
 */
public class GalenSpecParsingProvider implements GalenPageSpecProvider {

  private String specPath;

  /**
   * <p>Constructor for GalenSpecParsingProvider.</p>
   *
   * @param specPath a {@link java.lang.String} object.
   * @since 4.0.0
   */
  public GalenSpecParsingProvider(String specPath) {
    setSpecPath(specPath);
  }

  /** {@inheritDoc} */
  @Override
  public PageSpec getPageSpec() {
    return GalenParsing.fromPath(getSpecPath());
  }

  /**
   * <p>Getter for the field <code>specPath</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 4.0.0
   */
  public String getSpecPath() {
    return specPath;
  }

  /**
   * <p>Setter for the field <code>specPath</code>.</p>
   *
   * @param specPath a {@link java.lang.String} object.
   * @since 4.0.0
   */
  public void setSpecPath(String specPath) {
    this.specPath = specPath;
  }

}
