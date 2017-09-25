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

import java.util.ArrayList;
import java.util.List;

import org.testng.IConfigurationListener2;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import io.wcm.qa.galenium.sampling.text.TextSamplePersistenceListener;

/**
 * Listener to manage WebDriver management, reporting and screenshots. This listener is just a container for other
 * listeners that will be called throughout test and suite lifecycle.
 * Contained by default:
 * <ul>
 * <li>
 * {@link LoggingListener}
 * </li>
 * <li>
 * {@link ExtentReportsListener}
 * </li>
 * <li>
 * {@link WebDriverListener}
 * </li>
 * <li>
 * {@link TextSamplePersistenceListener}
 * </li>
 * </ul>
 * You can extend this class and add your own listeners using the {@link #add(ITestListener)} method.
 */
public class DefaultGaleniumListener extends TestListenerAdapter {

  private List<ITestListener> listeners = new ArrayList<ITestListener>();

  /**
   * Constructor.
   */
  public DefaultGaleniumListener() {
    add(new LoggingListener());
    add(new ExtentReportsListener());
    add(new WebDriverListener());
    add(new TextSamplePersistenceListener());
  }

  /**
   * Adds an additional test listener.
   * @param listener to add
   * @return true
   */
  public boolean add(ITestListener listener) {
    return listeners.add(listener);
  }

  @Override
  public void beforeConfiguration(ITestResult tr) {
    getLogger().trace("+++LISTENER: beforeConfiguration(ITestResult tr)");
    for (ITestListener listener : listeners) {
      if (listener instanceof IConfigurationListener2) {
        getLogger().trace("{}: beforeConfiguration(ITestResult tr)", listener.getClass());
        ((IConfigurationListener2)listener).beforeConfiguration(tr);
      }
    }
    super.beforeConfiguration(tr);
  }

  @Override
  public void onFinish(ITestContext context) {
    getLogger().trace("+++LISTENER: onFinish(ITestContext context)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onFinish(ITestContext context)", listener.getClass());
      listener.onFinish(context);
    }
    super.onFinish(context);
  }

  @Override
  public void onStart(ITestContext context) {
    getLogger().trace("+++LISTENER: onStart(ITestContext context)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onStart(ITestContext context)", listener.getClass());
      listener.onStart(context);
    }
    super.onStart(context);
  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestFailedButWithinSuccessPercentage(ITestResult result)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onTestFailedButWithinSuccessPercentage(ITestResult result)", listener.getClass());
      listener.onTestFailedButWithinSuccessPercentage(result);
    }
    super.onTestFailedButWithinSuccessPercentage(result);
  }

  @Override
  public void onTestFailure(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestFailure(ITestResult result)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onTestFailure(ITestResult result)", listener.getClass());
      listener.onTestFailure(result);
    }
    super.onTestFailure(result);
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    getLogger().trace("LISTENER: onTestSkipped(ITestResult result)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onTestSkipped(ITestResult result)", listener.getClass());
      listener.onTestSkipped(result);
    }
    super.onTestSkipped(result);
  }

  @Override
  public void onTestStart(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestStart(ITestResult result)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onTestStart(ITestResult result)", listener.getClass());
      listener.onTestStart(result);
    }
    super.onTestStart(result);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestSuccess(ITestResult result)");
    for (ITestListener listener : listeners) {
      getLogger().trace("{}: onTestSuccess(ITestResult result)", listener.getClass());
      listener.onTestSuccess(result);
    }
    super.onTestSuccess(result);
  }

}
