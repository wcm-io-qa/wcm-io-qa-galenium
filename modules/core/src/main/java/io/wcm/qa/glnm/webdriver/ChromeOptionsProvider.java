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
package io.wcm.qa.glnm.webdriver;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

class ChromeOptionsProvider extends OptionsProvider<ChromeOptions> {

  private static final String OPTIONS_KEY_BINARY = "binary";
  protected static final String OPTIONS_KEY_ARGS = "args";

  private ChromeOptions augmentOptions(ChromeOptions options) {
    String chromeBinaryPath = GaleniumConfiguration.getChromeBinaryPath();
    if (StringUtils.isNotBlank(chromeBinaryPath)) {
      getLogger().debug("setting binary path: '" + chromeBinaryPath + "'");
      addChromeOption(options, OPTIONS_KEY_BINARY, chromeBinaryPath);
    }
    return options;
  }

  protected ChromeOptions addChromeOption(ChromeOptions chromeOptions, String key, Object value) {
    switch (key) {
      case OPTIONS_KEY_BINARY:
        chromeOptions.setBinary(value.toString());
        break;
      case OPTIONS_KEY_ARGS:
        String[] arguments = (String[])value;
        chromeOptions.addArguments(arguments);
        break;

      default:
        getLogger().debug(GaleniumReportUtil.MARKER_ERROR, "cannot map option key: '" + key + "'");
        break;
    }
    return chromeOptions;
  }

  @Override
  protected ChromeOptions getBrowserSpecificOptions() {
    getLogger().debug("creating capabilities for Chrome");
    ChromeOptions options = newOptions();
    return augmentOptions(options);
  }

  @Override
  protected void log(ChromeOptions options) {
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

  @Override
  protected ChromeOptions newOptions() {
    return new ChromeOptions();
  }

}
