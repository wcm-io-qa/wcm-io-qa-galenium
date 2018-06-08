/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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
package io.wcm.qa.galenium.listeners;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getTestDevice;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.LogStatus;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;
import io.wcm.qa.galenium.webdriver.WebDriverManager;

/**
 * This listener is also responsible for closing the WebDriver instances. If
 * tests are run in parallel threads, it will close the WebDriver after each
 * test method. If they are run sequentially in the main thread, the same
 * WebDriver will be re-used and close after the last test case.
 */
public class LoggingListener extends TestListenerAdapter {

  @Override
  public void onFinish(ITestContext context) {
    getLogger().trace("Generating Galen reports.");
    GaleniumReportUtil.createGalenReports();
  }

  @Override
  public void onStart(ITestContext context) {
    Logger logger = getLogger();
    logger.debug("Starting tests");
    if (logger.isDebugEnabled()) {
      logger.trace("host: " + context.getHost());
      logger.trace("suite name: " + context.getSuite().getName());
      logger.trace("parallel: " + context.getSuite().getParallel());
      logger.trace("included groups: " + StringUtils.join(context.getIncludedGroups(), ", "));
      logger.trace("excluded groups: " + StringUtils.join(context.getExcludedGroups(), ", "));
    }
    if (logger.isTraceEnabled()) {
      ISuite suite = context.getSuite();
      XmlSuite xmlSuite = suite.getXmlSuite();
      logger.trace("thread count: " + xmlSuite.getThreadCount());
      logger.trace("data provider thread count: " + xmlSuite.getDataProviderThreadCount());
      logger.trace("methods: " + StringUtils.join(suite.getAllMethods(), ", "));
      logger.trace("excluded methods: " + StringUtils.join(suite.getExcludedMethods(), ", "));
      Set<Entry<String, String>> allParameters = xmlSuite.getAllParameters().entrySet();
      for (Entry<String, String> entry : allParameters) {
        logger.trace(entry.getKey() + "='" + entry.getValue() + "'");
      }
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    getLogger().info("Skipped test: " + getTestName(result));
    if (GaleniumConfiguration.isTakeScreenshotOnSkippedTest()) {
      takeScreenshot(result);
    }
    GaleniumReportUtil.getLogger().info(GaleniumReportUtil.MARKER_SKIP, "SKIPPED");
  }

  @Override
  public void onTestStart(ITestResult result) {
    getLogger().debug(getTestName(result) + ": Start in thread " + Thread.currentThread().getName());
    GaleniumReportUtil.getExtentTest(result).log(LogStatus.INFO, "Start in thread " + Thread.currentThread().getName());
  }

  private String getTestName(ITestResult result) {
    return result.getTestClass().getRealClass().getSimpleName() + "." + result.getName() + "(" + getAdditionalInfo() + ") ";
  }

  protected String getAdditionalInfo() {
    String additionalInfo = "no additional info";
    TestDevice testDevice = getTestDevice();
    if (testDevice != null) {
      additionalInfo = " [" + testDevice.toString() + "] ";
    }
    return additionalInfo;
  }

  protected void takeScreenshot(ITestResult result) {
    WebDriver driver = WebDriverManager.getCurrentDriver();
    if (driver != null) {
      GaleniumReportUtil.takeScreenshot(result, driver);
    }
  }

}
