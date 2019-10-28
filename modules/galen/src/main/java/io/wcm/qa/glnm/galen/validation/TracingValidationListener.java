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
import com.galenframework.validation.ValidationListener;
import com.galenframework.validation.ValidationResult;

final class TracingValidationListener implements ValidationListener {

  private static final Logger LOG = LoggerFactory.getLogger(TracingValidationListener.class);

  @Override
  public void onAfterObject(PageValidation pageValidation, String objectName) {
    LOG.trace("AfterObject(PageValidation pageValidation, String objectName)");
  }

  @Override
  public void onAfterPageAction(GalenPageAction action) {
    LOG.trace("AfterPageAction(GalenPageAction action)");
  }

  @Override
  public void onAfterSection(PageValidation pageValidation, PageSection pageSection) {
    LOG.trace("AfterSection(PageValidation pageValidation, PageSection pageSection)");
  }

  @Override
  public void onAfterSpecGroup(PageValidation pageValidation, String specGroupName) {
    LOG.trace("AfterSpecGroup(PageValidation pageValidation, String specGroupName)");
  }

  @Override
  public void onAfterSubLayout(PageValidation pageValidation, String objectName) {
    LOG.trace("AfterSubLayout(PageValidation pageValidation, String objectName)");
  }

  @Override
  public void onBeforePageAction(GalenPageAction action) {
    LOG.trace("BeforePageAction(GalenPageAction action)");
  }

  @Override
  public void onBeforeSection(PageValidation pageValidation, PageSection pageSection) {
    LOG.trace("BeforeSection(PageValidation pageValidation, PageSection pageSection)");
  }

  @Override
  public void onBeforeSpec(PageValidation pageValidation, String objectName, Spec spec) {
    LOG.trace("BeforeSpec(PageValidation pageValidation, String objectName, Spec spec)");
  }

  @Override
  public void onGlobalError(Exception e) {
    LOG.trace("GlobalError(Exception e)");
  }

  @Override
  public void onObject(PageValidation pageValidation, String objectName) {
    LOG.trace("Object(PageValidation pageValidation, String objectName)");
  }

  @Override
  public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
    LOG.trace("SpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult)");
  }

  @Override
  public void onSpecGroup(PageValidation pageValidation, String specGroupName) {
    LOG.trace("SpecGroup(PageValidation pageValidation, String specGroupName)");
  }

  @Override
  public void onSpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
    LOG.trace("SpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult)");
  }

  @Override
  public void onSubLayout(PageValidation pageValidation, String objectName) {
    LOG.trace("SubLayout(PageValidation pageValidation, String objectName)");
  }

  @Override
  public String toString() {
    return "Tracing Validation Listener";
  }
}
