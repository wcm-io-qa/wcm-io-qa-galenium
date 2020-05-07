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
package io.wcm.qa.glnm.listeners.junit;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs memory usage.
 *
 * @since 1.0.0
 */
public class DiskUsageExtension
    implements
    AfterAllCallback,
    AfterTestExecutionCallback,
    BeforeAllCallback,
    BeforeTestExecutionCallback {

  private static final Logger LOG = LoggerFactory.getLogger(DiskUsageExtension.class);
  private static final String[] PATHS_TO_LOG_DISK_USAGE_FOR = new String[] {
      ".",
      "/dev/shm"
  };

  /** {@inheritDoc} */
  @Override
  public void afterAll(ExtensionContext testContext) {
    logDiskUsage();
  }


  /** {@inheritDoc} */
  @Override
  public void afterTestExecution(ExtensionContext tr) {
    logDiskUsage();
  }


  /** {@inheritDoc} */
  @Override
  public void beforeAll(ExtensionContext testContext) {
    logDiskUsage();
  }


  /** {@inheritDoc} */
  @Override
  public void beforeTestExecution(ExtensionContext tr) {
    logDiskUsage();
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
