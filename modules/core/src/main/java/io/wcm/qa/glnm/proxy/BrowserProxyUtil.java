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
package io.wcm.qa.glnm.proxy;

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.auth.AuthType;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Configuring the BrowserMob Proxy.
 */
public final class BrowserProxyUtil {

  private static final String BROWSER_PROXY = "galenium.proxy.browser";
  private static final String SELENIUM_PROXY = "galenium.proxy.selenium";

  private BrowserProxyUtil() {
    // do not instantiate
  }

  /**
   * Add basic authentication header to every request.
   * @param name user name to use for auth
   * @param pass password to use for auth
   */
  public static void addBasicAuth(String name, String pass) {
    addBasicAuth("", name, pass);
  }

  /**
   * Add basic authentication header to every request.
   * @param url to extract protected domain from
   * @param name user name to use for auth
   * @param pass password to use for auth
   */
  public static void addBasicAuth(String url, String name, String pass) {
    String domain = extractDomain(url);
    getLogger().debug(MARKER_INFO, "setting basic auth for domain '" + domain + "'");
    getBrowserProxy().autoAuthorization(domain, name, pass, AuthType.BASIC);
  }

  private static String extractDomain(String url) {
    String host = getHostFromUrl(url);
    String domain;
    if (StringUtils.isNotBlank(host)) {
      domain = host;
    }
    else {
      domain = url;
    }
    return domain;
  }

  private static String getHostFromUrl(String url) {
    URI rawUri = URI.create(url);
    String host = rawUri.getHost();
    return host;
  }

  /**
   * Add header to every request.
   * @param name header name
   * @param value header value
   */
  public static void addHeader(String name, String value) {
    getLogger().debug(MARKER_INFO, "adding header: " + name);
    getBrowserProxy().addHeader(name, value);
  }

  /**
   * @return Selenium proxy using BrowserMob Proxy
   */
  public static Proxy getSeleniumProxy() {
    Proxy seleniumProxy = (Proxy)GaleniumContext.get(SELENIUM_PROXY);
    if (seleniumProxy == null) {
      seleniumProxy = ClientUtil.createSeleniumProxy(getBrowserProxy());
      GaleniumContext.put(SELENIUM_PROXY, seleniumProxy);
    }
    return seleniumProxy;

  }

  /**
   * BrowserMob Proxy from Galenium context.
   * @return the BrowserUp Proxy for the current thread
   */
  public static BrowserUpProxy getBrowserProxy() {
    if (!GaleniumConfiguration.isUseBrowserProxy()) {
      throw new GaleniumException("set 'galenium.browser.proxy' to true before fetching browser proxy.");
    }
    BrowserUpProxy proxy = (BrowserUpProxy)GaleniumContext.get(BROWSER_PROXY);
    if (proxy == null) {
      proxy = new BrowserUpProxyServer();
      proxy.setMitmDisabled(false);
      proxy.setTrustAllServers(true);
      proxy.start();
      GaleniumContext.put(BROWSER_PROXY, proxy);
    }
    return proxy;
  }

}
