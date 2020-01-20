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

import java.util.Stack;

import com.galenframework.specs.Spec;
import com.galenframework.specs.page.PageSection;
import com.galenframework.suite.GalenPageAction;
import com.galenframework.validation.PageValidation;
import com.galenframework.validation.ValidationResult;

import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

class AllureValidationListener extends NoOpValidationListener {

  private Stack<String> stepStack = new Stack<String>();
  private String parentStep;

  /**
   * <p>Constructor for AllureValidationListener.</p>
   *
   * @param uuid a {@link java.lang.String} object.
   */
  AllureValidationListener(String uuid) {
    parentStep = uuid;
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterObject(PageValidation pageValidation, String objectName) {
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterPageAction(GalenPageAction action) {
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterSection(PageValidation pageValidation, PageSection pageSection) {
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterSpecGroup(PageValidation pageValidation, String specGroupName) {
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onAfterSubLayout(PageValidation pageValidation, String objectName) {
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onBeforePageAction(GalenPageAction action) {
    nestedStep(action.getOriginalCommand());
  }

  /** {@inheritDoc} */
  @Override
  public void onBeforeSection(PageValidation pageValidation, PageSection pageSection) {
    if (stepStack.isEmpty()) {
      rootStep(pageSection.getName());
      return;
    }
    nestedStep(pageSection.getName());
  }

  /** {@inheritDoc} */
  @Override
  public void onBeforeSpec(PageValidation pageValidation, String objectName, Spec spec) {
    nestedStep(generateStepName(objectName, spec));
  }

  /** {@inheritDoc} */
  @Override
  public void onGlobalError(Exception e) {
    // not sure how to handle, so doing nothing
  }

  /** {@inheritDoc} */
  @Override
  public void onObject(PageValidation pageValidation, String objectName) {
    nestedStep(objectName);
  }

  /** {@inheritDoc} */
  @Override
  public void onSpecError(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
    for (String step : stepStack) {
      GaleniumReportUtil.failStep(step);
    }
    GaleniumReportUtil.failStep(parentStep);
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onSpecGroup(PageValidation pageValidation, String specGroupName) {
    nestedStep(specGroupName);
  }

  /** {@inheritDoc} */
  @Override
  public void onSpecSuccess(PageValidation pageValidation, String objectName, Spec spec, ValidationResult validationResult) {
    for (String step : stepStack) {
      GaleniumReportUtil.passStep(step);
    }
    GaleniumReportUtil.passStep(parentStep);
    stopStep();
  }

  /** {@inheritDoc} */
  @Override
  public void onSubLayout(PageValidation pageValidation, String objectName) {
    nestedStep(objectName);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Allure Validation Listener";
  }

  private String currentStep() {
    return stepStack.peek();
  }

  private String generateStepName(String objectName, Spec spec) {
    return objectName + " " + spec.toText();
  }

  private void nestedStep(String stepName) {
    String uuid = GaleniumReportUtil.startStep(currentStep(), stepName);
    pushStep(uuid);
  }

  private String popStep() {
    return stepStack.pop();
  }

  private void pushStep(String uuid) {
    stepStack.push(uuid);
  }

  private void rootStep(String stepName) {
    String uuid = GaleniumReportUtil.startStep(parentStep, stepName);
    pushStep(uuid);
  }

  private void stopStep() {
    GaleniumReportUtil.stopStep();
    popStep();
  }

}
