/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.junit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.seljup.Arguments;
import io.github.bonigarcia.seljup.SeleniumExtension;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * <p>UsesChrome interface.</p>
 *
 * @since 5.0.0
 */
@ExtendWith(SeleniumExtension.class)
public interface UsesChrome {

  /**
   * Initializes headless driver.
   *
   * @param driver is injected by Selenium Jupiter extension
   * @since 5.0.0
   */
  @BeforeEach
  default void initBrowser(@Arguments("--headless") ChromeDriver driver) {
    GaleniumContext.getContext().setDriver(driver);
  }

  /**
   * Frees driver to be cleaned up.
   *
   * @since 5.0.0
   */
  @AfterEach
  default void dropBrowser() {
    finalScreenshot();
    GaleniumContext.getContext().setDriver(null);
  }

  /**
   * Override to change screenshot after test.
   */
  default void finalScreenshot() {
    GaleniumReportUtil.takeScreenshot();
  }

}
