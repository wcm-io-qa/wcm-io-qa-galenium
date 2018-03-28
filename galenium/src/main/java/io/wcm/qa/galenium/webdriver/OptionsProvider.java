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

import static io.wcm.qa.galenium.util.GaleniumConfiguration.isWebDriverAcceptTrustedSslCertificatesOnly;
import static io.wcm.qa.galenium.util.GaleniumConfiguration.isWebDriverRefuseSslCertificates;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.slf4j.Logger;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

abstract class OptionsProvider<O extends MutableCapabilities> {

  /**
   * @return capabilities for browser
   */
  public O getOptions() {
    O options = getBrowserSpecificOptions();
    options = mergeOptions(options);
    GaleniumReportUtil.getLogger().info("Done generating capabilities");
    log(options);
    return options;
  }

  private void log(O options) {
    if (getLogger().isTraceEnabled()) {
      getLogger().trace("generated capabilities: " + options);
      Object chromeOptionsCapability = options.getCapability(ChromeOptions.CAPABILITY);
      if (chromeOptionsCapability != null) {
        if (chromeOptionsCapability instanceof ChromeOptions) {
          ChromeOptions chromeOptions = (ChromeOptions)chromeOptionsCapability;
            Map<String, Object> json = chromeOptions.toJson();
          Set<Entry<String, Object>> entrySet = json.entrySet();
          StringBuilder sb = new StringBuilder();
          sb.append("chromeOptions:\n");
          for (Entry<String, Object> entry : entrySet) {
            sb.append("'");
            sb.append(entry.getKey());
            sb.append("': '");
            sb.append(entry.getValue());
            sb.append("'");
          }
          getLogger().trace(sb.toString());
        }
      }
    }
  }

  /**
   * Capabilities specific to this browser type. These are automatically included in {@link #getOptions()}.
   * Override when implementing new browser types.
   * @return capabilities for browser
   */
  protected abstract O getBrowserSpecificOptions();

  protected O getCommonOptions() {
    // Request browser logging capabilities for capturing console.log output
    LoggingPreferences loggingPrefs = new LoggingPreferences();
    loggingPrefs.enable(LogType.BROWSER, GaleniumConfiguration.getBrowserLogLevel());
    O options = newOptions();
    options.setCapability(CapabilityType.LOGGING_PREFS, loggingPrefs);
    options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, !isWebDriverRefuseSslCertificates());
    options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, !isWebDriverAcceptTrustedSslCertificatesOnly());

    return options;
  }

  protected Logger getLogger() {
    return WebDriverManager.getLogger();
  }

  @SuppressWarnings("unchecked")
  protected O mergeOptions(O options) {
    return (O)options.merge(getCommonOptions());
  }

  protected abstract O newOptions();
}
