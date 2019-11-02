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

import java.util.List;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationResult;

/**
 * Represents an execution of a Galen spec.
 *
 * @since 4.0.0
 */
public class GalenSpecRun {

  private LayoutReport report;
  private GalenSpec spec;

  /**
   * <p>Constructor for GalenSpecRun.</p>
   *
   * @param spec a {@link io.wcm.qa.glnm.galen.specs.GalenSpec} object.
   * @param report a {@link com.galenframework.reports.model.LayoutReport} object.
   */
  public GalenSpecRun(GalenSpec spec, LayoutReport report) {
    this.setSpec(spec);
    this.setReport(report);
  }

  /**
   * When there are errors that are not marked as warning level only.
   *
   * @return whether spec has failed
   * @since 4.0.0
   */
  public boolean hasFailed() {
    return getReport().errors() > 0;
  }

  /**
   * When there are errors that are not marked as warning level only.
   *
   * @return whether spec has failed
   * @since 4.0.0
   */
  public boolean hasWarnings() {
    return getReport().warnings() > 0;
  }


  /**
   * When there are no errors or warnings the spec is considered clean.
   *
   * @return whether spec is clean
   * @since 4.0.0
   */
  public boolean isClean() {
    return getValidationErrors().isEmpty();
  }

  /**
   * <p>getValidationErrors.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<ValidationResult> getValidationErrors() {
    return getReport().getValidationErrorResults();
  }

  protected LayoutReport getReport() {
    return report;
  }

  protected GalenSpec getSpec() {
    return spec;
  }

  protected void setReport(LayoutReport report) {
    this.report = report;
  }

  protected void setSpec(GalenSpec spec) {
    this.spec = spec;
  }

}
