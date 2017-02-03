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
package io.wcm.qa.galenium.listeners;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

/**
 * Simple retry analyzer to deal with flaky tests in TestNG.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

  private static final int MAX_RETRY_COUNT = GaleniumConfiguration.getNumberOfRetries();
  private Map<Object, AtomicInteger> counts = new Hashtable<Object, AtomicInteger>();

  @Override
  public boolean retry(ITestResult result) {
    if (getCount(result).incrementAndGet() > MAX_RETRY_COUNT) {
      // don't retry if max count is exceeded
      String failureMessage = "Exceeded max count (" + getCount(result).get() + "): " + result.getTestName();
      Reporter.log(failureMessage, true);
      getLogger().info(GaleniumReportUtil.MARKER_FAIL, failureMessage);
      return false;
    }

    String infoMessage = "Rerunning test (" + getCount(result).get() + "): " + result.getTestName();
    Reporter.log(infoMessage, true);
    getLogger().info(infoMessage);

    return true;
  }

  private AtomicInteger getCount(ITestResult result) {
    Object key = result.getInstance();

    if (!counts.containsKey(key)) {
      counts.put(key, new AtomicInteger());
    }

    return counts.get(key);
  }

}
