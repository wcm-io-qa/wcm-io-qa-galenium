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

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentTest;

import io.wcm.qa.galenium.testcase.AbstractGaleniumBase;

/**
 * Utility class to assist with extracting information about test parameters to be used in reporting.
 */
public final class TestInfoUtil {

  private static final String EXTENT_CATEGORY_PREFIX_TEST_NG = System.getProperty("galenium.extent.category.testNG", "testNG-");
  private static final String EXTENT_CATEGORY_PREFIX_BROWSER = System.getProperty("galenium.extent.category.browser", "BROWSER-");
  private static final String EXTENT_CATEGORY_PREFIX_BREAKPOINTS = System.getProperty("galenium.extent.category.breakpoints", "");

  private TestInfoUtil() {
    // do not instantiate
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

  static TestDevice getTestDevice(ITestResult result) {
    Object testClass = result.getInstance();
    if (testClass instanceof AbstractGaleniumBase) {
      Object device = ((AbstractGaleniumBase)testClass).getDevice();
      if (device instanceof TestDevice) {
        return (TestDevice)device;
      }
    }
    return null;
  }

  static String getAlphanumericTestName(ITestResult result) {
    String name = result.getName();
    return name.replaceAll("[^-A-Za-z0-9]", "_");
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
    if (breakPoints != null) {
      for (String breakPoint : breakPoints) {
        test.assignCategory(EXTENT_CATEGORY_PREFIX_BREAKPOINTS + breakPoint);
      }
    }
  }

}
