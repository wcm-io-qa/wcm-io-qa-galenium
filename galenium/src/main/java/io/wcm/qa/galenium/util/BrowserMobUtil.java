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

import java.nio.charset.Charset;
import java.util.Base64;

import org.openqa.selenium.Proxy;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

/**
 * Configuring the BrowserMob Proxy.
 */
public final class BrowserMobUtil {

  private static final String HEADER_NAME_BASIC_AUTHENTICATION = "Authorization";
  private static Proxy seleniumProxy;
  private static BrowserMobProxyServer proxy;

  private BrowserMobUtil() {
    // do not instantiate
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

  private static BrowserMobProxy getBrowserMobProxy() {
    if (proxy == null) {
      proxy = new BrowserMobProxyServer();
      proxy.start();
    }
    return proxy;
  }

  /**
   * Add basic authentication header to every request.
   * @param name user name to use for auth
   * @param pass password to use for auth
   */
  public static void addBasicAuth(String name, String pass) {
    String credentialsAsString = name + ":" + pass;
    byte[] credentialsAsBytes = credentialsAsString.getBytes(Charset.forName("UTF-8"));
    String encodedCredentials = Base64.getEncoder().encodeToString(credentialsAsBytes);
    getBrowserMobProxy().addHeader(HEADER_NAME_BASIC_AUTHENTICATION, "Basic " + encodedCredentials);
  }

  /**
   * Removes basic authentication header so that BrowserMob Proxy does not add them to any requests anymore.
   */
  public static void removeBasicAuth() {
    getBrowserMobProxy().removeHeader(HEADER_NAME_BASIC_AUTHENTICATION);
  }

  /**
   * Add header to every request.
   * @param name header name
   * @param value header value
   */
  public static void addHeader(String name, String value) {
    getBrowserMobProxy().addHeader(name, value);
  }

}
