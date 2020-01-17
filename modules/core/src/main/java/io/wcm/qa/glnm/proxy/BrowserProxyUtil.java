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

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.BrowserUpProxyServer;
import com.browserup.bup.client.ClientUtil;
import com.browserup.bup.proxy.auth.AuthType;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Configuring the BrowserMob Proxy.
 *
 * @since 1.0.0
 */
public final class BrowserProxyUtil {

  private static final String BROWSER_PROXY = "galenium.proxy.browser";
  private static final String SELENIUM_PROXY = "galenium.proxy.selenium";
  private static final Logger LOG = LoggerFactory.getLogger(BrowserProxyUtil.class);

  private BrowserProxyUtil() {
    // do not instantiate
  }

  /**
   * Add basic authentication header to every request.
   *
   * @param name user name to use for auth
   * @param pass password to use for auth
   * @since 3.0.0
   */
  public static void addBasicAuth(String name, String pass) {
    addBasicAuth("", name, pass);
  }

  /**
   * Add basic authentication header to every request.
   *
   * @param url to extract protected domain from
   * @param name user name to use for auth
   * @param pass password to use for auth
   * @since 3.0.0
   */
  public static void addBasicAuth(String url, String name, String pass) {
    String domain = extractDomain(url);
    LOG.debug("setting basic auth for domain '" + domain + "'");
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
   *
   * @param name header name
   * @param value header value
   * @since 3.0.0
   */
  public static void addHeader(String name, String value) {
    LOG.debug("adding header: " + name);
    getBrowserProxy().addHeader(name, value);
  }

  /**
   * <p>getSeleniumProxy.</p>
   *
   * @return Selenium proxy using BrowserMob Proxy
   * @since 3.0.0
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
   *
   * @return the BrowserUp Proxy for the current thread
   * @since 3.0.0
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
