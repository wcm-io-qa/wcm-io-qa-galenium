/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.sampling.jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.Connection.KeyVal;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.jsoup.base.JsoupBasedSampler;

/**
 * Samples cookies from a Jsoup network response.
 *
 * @since 3.0.0
 */
public class JsoupCookieSampler extends JsoupBasedSampler<Map<String, String>> {

  private static final Logger LOG = LoggerFactory.getLogger(JsoupCookieSampler.class);

  /**
   * <p>Constructor for JsoupCookieSampler.</p>
   *
   * @param url to fetch cookies from
   * @since 3.0.0
   */
  public JsoupCookieSampler(String url) {
    super(url);
  }

  private Method method = Method.POST;

  /** {@inheritDoc} */
  @Override
  public Map<String, String> freshSample() {
    return fetchCookies();
  }

  /**
   * <p>Setter for the field <code>method</code>.</p>
   *
   * @param requestMethod HTTP method to use for retrieval
   * @since 3.0.0
   */
  public void setMethod(Method requestMethod) {
    this.method = requestMethod;
  }

  protected Map<String, String> fetchCookies() {
    Connection connection = getJsoupConnection();

    try {
      connection.method(getMethod());

      Request request = connection.request();
      URL url = request.url();
      LOG.info("sending " + getMethod() + " request to '" + url + "'");
      logRequest(request);

      connection.execute();

      Response response = connection.response();
      if (LOG.isDebugEnabled()) {
        LOG.debug("response(" + response.url() + "): " + response.statusCode() + " - " + response.statusMessage());
      }
      logResponse(response);

      Map<String, String> cookies = response.cookies();
      if (LOG.isDebugEnabled()) {
        LOG.debug("got " + cookies.size() + " cookies from '" + url + "'");
      }

      return cookies;
    }
    catch (IOException ex) {
      throw new GaleniumException("Could not fetch cookies", ex);
    }

  }

  private void logResponse(Response response) {
    if (LOG.isTraceEnabled()) {
      for (Entry<String, String> header : response.headers().entrySet()) {
        LOG.trace("response-header: '" + header.getKey() + "' : '" + header.getValue() + "'");
      }
    }
  }

  private void logRequest(Request request) {
    if (LOG.isTraceEnabled()) {
      for (Entry<String, String> header : request.headers().entrySet()) {
        LOG.trace("request-header: '" + header.getKey() + "' : '" + header.getValue() + "'");
      }
      Collection<KeyVal> data = request.data();
      for (KeyVal keyVal : data) {
        LOG.trace(keyVal.contentType() + ": '" + keyVal.key() + "' : '" + keyVal.value());
      }
    }
  }

  protected Method getMethod() {
    return method;
  }

}
