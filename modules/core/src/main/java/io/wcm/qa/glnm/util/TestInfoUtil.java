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
package io.wcm.qa.glnm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import com.galenframework.reports.GalenTestInfo;
import com.galenframework.reports.TestReport;
import com.galenframework.reports.TestStatistic;
import com.galenframework.reports.nodes.ReportExtra;
import com.galenframework.reports.nodes.TestReportNode;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.webdriver.HasDevice;

/**
 * Utility class to assist with extracting information about test parameters to be used in reporting.
 */
public final class TestInfoUtil {

  private static final String BROWSER_UNKNOWN = "UNKNOWN";
  private static final Logger LOG = LoggerFactory.getLogger(TestInfoUtil.class);

  private TestInfoUtil() {
    // do not instantiate
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

  /**
   * Gets test device if test case in this result implements {@link HasDevice}.
   * @param result test result to retrieve test case from
   * @return device if one was found
   */
  public static TestDevice getTestDevice(ITestResult result) {
    Object testClass = result.getInstance();
    if (testClass instanceof HasDevice) {
      return ((HasDevice)testClass).getDevice();
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
      LOG.trace("report was null: " + testInfo);
      return false;
    }
    TestStatistic statistics = report.fetchStatistic();
    if (statistics == null) {
      LOG.trace("statistics were null: " + testInfo + "->" + report);
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
      LOG.info("failed: " + testInfo.getName());
    }
    else if (hasWarnings(testInfo)) {
      LOG.info("warnings: " + testInfo.getName());
    }
    else {
      LOG.info("passed: " + testInfo.getName());
    }
    if (LOG.isDebugEnabled()) {
      List<TestReportNode> nodes = testInfo.getReport().getNodes();
      int nodeCounter = 0;
      for (TestReportNode testReportNode : nodes) {
        logTestReportNode(testReportNode, "node" + nodeCounter++);
      }
    }
  }


  private static void logTestReportNode(TestReportNode node, String prefix) {
    String marker;
    switch (node.getStatus()) {
      case WARN:
        marker = "WARN";
        break;
      case ERROR:
        marker = "FAIL";
        break;
      case INFO:
      default:
        marker = "INFO";
        break;
    }
    String type = node.getType();
    if (StringUtils.equals("layout", type)) {
      LOG.info("[" + marker + "]" + prefix + ".name: " + node.getName());
    }
    else {
      LOG.debug("[" + marker + "]" + prefix + ".name: " + node.getName());
    }
    LOG.trace("[" + marker + "]" + prefix + ".type: " + type);
    List<String> attachments = node.getAttachments();
    if (attachments != null) {
      for (String attachment : attachments) {
        LOG.debug("[" + marker + "]" + prefix + ".attachment: " + attachment);
      }
    }
    else {
      LOG.trace("[" + marker + "]" + prefix + ".attachments: none");
    }
    Map<String, ReportExtra> reportExtras = node.getExtras();
    if (reportExtras != null) {
      Set<Entry<String, ReportExtra>> extras = reportExtras.entrySet();
      for (Entry<String, ReportExtra> entry : extras) {
        LOG.debug("[" + marker + "]" + prefix + ".extra: " + entry.getKey() + "=" + entry.getValue());
      }
    }
    else {
      LOG.trace("[" + marker + "]" + prefix + ".extras: none");
    }
    List<TestReportNode> nodes = node.getNodes();
    if (nodes != null) {
      int childCounter = 0;
      for (TestReportNode childNode : nodes) {
        logTestReportNode(childNode, prefix + "." + childCounter++);
      }
    }
    else {
      LOG.trace("[" + marker + "]" + prefix + ".children: none");
    }
  }

  static List<String> getBreakPoint(ITestResult result) {
    TestDevice testDevice = getTestDevice(result);
    if (testDevice != null) {
      ArrayList<String> mediaQueries = new ArrayList<String>();
      CollectionUtils.addAll(mediaQueries, testDevice.getTags());
      mediaQueries.remove(testDevice.getBrowserType().name());
      return mediaQueries;
    }
    return Collections.emptyList();
  }

  static String getBrowser(ITestResult result) {
    TestDevice testDevice = getTestDevice(result);
    if (testDevice != null) {
      return testDevice.getBrowserType().getBrowser();
    }

    String name = result.getName();
    if (StringUtils.contains(name, "using ")) {
      String browser = name.replaceFirst(".*using ", "");
      browser = browser.replaceFirst(" \\(.*", "");
      return browser;
    }
    return BROWSER_UNKNOWN;
  }

}
