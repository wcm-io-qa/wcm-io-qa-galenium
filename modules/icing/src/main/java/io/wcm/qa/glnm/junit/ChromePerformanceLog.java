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

import java.util.logging.Level;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;

/**
 * <p>ChromePerformanceLog interface.</p>
 *
 * @since 5.0.0
 */
public interface ChromePerformanceLog {

  /**
   * <p>chromeOptionsWithPerformanceLog.</p>
   *
   * @return chrome options with performance logging enabled
   * @since 5.0.0
   */
  static ChromeOptions chromeOptionsWithPerformanceLog() {
    ChromeOptions options = new ChromeOptions();
    LoggingPreferences preferences = new LoggingPreferences();
    preferences.enable(LogType.PERFORMANCE, Level.ALL);

    options.setCapability("goog:loggingPrefs", preferences);

    return options;
  }

}
