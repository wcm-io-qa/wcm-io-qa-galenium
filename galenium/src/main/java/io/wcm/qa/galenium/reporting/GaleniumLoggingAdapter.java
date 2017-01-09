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

import static com.relevantcodes.extentreports.LogStatus.FAIL;
import static com.relevantcodes.extentreports.LogStatus.FATAL;
import static com.relevantcodes.extentreports.LogStatus.INFO;
import static com.relevantcodes.extentreports.LogStatus.PASS;
import static com.relevantcodes.extentreports.LogStatus.UNKNOWN;
import static com.relevantcodes.extentreports.LogStatus.WARNING;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.TestAttribute;

/**
 * Wraps a {@link ExtentReportable} and supplies methods for reporting. The reporting methods will always write to the
 * reports. The debug methods will only write when {@link GaleniumConfiguration#isSparseReporting()} is false.
 */
public class GaleniumLoggingAdapter implements GaleniumLogging {

  private ExtentReportable reportable;
  private String stepName;

  /**
   * @param reportable {@link ExtentReportable} to be used for logging
   */
  public GaleniumLoggingAdapter(ExtentReportable reportable) {
    setReportable(reportable);
  }

  @Override
  public void assignCategory(String extentCategory) {
    ExtentTest extentTest = getReportable().getExtentTest();
    List<TestAttribute> categoryList = extentTest.getTest().getCategoryList();
    for (TestAttribute testAttribute : categoryList) {
      if (StringUtils.equals(extentCategory, testAttribute.getName())) {
        // only add category once
        return;
      }
    }
    extentTest.assignCategory(extentCategory);
  }

  private void debug(LogStatus logStatus, String msg) {
    if (isDebugging()) {
      report(logStatus, msg);
    }
  }

  private void debug(LogStatus logStatus, Throwable ex) {
    if (isDebugging()) {
      report(logStatus, ex);
    }
  }

  @Override
  public void debugAndIgnoreException(Throwable ex) {
    debug(UNKNOWN, ex);
  }

  @Override
  public void debugException(Throwable ex) {
    debug(FAIL, ex);
  }

  @Override
  public void debugFailed(String msg) {
    debug(FAIL, msg);
  }

  @Override
  public void debugInfo(String msg) {
    debug(INFO, msg);
  }

  @Override
  public void debugPassed(String msg) {
    debug(PASS, msg);
  }

  @Override
  public void debugSkip(String msg) {
    report(LogStatus.SKIP, msg);
  }

  @Override
  public void debugUnknown(String msg) {
    debug(UNKNOWN, msg);
  }

  @Override
  public void debugWarning(String msg) {
    debug(WARNING, msg);
  }

  public ExtentReportable getReportable() {
    return reportable;
  }

  private void report(LogStatus logStatus, String msg) {
    if (hasStepName()) {
      getReportable().getExtentTest().log(logStatus, getStepName(), msg);
    }
    else {
      getReportable().getExtentTest().log(logStatus, msg);
    }
  }

  private void report(LogStatus logStatus, Throwable ex) {
    if (hasStepName()) {
      getReportable().getExtentTest().log(logStatus, getStepName(), ex);
    }
    else {
      getReportable().getExtentTest().log(logStatus, ex);
    }
  }

  @Override
  public void reportException(Throwable ex) {
    report(FAIL, ex);
  }

  @Override
  public void reportFailed(String msg) {
    report(FAIL, msg);
  }

  @Override
  public void reportFatal(String msg) {
    report(FATAL, msg);
  }

  @Override
  public void reportFatalException(Throwable ex) {
    report(FATAL, ex);
  }

  @Override
  public void reportInfo(String msg) {
    report(INFO, msg);
  }

  @Override
  public void reportPassed(String msg) {
    report(PASS, msg);
  }

  @Override
  public void reportSkip(String msg) {
    report(LogStatus.SKIP, msg);
  }

  @Override
  public void reportWarning(String msg) {
    report(WARNING, msg);
  }

  public void setReportable(ExtentReportable reportable) {
    this.reportable = reportable;
  }

  private boolean hasStepName() {
    return StringUtils.isNotBlank(getStepName());
  }

  public String getStepName() {
    return stepName;
  }

  public void setStepName(String stepName) {
    this.stepName = stepName;
  }

  @Override
  public boolean isDebugging() {
    return !GaleniumConfiguration.isSparseReporting();
  }

}
