/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 - 2016 wcm.io
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

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.format.NameUtil;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * This listener is also responsible for closing the WebDriver instances. If
 * tests are run in parallel threads, it will close the WebDriver after each
 * test method. If they are run sequentially in the main thread, the same
 * WebDriver will be re-used and close after the last test case.
 *
 * @since 1.0.0
 */
public class LoggingExtension
    implements
    AfterAllCallback,
    AfterTestExecutionCallback,
    BeforeAllCallback,
    BeforeTestExecutionCallback {

  private static final Logger LOG = LoggerFactory.getLogger(LoggingExtension.class);

  /** {@inheritDoc} */
  @Override
  public void afterAll(ExtensionContext context) {
    LOG.trace("Generating Galen reports.");
    GaleniumReportUtil.createGalenReports();
  }

  /** {@inheritDoc} */
  @Override
  public void afterTestExecution(ExtensionContext context) {
    screenshot();
  }

  /** {@inheritDoc} */
  @Override
  public void beforeAll(ExtensionContext context) throws Exception {
    LOG.debug("Starting tests");
    if (LOG.isTraceEnabled()) {
      LOG.trace("included tags: " + StringUtils.join(context.getTags(), ", "));
    }
  }

  /** {@inheritDoc} */
  @Override
  public void beforeTestExecution(ExtensionContext context) {
    String displayName = context.getDisplayName();
    String sanitized = NameUtil.getSanitized(displayName, 120);
    LOG.debug(sanitized + ": Start in thread " + Thread.currentThread().getName());
  }

  private static void screenshot() {
    try {
      GaleniumReportUtil.takeScreenshot();
    }
    catch (GaleniumException ex) {
      LOG.info("Cannot take Screenshot: " + ex.getMessage());
    }
  }

}
