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
package io.wcm.qa.glnm.listeners.testng;

import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;

import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.xml.XmlSuite;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * This listener is also responsible for closing the WebDriver instances. If
 * tests are run in parallel threads, it will close the WebDriver after each
 * test method. If they are run sequentially in the main thread, the same
 * WebDriver will be re-used and close after the last test case.
 *
 * @since 1.0.0
 */
public class LoggingListener extends TestListenerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingListener.class);

  /** {@inheritDoc} */
  @Override
  public void onFinish(ITestContext context) {
    LOG.trace("Generating Galen reports.");
    GaleniumReportUtil.createGalenReports();
  }

  /** {@inheritDoc} */
  @Override
  public void onStart(ITestContext context) {
    LOG.debug("Starting tests");
    if (LOG.isDebugEnabled()) {
      LOG.trace("host: " + context.getHost());
      LOG.trace("suite name: " + context.getSuite().getName());
      LOG.trace("parallel: " + context.getSuite().getParallel());
      LOG.trace("included groups: " + StringUtils.join(context.getIncludedGroups(), ", "));
      LOG.trace("excluded groups: " + StringUtils.join(context.getExcludedGroups(), ", "));
    }
    if (LOG.isTraceEnabled()) {
      ISuite suite = context.getSuite();
      XmlSuite xmlSuite = suite.getXmlSuite();
      LOG.trace("thread count: " + xmlSuite.getThreadCount());
      LOG.trace("data provider thread count: " + xmlSuite.getDataProviderThreadCount());
      LOG.trace("methods: " + StringUtils.join(suite.getAllMethods(), ", "));
      LOG.trace("excluded methods: " + StringUtils.join(suite.getExcludedMethods(), ", "));
      Set<Entry<String, String>> allParameters = xmlSuite.getAllParameters().entrySet();
      for (Entry<String, String> entry : allParameters) {
        LOG.trace(entry.getKey() + "='" + entry.getValue() + "'");
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onTestSkipped(ITestResult result) {
    StringBuilder skipMessage = new StringBuilder();
    skipMessage.append("Skipped test: ");
    skipMessage.append(getTestNameForInternalLogging(result));
    if (result.getThrowable() != null) {
      LOG.info(skipMessage.toString(), result.getThrowable());
    }
    else {
      LOG.info(skipMessage.toString());
    }
    if (GaleniumConfiguration.isTakeScreenshotOnSkippedTest()) {
      takeScreenshot(result);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onTestStart(ITestResult result) {
    LOG.debug(getTestNameForInternalLogging(result) + ": Start in thread " + Thread.currentThread().getName());
  }

  private String getTestNameForInternalLogging(ITestResult result) {
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
    WebDriver driver = GaleniumContext.getDriver();
    if (driver != null) {
      GaleniumReportUtil.takeScreenshot(result, driver);
    }
  }

}
