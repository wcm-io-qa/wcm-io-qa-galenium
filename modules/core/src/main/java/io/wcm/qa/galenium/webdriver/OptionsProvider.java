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

import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.isWebDriverAcceptTrustedSslCertificatesOnly;
import static io.wcm.qa.galenium.configuration.GaleniumConfiguration.isWebDriverRefuseSslCertificates;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.slf4j.Logger;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.proxy.BrowserProxyUtil;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

abstract class OptionsProvider<O extends MutableCapabilities> {

  /**
   * @return capabilities for browser
   */
  public O getOptions() {
    O options = getBrowserSpecificOptions();
    options = mergeCommonOptions(options);
    GaleniumReportUtil.getLogger().info("Done generating capabilities");
    log(options);
    return options;
  }

  /**
   * Capabilities specific to this browser type. These are automatically included in {@link #getOptions()}.
   * Override when implementing new browser types.
   * @return capabilities for browser
   */
  protected abstract O getBrowserSpecificOptions();

  protected O getCommonOptions() {
    O options = newOptions();
    options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, !isWebDriverRefuseSslCertificates());
    options.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, !isWebDriverAcceptTrustedSslCertificatesOnly());

    Proxy proxyToUse = getProxyToUse();
    if (proxyToUse != null) {
      options.setCapability(CapabilityType.PROXY, proxyToUse);
    }
    return options;
  }

  private static Proxy getProxyToUse() {
    Proxy proxy = null;
    if (GaleniumConfiguration.isUseBrowserMobProxy()) {
      proxy = BrowserProxyUtil.getSeleniumProxy();
    }
    else if (isHttpsProxyConfigured()) {
      proxy = new Proxy();
      String proxyHost = GaleniumConfiguration.getHttpsProxyHost();
      String proxyPort = GaleniumConfiguration.getHttpsProxyPort();
      proxy.setSslProxy(proxyHost + ":" + proxyPort);
      WebDriverManagement.getLogger().debug("Using Proxy Configuration for webdriver with host: " + proxyHost + " and Port: " + proxyPort);
    }
    return proxy;
  }

  private static boolean isHttpsProxyConfigured() {
    return StringUtils.isNotEmpty(GaleniumConfiguration.getHttpsProxyHost()) && StringUtils.isNotEmpty(GaleniumConfiguration.getHttpsProxyPort());
  }

  protected Logger getLogger() {
    return WebDriverManagement.getLogger();
  }

  protected abstract void log(O options);

  @SuppressWarnings("unchecked")
  protected O mergeCommonOptions(O options) {
    return (O)options.merge(getCommonOptions());
  }

  protected abstract O newOptions();
}
