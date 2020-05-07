/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.reporting;


import java.util.Stack;

/**
 * Allure step integration.
 *
 * @since 5.0.0
 */
public abstract class AllureNestedStepping {

  private String parentStep;
  protected final Stack<String> stepStack = new Stack<String>();

  /**
   * Constructor.
   *
   * @param uuid used as root parent step
   * @since 5.0.0
   */
  public AllureNestedStepping(String uuid) {
    super();
  }

  private String currentStep() {
    return stepStack.peek();
  }

  private String popStep() {
    return stepStack.pop();
  }

  private void pushStep(String uuid) {
    stepStack.push(uuid);
  }

  protected String getParentStep() {
    return parentStep;
  }

  protected void nestedStep(String stepName) {
    String uuid = GaleniumReportUtil.startStep(currentStep(), stepName);
    pushStep(uuid);
  }

  protected void rootStep(String stepName) {
    String uuid = GaleniumReportUtil.startStep(getParentStep(), stepName);
    pushStep(uuid);
  }

  protected void setParentStep(String parentStep) {
    this.parentStep = parentStep;
  }

  protected void stopStep() {
    GaleniumReportUtil.stopStep();
    popStep();
  }

}
