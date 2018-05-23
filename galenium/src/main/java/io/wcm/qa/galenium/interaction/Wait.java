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
package io.wcm.qa.galenium.interaction;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class Wait {

  private Wait() {
    // do not instantiate
  }

  /**
   * Load URL and wait passed number of seconds for it to be loaded.
   * @param url to load
   * @param timeOutInSeconds how long to wait for URL to be current
   */
  public static void forUrl(String url, int timeOutInSeconds) {
    getLogger().trace("waiting for URL: '" + url + "'");
    WebDriverWait wait = new WebDriverWait(getDriver(), timeOutInSeconds);
    wait.until(ExpectedConditions.urlToBe(url));
    getLogger().trace("found URL: '" + url + "'");
  }

  /**
   * Load URL and wait for it to be loaded.
   * @param url to load
   */
  public static void forUrl(String url) {
    Wait.forUrl(url, 5);
  }


}
