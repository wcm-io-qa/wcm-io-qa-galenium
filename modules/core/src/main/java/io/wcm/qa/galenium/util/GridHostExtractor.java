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
package io.wcm.qa.galenium.util;


import static io.wcm.qa.galenium.util.GaleniumContext.getDriver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import com.github.wnameless.json.flattener.JsonFlattener;

/**
 * Utility class to fetch name of Selenium Grid node.
 *
 * @since 1.0.0
 */
public final class GridHostExtractor {

  static final String PATH_GRID_API_TESTSESSION = "/grid/api/testsession?session=";
  static final String HTTP = "http://";
  /**
   * Constant used when no grid host could be retrieved.
   */
  public static final String NO_HOST_RETRIEVED = "NO_HOST_RETRIEVED";
  private static final String NOT_REMOTE = "NOT_REMOTE";
  private static final String EMPTY_PROXY_ID = "EMPTY_PROXY_ID";

  private GridHostExtractor() {
    // do not instantiate
  }

  /**
   * <p>getGridNodeHostname.</p>
   *
   * @return the hostname of the Selenium Grid node the test is run on or NO_HOST_RETRIEVED if
   *         hostname cannot be retrieved or NOT_REMOTE if driver is not a
   *         {@link org.openqa.selenium.remote.RemoteWebDriver}.
   */
  public static String getGridNodeHostname() {
    WebDriver driver = getDriver();
    if (driver instanceof RemoteWebDriver) {
      String host = System.getProperty("selenium.host");
      int port = Integer.parseInt(System.getProperty("selenium.port", "4444"));
      SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
      return getHostnameAndPort(host, port, sessionId);
    }
    return NOT_REMOTE;
  }

  /**
   * <p>getHostnameAndPort.</p>
   *
   * @param hostname hostname for Selenium Grid hub
   * @param port port for Selenium Grid hub
   * @param session session ID to use
   * @return proxy ID of associated Selenium Grid node
   */
  public static String getHostnameAndPort(String hostname, int port, SessionId session) {

    try {
      BasicHttpEntityEnclosingRequest request = HttpUtil.getRequest(hostname, port, session);
      HttpHost host = new HttpHost(hostname, port);
      CloseableHttpClient client = HttpUtil.getNewClient();
      HttpResponse response = client.execute(host, request);
      Map<String, Object> jsonAsMap = extractObject(response);
      client.close();
      Object proxyId = jsonAsMap.get("proxyId");
      if (proxyId != null) {
        String proxyIdValue = proxyId.toString();
        if (StringUtils.isNotBlank(proxyIdValue)) {
          return proxyIdValue;
        }
        return EMPTY_PROXY_ID;
      }
      return NO_HOST_RETRIEVED;
    }
    catch (IOException ex) {
      return NO_HOST_RETRIEVED;
    }

  }

  private static Map<String, Object> extractObject(HttpResponse resp) throws IOException {
    InputStreamReader jsonReader = HttpUtil.getResponseReader(resp);
    JsonFlattener jsonFlattener = new JsonFlattener(jsonReader);
    return jsonFlattener.flattenAsMap();
  }

}
