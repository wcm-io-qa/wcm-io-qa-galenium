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
package io.wcm.qa.glnm.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.NetworkMode;
import com.relevantcodes.extentreports.model.Test;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Handles closing of reports a little more gracefully than the original.
 */
class GaleniumExtentReports extends ExtentReports {

  // Logger
  private static final Logger log = LoggerFactory.getLogger(GaleniumExtentReports.class);
  private static final long serialVersionUID = 1L;
  private boolean closed;

  private Map<String, ExtentTest> map = new HashMap<String, ExtentTest>();

  GaleniumExtentReports(String pathExtentReportsReport, NetworkMode networkMode) {
    super(pathExtentReportsReport, networkMode);
    log.info("init GaleniumExtentReports");
    if (GaleniumConfiguration.isSkipExtentReports()) {
      throw new GaleniumException("init despite skipping.");
    }
  }

  @Override
  public synchronized void close() {
    log.info("attempting closing GaleniumExtentReports");
    if (isClosed()) {
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
    setClosed(true);
    log.info("closed GaleniumExtentReports");
  }

  public ExtentTest getExtentTest(String name) {
    if (map.containsKey(name)) {
      return map.get(name);
    }
    return startTest(name, "");
  }

  @Override
  public synchronized ExtentTest startTest(String testName, String description) {
    log.info("starting ExtentTest: " + testName + " (" + description + ")");
    ExtentTest extentTest = super.startTest(testName, description);
    addExtentTest(extentTest);
    return extentTest;
  }

  private ExtentTest addExtentTest(ExtentTest extentTest) {
    return map.put(extentTest.getTest().getName(), extentTest);
  }

  private void closeAllTests() {
    log.info("closing all tests.");
    List<ExtentTest> tests = new ArrayList<ExtentTest>();
    tests.addAll(getTestList());

    closeTests(tests);
    if (!ListUtils.isEqualList(tests, getTestList())) {
      tests = getTestList();
      closeTests(tests);
    }
  }

  private void closeTests(List<ExtentTest> tests) {
    for (ExtentTest extentTest : tests) {
      Test test = (Test)extentTest.getTest();

      // ensure the same test isn't being closed twice
      if (!test.hasEnded) {
        log.debug("test not ended on close: " + test.getName());
        endTest(extentTest);
      }
    }
  }

  private boolean isClosed() {
    return closed;
  }

  private void setClosed(boolean closed) {
    this.closed = closed;
  }

  @Override
  protected void updateTestQueue(ExtentTest extentTest) {
    addExtentTest(extentTest);
    super.updateTestQueue(extentTest);
  }

}
