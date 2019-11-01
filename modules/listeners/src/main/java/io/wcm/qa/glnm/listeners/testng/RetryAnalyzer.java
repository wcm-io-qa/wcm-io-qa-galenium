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
package io.wcm.qa.glnm.listeners.testng;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

import io.qameta.allure.Allure;
import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.webdriver.WebDriverManagement;

/**
 * Simple retry analyzer to deal with flaky tests in TestNG.
 *
 * @since 1.0.0
 */
public class RetryAnalyzer implements IRetryAnalyzer {

  private static final Logger LOG = LoggerFactory.getLogger(RetryAnalyzer.class);

  private static final int MAX_RETRY_COUNT = GaleniumConfiguration.getNumberOfRetries();
  private Map<Object, AtomicInteger> counts = new Hashtable<Object, AtomicInteger>();

  /** {@inheritDoc} */
  @Override
  public boolean retry(ITestResult result) {
    if (getCount(result).incrementAndGet() > MAX_RETRY_COUNT) {
      // don't retry if max count is exceeded
      String failureMessage = "Failed last retry (" + getCount(result).get() + "): " + result.getTestName();
      logResult(result, failureMessage);
      Allure.label("RERUN_MAX", "true");
      return false;
    }

    String infoMessage = "Rerunning test (" + getCount(result).get() + "): " + result.getTestName();
    logResult(result, infoMessage);
    Allure.label("RERUN", "" + getCount(result).get());
    WebDriverManagement.closeDriver();
    return true;
  }

  private AtomicInteger getCount(ITestResult result) {
    Object key = result.getInstance();

    if (!counts.containsKey(key)) {
      counts.put(key, new AtomicInteger());
    }

    return counts.get(key);
  }

  private void logResult(ITestResult result, String message) {
    Reporter.log(message, true);
    LOG.info(message);
    Throwable throwable = result.getThrowable();
    if (throwable != null) {
      String throwableMessage = throwable.getMessage();
      LOG.info(result.getTestName() + ": " + throwableMessage);
      LOG.debug(result.getTestName(), throwable);
    }
  }

}
