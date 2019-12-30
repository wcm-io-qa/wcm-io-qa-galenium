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
package io.wcm.qa.glnm.listeners.testng;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.util.ResultsUtils;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * <p>AllureInfoListener class.</p>
 *
 * @since 4.0.0
 */
public class AllureInfoListener extends TestListenerAdapter {

  /** Constant <code>CONTEXT_KEY_ALLURE_PARAMETERS="allure-parameters"</code> */
  public static final String CONTEXT_KEY_ALLURE_PARAMETERS = "allure-parameters";

  /** {@inheritDoc} */
  @Override
  public void onTestStart(ITestResult result) {
    ParameterUpdater updater = new ParameterUpdater();
    Object[] factoryParameters = result.getFactoryParameters();
    for (Object factoryParameter : factoryParameters) {
      addParameter(updater, factoryParameter);
    }
    GaleniumContext.put(CONTEXT_KEY_ALLURE_PARAMETERS, updater);
    Allure.step("Running on thread: '" + Thread.currentThread().getName() + "'");
  }

  private void addParameter(ParameterUpdater updater, Object factoryParameter) {
    if (factoryParameter instanceof TestDevice) {
      TestDevice device = (TestDevice)factoryParameter;
      updater.addParameter("Browser", device.getBrowserType().getBrowser());
      updater.addParameter("Size", device.getScreenSize().toString());
      return;
    }
    String name = factoryParameter.getClass().getName();
    updater.addParameter(name, factoryParameter.toString());
  }

  private static final class ParameterUpdater implements Consumer<TestResult> {

    private List<Parameter> additionalParameters = new ArrayList<Parameter>();

    @Override
    public void accept(TestResult result) {
      @SuppressWarnings("deprecation")
      List<Parameter> parameters = result.getParameters();
      List<Parameter> updatedParameters = update(parameters);
      result.setParameters(updatedParameters);
    }

    private List<Parameter> additionalParameters() {
      return additionalParameters;
    }

    private void addParameter(String name, String value) {
      additionalParameters.add(ResultsUtils.createParameter(name, value));
    }

    private List<Parameter> update(List<Parameter> parameters) {
      List<Parameter> updated = new ArrayList<Parameter>();
      updated.addAll(parameters);
      updated.addAll(additionalParameters());
      return updated;
    }

  }

}
