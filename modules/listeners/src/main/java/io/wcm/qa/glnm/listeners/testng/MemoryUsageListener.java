/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Logs memory usage.
 *
 * @since 1.0.0
 */
public class MemoryUsageListener extends TestListenerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(MemoryUsageListener.class);

  /** {@inheritDoc} */
  @Override
  public void onFinish(ITestContext testContext) {
    logMemory();
  }


  /** {@inheritDoc} */
  @Override
  public void onStart(ITestContext testContext) {
    logMemory();
    super.onStart(testContext);
  }


  /** {@inheritDoc} */
  @Override
  public void onTestFailure(ITestResult tr) {
    logMemory();
    super.onTestFailure(tr);
  }


  /** {@inheritDoc} */
  @Override
  public void onTestSkipped(ITestResult tr) {
    logMemory();
    super.onTestSkipped(tr);
  }


  /** {@inheritDoc} */
  @Override
  public void onTestStart(ITestResult result) {
    logMemory();
  }


  /** {@inheritDoc} */
  @Override
  public void onTestSuccess(ITestResult tr) {
    logMemory();
    super.onTestSuccess(tr);
  }


  private String getMemoryMessage() {
    Runtime runtime = Runtime.getRuntime();
    StringBuilder memoryMessage = new StringBuilder()
        .append("free memory: ")
        .append(runtime.freeMemory())
        .append("\ntotal memory: ")
        .append(runtime.totalMemory())
        .append("\nmax memory: ")
        .append(runtime.maxMemory());
    return memoryMessage.toString();
  }

  private void logMemory() {
    LOG.trace(getMemoryMessage());
  }

}
