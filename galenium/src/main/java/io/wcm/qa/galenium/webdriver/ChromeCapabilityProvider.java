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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

class ChromeCapabilityProvider extends CapabilityProvider {

  private static final String OPTIONS_KEY_BINARY = "binary";
  protected static final String OPTIONS_KEY_ARGS = "args";

  private void addChromeOption(ChromeOptions chromeOptions, String key, Object value) {
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
  }

  private DesiredCapabilities augmentCapabilities(DesiredCapabilities capabilities) {
    String chromeBinaryPath = GaleniumConfiguration.getChromeBinaryPath();
    if (StringUtils.isNotBlank(chromeBinaryPath)) {
      getLogger().debug("setting binary path: '" + chromeBinaryPath + "'");
      addChromeOption(capabilities, OPTIONS_KEY_BINARY, chromeBinaryPath);
    }
    return capabilities;
  }

  protected DesiredCapabilities addChromeOption(DesiredCapabilities capabilities, String key, Object value) {
    Object options = capabilities.getCapability(ChromeOptions.CAPABILITY);
    if (options == null) {
      getLogger().debug("setting in fresh chrome options: '" + key + "' -> '" + chromeOptionValueToString(value) + "'");
      options = new ChromeOptions();
      addChromeOption((ChromeOptions)options, key, value);
    }
    else if (options instanceof ChromeOptions) {
      getLogger().debug("setting in existing chrome options: '" + key + "' -> '" + chromeOptionValueToString(value) + "'");
      ChromeOptions chromeOptions = (ChromeOptions)options;
      addChromeOption(chromeOptions, key, value);
    }
    else if (options instanceof Map<?, ?>) {
      getLogger().debug("setting in existing map options: '" + key + "' -> '" + chromeOptionValueToString(value) + "'");
      @SuppressWarnings("unchecked")
      Map<Object, Object> optionMap = (Map<Object, Object>)options;
      optionMap.put(key, value);
    }
    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    return capabilities;
  }

  private String chromeOptionValueToString(Object value) {
    if (value instanceof Object[]) {
      Object[] array = (Object[])value;
      return "[" + StringUtils.join(array, ", ") + "]";
    }
    if (value instanceof Iterable) {
      Iterable iterable = (Iterable)value;
      return "[" + StringUtils.join(iterable, ", ") + "]";
    }
    return value.toString();
  }

  @Override
  protected DesiredCapabilities getBrowserSpecificCapabilities() {
    getLogger().debug("creating capabilities for Chrome");
    DesiredCapabilities capabilities = DesiredCapabilities.chrome();
    return augmentCapabilities(capabilities);
  }

}
