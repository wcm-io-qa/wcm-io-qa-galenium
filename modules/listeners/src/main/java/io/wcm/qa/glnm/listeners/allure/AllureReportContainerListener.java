/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.listeners.allure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import io.wcm.qa.glnm.logging.util.GaleniumLoggingUtil;

/**
 * <p>
 * AllureReportContainerListener class.
 * </p>
 *
 * @since 4.0.0
 */
public class AllureReportContainerListener implements TestLifecycleListener {

  private static final Logger LOG = LoggerFactory.getLogger(AllureReportContainerListener.class);

  /** {@inheritDoc} */
  @Override
  public void afterTestStart(TestResult result) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("start logging");
    }
    GaleniumLoggingUtil.startTestLogging();
  }

  /** {@inheritDoc} */
  @Override
  public void beforeTestStop(TestResult result) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("stop logging");
    }
    GaleniumLoggingUtil.stopTestLogging();
  }

}
