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
public class MemoryUsageExtension
    implements
    AfterAllCallback,
    AfterTestExecutionCallback,
    BeforeAllCallback,
    BeforeTestExecutionCallback {

  private static final Logger LOG = LoggerFactory.getLogger(MemoryUsageExtension.class);

  /** {@inheritDoc} */
  @Override
  public void afterAll(ExtensionContext testContext) {
    logMemory();
  }

  /** {@inheritDoc} */
  @Override
  public void afterTestExecution(ExtensionContext tr) {
    logMemory();
  }

  /** {@inheritDoc} */
  @Override
  public void beforeAll(ExtensionContext testContext) {
    logMemory();
  }

  /** {@inheritDoc} */
  @Override
  public void beforeTestExecution(ExtensionContext tr) {
    logMemory();
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
