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
package io.wcm.qa.glnm.differences.difference.testcase;

import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;

import io.wcm.qa.glnm.differences.base.DifferenceBase;

/**
 * Differnce based on current test method.
 */
public class TestMethodNameDifference extends DifferenceBase {

  private static final String NO_TEST_METHOD = "no_method_name_ntm";
  private static final String NO_TEST_RESULT = "no_method_name_ntr";

  /**
   * Constructor.
   */
  public TestMethodNameDifference() {
    setMaxTagLength(50);
  }

  @Override
  protected String getRawTag() {

    ITestResult currentTestResult = Reporter.getCurrentTestResult();
    if (currentTestResult == null) {
      return NO_TEST_RESULT;
    }

    ITestNGMethod method = currentTestResult.getMethod();
    if (method == null) {
      return NO_TEST_METHOD;
    }

    return method.getMethodName();
  }

}
