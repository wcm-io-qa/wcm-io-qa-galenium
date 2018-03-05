/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.galenium.webdriver;

import java.io.IOException;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;

import com.google.gson.JsonElement;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

abstract class CapabilityProvider {

  /**
   * @return capabilities for browser
   */
  public DesiredCapabilities getDesiredCapabilities() {
    DesiredCapabilities capabilities = getBrowserSpecificCapabilities();
    capabilities = capabilities.merge(getCommonCapabilities());
    GaleniumReportUtil.getLogger().info("Done generating capabilities");
    log(capabilities);
    return capabilities;
  }

  private void log(DesiredCapabilities capabilities) {
    if (getLogger().isTraceEnabled()) {
      getLogger().trace("generated capabilities: " + capabilities);
      Object chromeOptionsCapability = capabilities.getCapability(ChromeOptions.CAPABILITY);
      if (chromeOptionsCapability != null) {
        if (chromeOptionsCapability instanceof ChromeOptions) {
          ChromeOptions chromeOptions = (ChromeOptions)chromeOptionsCapability;
          try {
            JsonElement json = chromeOptions.toJson();
            getLogger().trace("chromeOptions: " + json);
          }
          catch (IOException ex) {
            getLogger().trace("when getting chrome options as JSON.", ex);
          }
        }
      }
    }
  }

  /**
   * Capabilities specific to this browser type. These are automatically included in {@link #getDesiredCapabilities()}.
   * Override when implementing new browser types.
   * @return capabilities for browser
   */
  protected abstract DesiredCapabilities getBrowserSpecificCapabilities();

  protected DesiredCapabilities getCommonCapabilities() {
    // Request browser logging capabilities for capturing console.log output
    LoggingPreferences loggingPrefs = new LoggingPreferences();
    loggingPrefs.enable(LogType.BROWSER, GaleniumConfiguration.getBrowserLogLevel());
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs);
    capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

    return capabilities;
  }

  protected Logger getLogger() {
    return WebDriverManager.getLogger();
  }
}
