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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.openqa.selenium.remote.SessionId;

import io.wcm.qa.galenium.exceptions.GaleniumException;

/**
 * Convenience methods to handle straight HTTP connections without going through Selenium.
 */
public final class HttpUtil {

  private HttpUtil() {
    // do not instantiate
  }

  static CloseableHttpClient getNewClient() {
    CloseableHttpClient client = HttpClientBuilder.create().build();
    return client;
  }

  /**
   * @param url to post to
   * @return response to POST
   */
  public static HttpResponse post(URL url) {
    CloseableHttpClient client = getNewClient();
    try {
      BasicHttpEntityEnclosingRequest postRequest = getPostRequest(url);
      return post(client, url, postRequest);
    }
    finally {
      HttpClientUtils.closeQuietly(client);
    }
  }

  private static HttpResponse post(CloseableHttpClient client, URL url, BasicHttpEntityEnclosingRequest postRequest) {
    HttpHost host = new HttpHost(getAddress(url));
    try {
      return client.execute(host, postRequest);
    }
    catch (IOException ex) {
      throw new GaleniumException("when executing request.", ex);
    }
  }

  private static InetAddress getAddress(URL url) {
    InetAddress address;
    try {
      address = InetAddress.getByName(url.getHost());
    }
    catch (UnknownHostException ex) {
      throw new GaleniumException("trying to construct address from URL.", ex);
    }
    return address;
  }

  static InputStreamReader getResponseReader(HttpResponse resp) throws IOException {
    return new InputStreamReader(resp.getEntity().getContent());
  }

  static URL getSessionUrl(String hostname, int port, SessionId session) throws MalformedURLException {
    String urlString = GridHostExtractor.HTTP + hostname + ":" + port + GridHostExtractor.PATH_GRID_API_TESTSESSION + session;
    return new URL(urlString);
  }

  static BasicHttpEntityEnclosingRequest getRequest(String hostname, int port, SessionId session) throws MalformedURLException {
    URL sessionURL = HttpUtil.getSessionUrl(hostname, port, session);
    return getPostRequest(sessionURL);
  }

  private static BasicHttpEntityEnclosingRequest getPostRequest(URL url) {
    return new BasicHttpEntityEnclosingRequest("POST", url.toExternalForm());
  }

}
