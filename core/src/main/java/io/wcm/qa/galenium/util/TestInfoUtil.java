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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_FAIL;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_WARN;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;
import org.testng.ITestResult;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.TestReport;
import com.galenframework.reports.TestStatistic;
import com.galenframework.reports.nodes.ReportExtra;
import com.galenframework.reports.nodes.TestReportNode;
import com.relevantcodes.extentreports.ExtentTest;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.webdriver.HasDevice;

/**
 * Utility class to assist with extracting information about test parameters to be used in reporting.
 */
public final class TestInfoUtil {

  private static final String EXTENT_CATEGORY_PREFIX_MEDIA_QUERIES = System.getProperty("galenium.mediaquery.extentCategory", "");
  private static final String EXTENT_CATEGORY_PREFIX_BROWSER = System.getProperty("galenium.extent.category.browser", "BROWSER-");
  private static final String EXTENT_CATEGORY_PREFIX_TEST_NG = System.getProperty("galenium.extent.category.testNG", "testNG-");

  private TestInfoUtil() {
    // do not instantiate
  }

  /**
   * @param test to add categories to
   * @param result source for TestNG groups, browser, and breakpoints
   */
  public static void assignCategories(ExtentTest test, ITestResult result) {

    for (String group : result.getMethod().getGroups()) {
      test.assignCategory(EXTENT_CATEGORY_PREFIX_TEST_NG + group);
    }

    String browser = getBrowser(result);
    if (browser != null) {
      test.assignCategory(EXTENT_CATEGORY_PREFIX_BROWSER + browser);
    }

    List<String> breakPoints = getBreakPoint(result);
    for (String breakPoint : breakPoints) {
      test.assignCategory(EXTENT_CATEGORY_PREFIX_MEDIA_QUERIES + breakPoint);
    }
  }

  /**
   * Gets test device if test case in this result implements {@link HasDevice}.
   * @param result test result to retrieve test case from
   * @return device if one was found
   */
  public static TestDevice getTestDevice(ITestResult result) {
    Object testClass = result.getInstance();
    if (testClass instanceof HasDevice) {
      Object device = ((HasDevice)testClass).getDevice();
      if (device instanceof TestDevice) {
        return (TestDevice)device;
      }
    }
    return null;
  }

  /**
   * @param testInfo to check
   * @return whether there are warnings in the results
   */
  public static boolean hasWarnings(GalenTestInfo testInfo) {
    TestReport report = testInfo.getReport();
    if (report == null) {
      getLogger().trace("report was null: " + testInfo);
      return false;
    }
    TestStatistic statistics = report.fetchStatistic();
    if (statistics == null) {
      getLogger().trace("statistics were null: " + testInfo + "->" + report);
      return false;
    }

    return statistics.getWarnings() > 0;
  }

  /**
   * @param testInfo to check
   * @return whether test failed
   */
  public static boolean isFailed(GalenTestInfo testInfo) {
    return testInfo.isFailed();
  }

  /**
   * @param testInfo to log
   */
  public static void logGalenTestInfo(GalenTestInfo testInfo) {
    if (isFailed(testInfo)) {
      getLogger().info(MARKER_FAIL, "failed: " + testInfo.getName());
    }
    else if (hasWarnings(testInfo)) {
      getLogger().info(MARKER_WARN, "warnings: " + testInfo.getName());
    }
    else {
      getLogger().info(GaleniumReportUtil.MARKER_PASS, "passed: " + testInfo.getName());
    }
    if (getLogger().isDebugEnabled()) {
      List<TestReportNode> nodes = testInfo.getReport().getNodes();
      int nodeCounter = 0;
      for (TestReportNode testReportNode : nodes) {
        logTestReportNode(testReportNode, "node" + nodeCounter++);
      }
    }
  }

  private static void logTestReportNode(TestReportNode node, String prefix) {
    Marker marker;
    switch (node.getStatus()) {
      case WARN:
        marker = GaleniumReportUtil.MARKER_WARN;
        break;
      case ERROR:
        marker = GaleniumReportUtil.MARKER_FAIL;
        break;
      case INFO:
      default:
        marker = GaleniumReportUtil.MARKER_PASS;
        break;
    }
    String type = node.getType();
    if (StringUtils.equals("layout", type)) {
      getLogger().info(marker, prefix + ".name: " + node.getName());
    }
    else {
      getLogger().debug(marker, prefix + ".name: " + node.getName());
    }
    getLogger().trace(marker, prefix + ".type: " + type);
    List<String> attachments = node.getAttachments();
    if (attachments != null) {
      for (String attachment : attachments) {
        getLogger().debug(marker, prefix + ".attachment: " + attachment);
      }
    }
    else {
      getLogger().trace(marker, prefix + ".attachments: none");
    }
    Map<String, ReportExtra> reportExtras = node.getExtras();
    if (reportExtras != null) {
      Set<Entry<String, ReportExtra>> extras = reportExtras.entrySet();
      for (Entry<String, ReportExtra> entry : extras) {
        getLogger().debug(marker, prefix + ".extra: " + entry.getKey() + "=" + entry.getValue());
      }
    }
    else {
      getLogger().trace(marker, prefix + ".extras: none");
    }
    List<TestReportNode> nodes = node.getNodes();
    if (nodes != null) {
      int childCounter = 0;
      for (TestReportNode childNode : nodes) {
        logTestReportNode(childNode, prefix + "." + childCounter++);
      }
    }
    else {
      getLogger().trace(marker, prefix + ".children: none");
    }
  }


  /**
   * Replaces all non-alphanumeric characters with underscore.
   * @param result to extract test name from
   * @return testname containing only characters matched by <i>[-_A-Za-z0-9]</i>
   */
  public static String getAlphanumericTestName(ITestResult result) {
    String name = result.getName();
    return name.replaceAll("[^-A-Za-z0-9]", "_");
  }

  static List<String> getBreakPoint(ITestResult result) {
    TestDevice testDevice = getTestDevice(result);
    if (testDevice != null) {
      return testDevice.getTags();
    }

    String name = result.getName();
    String breakPoint = name.replaceFirst(".*profile ", "");
    breakPoint = breakPoint.replaceFirst(" \\(.*", "");
    ArrayList<String> breakPointFromName = new ArrayList<String>();
    breakPointFromName.add(breakPoint);
    return breakPointFromName;
  }

  static String getBrowser(ITestResult result) {
    TestDevice testDevice = getTestDevice(result);
    if (testDevice != null) {
      return testDevice.getBrowserType().getBrowser();
    }

    String name = result.getName();
    String browser = name.replaceFirst(".*using ", "");
    browser = browser.replaceFirst(" \\(.*", "");
    return browser;
  }

}
