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
package io.wcm.qa.glnm.util;

import java.io.IOException;
import java.io.InputStreamReader;
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
import org.apache.http.message.BasicNameValuePair;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Convenience methods to handle straight HTTP connections without going through Selenium.
 */
public final class HttpUtil {

  private HttpUtil() {
    // do not instantiate
  }

  /**
   * @return fresh closable HTTP client
   */
  public static CloseableHttpClient getNewClient() {
    CloseableHttpClient client = HttpClientBuilder.create().build();
    return client;
  }

  /**
   * @param resp to read from
   * @return stream reader for response contents
   * @throws IOException
   */
  public static InputStreamReader getResponseReader(HttpResponse resp) throws IOException {
    return new InputStreamReader(resp.getEntity().getContent());
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

}
