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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.testng.IAnnotationTransformer;
import org.testng.IConfigurationListener2;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.ITestAnnotation;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Listener to manage WebDriver management, reporting and screenshots. This listener is just a container for other
 * listeners that will be called throughout test and suite lifecycle.
 * Contained by default:
 * <ul>
 * <li>
 * {@link io.wcm.qa.galenium.listeners.LoggingListener}
 * </li>
 * <li>
 * {@link io.wcm.qa.galenium.listeners.ExtentReportsListener}
 * </li>
 * <li>
 * {@link io.wcm.qa.galenium.listeners.WebDriverListener}
 * </li>
 * <li>
 * {@link io.wcm.qa.galenium.listeners.TextSamplePersistenceListener}
 * </li>
 * <li>
 * {@link io.wcm.qa.galenium.listeners.RetryAnalyzer} if galenium.retryMax is greater than zero
 * </li>
 * </ul>
 * You can extend this class and add your own listeners using the {@link #add(ITestNGListener)} method.
 *
 * @since 1.0.0
 */
public class DefaultGaleniumListener extends TestListenerAdapter implements IAnnotationTransformer {

  private static final Marker MARKER_LISTENERS = GaleniumReportUtil.getMarker("galenium.listeners");
  private List<ITestNGListener> listeners = new ArrayList<ITestNGListener>();

  /**
   * Constructor.
   */
  public DefaultGaleniumListener() {
    add(new LoggingListener());
    add(new ExtentReportsListener());
    add(new WebDriverListener());
    add(new TextSamplePersistenceListener());
    if (GaleniumConfiguration.getNumberOfRetries() > 0) {
      add(new RetryAnalyzerAnnotationTransformer());
    }
  }

  /**
   * Adds an additional test listener.
   *
   * @param listener to add
   * @return true
   */
  public boolean add(ITestNGListener listener) {
    return listeners.add(listener);
  }

  /**
   * Adds an additional test listener.
   *
   * @param index index at which the specified element is to be inserted
   * @param listener to add
   */
  public void add(int index, ITestNGListener listener) {
    listeners.add(index, listener);
  }

  /** {@inheritDoc} */
  @Override
  public void beforeConfiguration(ITestResult tr) {
    getLogger().trace("+++LISTENER: beforeConfiguration(ITestResult tr)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof IConfigurationListener2) {
        getLogger().trace("{}: beforeConfiguration(ITestResult tr)", listener.getClass());
        ((IConfigurationListener2)listener).beforeConfiguration(tr);
      }
    }
    super.beforeConfiguration(tr);
  }

  /** {@inheritDoc} */
  @Override
  public void onFinish(ITestContext context) {
    getLogger().trace("+++LISTENER: onFinish(ITestContext context)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onFinish(ITestContext context)", listener.getClass());
        ((ITestListener)listener).onFinish(context);
      }
    }
    super.onFinish(context);
  }

  /** {@inheritDoc} */
  @Override
  public void onStart(ITestContext context) {
    getLogger().trace("+++LISTENER: onStart(ITestContext context)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onStart(ITestContext context)", listener.getClass());
        ((ITestListener)listener).onStart(context);
      }
    }
    super.onStart(context);
  }

  /** {@inheritDoc} */
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestFailedButWithinSuccessPercentage(ITestResult result)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onTestFailedButWithinSuccessPercentage(ITestResult result)", listener.getClass());
        ((ITestListener)listener).onTestFailedButWithinSuccessPercentage(result);
      }
    }
    super.onTestFailedButWithinSuccessPercentage(result);
  }

  /** {@inheritDoc} */
  @Override
  public void onTestFailure(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestFailure(ITestResult result)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onTestFailure(ITestResult result)", listener.getClass());
        ((ITestListener)listener).onTestFailure(result);
      }
    }
    super.onTestFailure(result);
  }

  /** {@inheritDoc} */
  @Override
  public void onTestSkipped(ITestResult result) {
    getLogger().trace("LISTENER: onTestSkipped(ITestResult result)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onTestSkipped(ITestResult result)", listener.getClass());
        ((ITestListener)listener).onTestSkipped(result);
      }
    }
    super.onTestSkipped(result);
  }

  /** {@inheritDoc} */
  @Override
  public void onTestStart(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestStart(ITestResult result)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onTestStart(ITestResult result)", listener.getClass());
        ((ITestListener)listener).onTestStart(result);
      }
    }
    super.onTestStart(result);
  }

  /** {@inheritDoc} */
  @Override
  public void onTestSuccess(ITestResult result) {
    getLogger().trace("+++LISTENER: onTestSuccess(ITestResult result)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof ITestListener) {
        getLogger().trace("{}: onTestSuccess(ITestResult result)", listener.getClass());
        ((ITestListener)listener).onTestSuccess(result);
      }
    }
    super.onTestSuccess(result);
  }

  /** {@inheritDoc} */
  @Override
  public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    getLogger().trace("+++LISTENER: transform("
        + "ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod)");
    for (ITestNGListener listener : listeners) {
      if (listener instanceof IAnnotationTransformer) {
        getLogger().trace("{}: transform("
            + "ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod)", listener.getClass());
        ((IAnnotationTransformer)listener).transform(annotation, testClass, testConstructor, testMethod);
      }
    }

  }

  private Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER_LISTENERS);
  }

}
