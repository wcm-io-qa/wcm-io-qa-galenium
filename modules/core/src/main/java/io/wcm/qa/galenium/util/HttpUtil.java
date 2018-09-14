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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicNameValuePair;
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
   * Posts parameters as form entity to URL.
   * @param url to post to
   * @param paramMap to send with request
   * @return response to POST
   */
  public static HttpResponse postForm(URL url, Map<String, String> paramMap) {
    ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
    for (Entry<String, String> entry : paramMap.entrySet()) {
      String name = entry.getKey();
      String value = entry.getValue();
      NameValuePair nameValuePair = new BasicNameValuePair(name, value);
      parameters.add(nameValuePair);
    }
    CloseableHttpClient client = null;
    try {
      UrlEncodedFormEntity encodedFormData = new UrlEncodedFormEntity(parameters);
      HttpPost httpPost = new HttpPost(url.toURI());
      httpPost.setEntity(encodedFormData);
      client = getNewClient();
      return client.execute(httpPost);
    }
    catch (URISyntaxException | IOException ex) {
      throw new GaleniumException("could not encode form post data.");
    }
    finally {
      HttpClientUtils.closeQuietly(client);
    }
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
