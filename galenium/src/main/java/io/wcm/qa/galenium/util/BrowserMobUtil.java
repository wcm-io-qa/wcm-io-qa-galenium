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

import org.openqa.selenium.Proxy;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.auth.AuthType;

/**
 * Configuring the BrowserMob Proxy.
 */
public final class BrowserMobUtil {

  private static BrowserMobProxyServer proxy;
  private static Proxy seleniumProxy;

  private BrowserMobUtil() {
    // do not instantiate
  }

  /**
   * Add basic authentication header to every request.
   * @param name user name to use for auth
   * @param pass password to use for auth
   */
  public static void addBasicAuth(String name, String pass) {
    getLogger().debug(MARKER_INFO, "setting basic auth.");
    getBrowserMobProxy().autoAuthorization("", name, pass, AuthType.BASIC);
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
    if (seleniumProxy == null) {
      seleniumProxy = ClientUtil.createSeleniumProxy(getBrowserMobProxy());
    }
    return seleniumProxy;

  }

  /**
   * @return the started BrowserMob Proxy
   */
  public static BrowserMobProxy getBrowserMobProxy() {
    if (proxy == null) {
      proxy = new BrowserMobProxyServer();
      proxy.start();
    }
    return proxy;
  }

  public static String getProxyAsString() {
    return "localhost:" + getBrowserMobProxy().getPort();
  }

}
