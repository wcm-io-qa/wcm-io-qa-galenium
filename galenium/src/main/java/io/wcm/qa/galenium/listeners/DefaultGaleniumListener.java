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

import java.util.ArrayList;
import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Listener to manage WebDriver management, reporting and screenshots.
 */
public class DefaultGaleniumListener extends TestListenerAdapter {

  private List<ITestListener> listeners = new ArrayList<ITestListener>();

  public DefaultGaleniumListener() {
    add(new LoggingListener());
    add(new ExtentReportsListener());
    add(new WebDriverListener());
  }

  public boolean add(ITestListener e) {
    return listeners.add(e);
  }

  @Override
  public void onFinish(ITestContext context) {
    for (ITestListener listener : listeners) {
      listener.onFinish(context);
    }
  }

  @Override
  public void onStart(ITestContext context) {
    for (ITestListener listener : listeners) {
      listener.onStart(context);
    }
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    for (ITestListener listener : listeners) {
      listener.onTestFailedButWithinSuccessPercentage(result);
    }
  }

  @Override
  public void onTestFailure(ITestResult result) {
    for (ITestListener listener : listeners) {
      listener.onTestFailure(result);
    }
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    for (ITestListener listener : listeners) {
      listener.onTestSkipped(result);
    }
  }

  @Override
  public void onTestStart(ITestResult result) {
    for (ITestListener listener : listeners) {
      listener.onTestStart(result);
    }
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    for (ITestListener listener : listeners) {
      listener.onTestSuccess(result);
    }
  }


}
