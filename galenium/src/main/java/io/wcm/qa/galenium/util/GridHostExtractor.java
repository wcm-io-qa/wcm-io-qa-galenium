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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.openqa.selenium.remote.JsonException;
import org.openqa.selenium.remote.SessionId;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Utility class to fetch name of Selenium Grid node.
 */
@SuppressWarnings("deprecation")
public final class GridHostExtractor {

  private GridHostExtractor() {
    // do not instantiate
  }

  /**
   * @param hostname hostname for Selenium Grid hub
   * @param port port for Selenium Grid hub
   * @param session session ID to use
   * @return proxy ID of associated Selenium Grid node
   */
  public static String getHostnameAndPort(String hostname, int port, SessionId session) {

    try {
      HttpHost host = new HttpHost(hostname, port);
      DefaultHttpClient client = new DefaultHttpClient();
      URL sessionURL = new URL("http://" + hostname + ":" + port + "/grid/api/testsession?session=" + session);
      BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", sessionURL.toExternalForm());
      HttpResponse response = client.execute(host, r);
      JsonObject object = extractObject(response);
      client.close();
      return object.get("proxyId").getAsString();
    }
    catch (Throwable ex) {
      return "NO_HOST_RETRIEVED";
    }

  }

  private static JsonObject extractObject(HttpResponse resp) throws IOException, JsonException {
    BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
    StringBuffer s = new StringBuffer();
    String line;
    while ((line = rd.readLine()) != null) {
      s.append(line);
    }
    rd.close();
    JsonParser parser = new JsonParser();
    JsonElement parsed = parser.parse(s.toString());
    JsonObject objToReturn = parsed.getAsJsonObject();
    return objToReturn;
  }
}