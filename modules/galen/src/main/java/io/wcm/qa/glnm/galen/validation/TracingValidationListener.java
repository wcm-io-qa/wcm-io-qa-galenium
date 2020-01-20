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
package io.wcm.qa.glnm.galen.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.specs.Spec;
import com.galenframework.specs.page.PageSection;
import com.galenframework.suite.GalenPageAction;
import com.galenframework.validation.PageValidation;
import com.galenframework.validation.ValidationResult;

final class TracingValidationListener extends AllureValidationListener {

  private static final Logger LOG = LoggerFactory.getLogger(TracingValidationListener.class);

  /**
   * <p>Constructor for TracingValidationListener.</p>
   *
   * @param uuid a {@link java.lang.String} object.
   */
  TracingValidationListener(String uuid) {
    super(uuid);
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterObject(PageValidation pageValidation, String objectName) {
    super.onAfterObject(pageValidation, objectName);
    LOG.trace("AfterObject(object:" + objectName + ")");
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterPageAction(GalenPageAction action) {
    super.onAfterPageAction(action);
    LOG.trace("AfterPageAction(GalenPageAction action)");
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterSection(PageValidation pageValidation, PageSection pageSection) {
    super.onAfterSection(pageValidation, pageSection);
    LOG.trace("AfterSection(pageSection:" + pageSection.getName() + ")");
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterSpecGroup(PageValidation pageValidation, String specGroupName) {
    LOG.trace("AfterSpecGroup(specGroup:" + specGroupName + ")");
    super.onAfterSpecGroup(pageValidation, specGroupName);
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterSubLayout(PageValidation pageValidation, String objectName) {
    LOG.trace("AfterSubLayout(object:" + objectName + ")");
    super.onAfterSubLayout(pageValidation, objectName);
  }

  /** {@inheritDoc} */
  @Override
  public void onBeforePageAction(GalenPageAction action) {
    LOG.trace("BeforePageAction(GalenPageAction action)");
    super.onBeforePageAction(action);
  }

  /** {@inheritDoc} */
  @Override
  public void onBeforeSection(PageValidation pageValidation, PageSection pageSection) {
    LOG.trace("BeforeSection(pageSection:" + pageSection.getName() + ")");
    super.onBeforeSection(pageValidation, pageSection);
  }

  /** {@inheritDoc} */
  @Override
  public void onBeforeSpec(PageValidation pageValidation, String objectName, Spec spec) {
    LOG.trace("BeforeSpec(object:" + objectName + ", Spec spec)");
    super.onBeforeSpec(pageValidation, objectName, spec);
  }

  /** {@inheritDoc} */
  @Override
  public void onGlobalError(Exception e) {
    LOG.trace("GlobalError(" + e.getMessage() + ")");
    super.onGlobalError(e);
  }

  /** {@inheritDoc} */
  @Override
  public void onObject(PageValidation pageValidation, String objectName) {
    LOG.trace("Object(object:" + objectName + ")");
    super.onObject(pageValidation, objectName);
  }

  /** {@inheritDoc} */
  @Override
  public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
    LOG.trace("SpecError(object:" + objectName + ", Spec spec, ValidationResult validationResult)");
    super.onSpecError(pageValidation, objectName, spec, validationResult);
  }

  /** {@inheritDoc} */
  @Override
  public void onSpecGroup(PageValidation pageValidation, String specGroupName) {
    LOG.trace("SpecGroup(specGroup:" + "specGroupName" + ")");
    super.onSpecGroup(pageValidation, specGroupName);
  }

  /** {@inheritDoc} */
  @Override
  public void onSpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
    LOG.trace("SpecSuccess(object:" + objectName + ", Spec spec, ValidationResult validationResult)");
    super.onSpecSuccess(pageValidation, objectName, spec, validationResult);
  }

  /** {@inheritDoc} */
  @Override
  public void onSubLayout(PageValidation pageValidation, String objectName) {
    LOG.trace("SubLayout(object:" + objectName + ")");
    super.onSubLayout(pageValidation, objectName);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Tracing Validation Listener";
  }
}
