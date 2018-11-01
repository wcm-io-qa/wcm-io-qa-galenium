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
package io.wcm.qa.galenium.listeners;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Logs memory usage.
 */
public class MemoryUsageListener extends TestListenerAdapter {

  private static final Marker MEMORY_MARKER = GaleniumReportUtil.getMarker("memory-listener");

  @Override
  public void onTestStart(ITestResult result) {
    logMemory();
  }

  private String getMemoryMessage() {
    Runtime runtime = Runtime.getRuntime();
    StringBuilder memoryMessage = new StringBuilder()
        .append("free memory: ")
        .append(runtime.freeMemory())
        .append("<br/>total memory: ")
        .append(runtime.totalMemory())
        .append("<br/>max memory: ")
        .append(runtime.maxMemory());
    return memoryMessage.toString();
  }

  private void logMemory() {
    getLogger().trace(getMemoryMessage());
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MEMORY_MARKER);
  }

}
