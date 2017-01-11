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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.model.Test;

/**
 * Handles closing of reports a little more gracefully than the original.
 */
class GaleniumExtentReports extends ExtentReports {

  private static final long serialVersionUID = 1L;
  private boolean closed;
  private Map<String, ExtentTest> map = new HashMap<String, ExtentTest>();

  // Logger
  private static final Logger log = LoggerFactory.getLogger(GaleniumExtentReports.class);

  GaleniumExtentReports(String pathExtentReportsReport, NetworkMode networkMode) {
    super(pathExtentReportsReport, networkMode);
  }

  @Override
  public synchronized void close() {
    if (closed) {
      StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
      StringBuilder stacktraceString = new StringBuilder();
      for (StackTraceElement stackTraceElement : stackTrace) {
        stacktraceString.append(stackTraceElement.toString());
        stacktraceString.append("\n");
      }
      log.error("attempting to close closed ExtentReports:\n" + stacktraceString.toString());
    }
    closeAllTests();
    map.clear();
    super.close();
  }

  private void closeAllTests() {
    List<ExtentTest> tests = getTestList();

    for (ExtentTest extentTest : tests) {
      Test test = (Test)extentTest.getTest();

      // ensure the same test isn't being closed twice
      if (!test.hasEnded) {
        log.debug("test not ended on close: " + test.getName());
        endTest(extentTest);
      }
    }
  }

  @Override
  protected void updateTestQueue(ExtentTest extentTest) {
    addExtentTest(extentTest);
    super.updateTestQueue(extentTest);
  }

  private ExtentTest addExtentTest(ExtentTest extentTest) {
    return map.put(extentTest.getTest().getName(), extentTest);
  }

  public ExtentTest getExtentTest(String name) {
    if (!map.containsKey(name)) {
      ExtentTest extentTest = new ExtentTest(name, "");
      addExtentTest(extentTest);
      return extentTest;
    }
    return map.get(name);
  }

}
