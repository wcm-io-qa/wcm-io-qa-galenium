/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.galenium.reporting;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

/**
 *
 */
public class GaleniumReportable implements ExtentReportable {


  private ExtentTest extentTest;
  private String testName;

  /**
   * @param testName name to use for reporting
   */
  public GaleniumReportable(String testName) {
    setTestName(testName);
  }

  @Override
  public ExtentReports getExtentReport() {
    return GalenReportUtil.getExtentReports();
  }

  @Override
  public ExtentTest getExtentTest() {
    if (extentTest == null) {
      extentTest = getExtentReport().startTest(getTestName());
    }
    return extentTest;
  }

  private String getTestName() {
    return testName;
  }

  private void setTestName(String testName) {
    this.testName = testName;
  }

}
