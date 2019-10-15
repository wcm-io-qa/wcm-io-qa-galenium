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

import java.io.File;
import java.io.IOException;

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
public class DiskUsageListener extends TestListenerAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(DiskUsageListener.class);
  private static final String[] PATHS_TO_LOG_DISK_USAGE_FOR = new String[] {
      ".",
      "/dev/shm"
  };

  /** {@inheritDoc} */
  @Override
  public void onFinish(ITestContext testContext) {
    logDiskUsage();
  }


  /** {@inheritDoc} */
  @Override
  public void onStart(ITestContext testContext) {
    logDiskUsage();
    super.onStart(testContext);
  }


  /** {@inheritDoc} */
  @Override
  public void onTestFailure(ITestResult tr) {
    logDiskUsage();
    super.onTestFailure(tr);
  }


  /** {@inheritDoc} */
  @Override
  public void onTestSkipped(ITestResult tr) {
    logDiskUsage();
    super.onTestSkipped(tr);
  }


  /** {@inheritDoc} */
  @Override
  public void onTestStart(ITestResult result) {
    logDiskUsage();
  }


  /** {@inheritDoc} */
  @Override
  public void onTestSuccess(ITestResult tr) {
    logDiskUsage();
    super.onTestSuccess(tr);
  }


  private static String getDiskUsageMessage(String path) {
    File file = new File(path);
    try {
      StringBuilder diskUsageMessage = new StringBuilder()
          .append("'")
          .append(file.getCanonicalPath())
          .append("' free space: ")
          .append(file.getFreeSpace())
          .append("\ntotal space: ")
          .append(file.getTotalSpace())
          .append("\nusable space: ")
          .append(file.getUsableSpace());
      return diskUsageMessage.toString();
    }
    catch (IOException ex) {
      return "no space info for '" + path + "': \"" + ex.getMessage() + "\"";
    }
  }


  private static String getDiskUsageMessages() {
    StringBuilder messages = new StringBuilder();
    for (String path : PATHS_TO_LOG_DISK_USAGE_FOR) {
      messages
          .append(getDiskUsageMessage(path))
          .append("\n");
    }
    String tempDirPath = System.getProperty("java.io.tmpdir");
    messages
        .append(getDiskUsageMessage(tempDirPath))
        .append("\n");
    return messages.toString();
  }

  private static void logDiskUsage() {
    LOG.trace(getDiskUsageMessages());
  }


}
