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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_INFO;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.auth.AuthType;

/**
 * Configuring the BrowserMob Proxy.
 */
public final class BrowserMobUtil {

  private static String BROWSER_MOB_PROXY = "galenium.proxy.browserMob";
  private static String SELENIUM_PROXY = "galenium.proxy.selenium";

  private BrowserMobUtil() {
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
    getBrowserMobProxy().autoAuthorization(domain, name, pass, AuthType.BASIC);
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
    getBrowserMobProxy().addHeader(name, value);
  }

  /**
   * @return Selenium proxy using BrowserMob Proxy
   */
  public static Proxy getSeleniumProxy() {
    Proxy seleniumProxy = (Proxy)GaleniumContext.get(SELENIUM_PROXY);
    if (seleniumProxy == null) {
      seleniumProxy = ClientUtil.createSeleniumProxy(getBrowserMobProxy());
      GaleniumContext.put(SELENIUM_PROXY, seleniumProxy);
    }
    return seleniumProxy;

  }

  public static BrowserMobProxy getBrowserMobProxy() {
    if (!GaleniumConfiguration.isUseBrowserMobProxy()) {
      throw new GaleniumException("set 'galenium.browsermob.proxy' to true before fetching browsermob proxy.");
    }
    BrowserMobProxy proxy = (BrowserMobProxy)GaleniumContext.get(BROWSER_MOB_PROXY);
    if (proxy == null) {
      proxy = new BrowserMobProxyServer();
      proxy.setMitmDisabled(false);
      proxy.start();
      GaleniumContext.put(BROWSER_MOB_PROXY, proxy);
    }
    return proxy;
  }

}
