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
package io.wcm.qa.glnm.sampling.aem;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.jsoup.JsoupCookieSampler;

/**
 * Fetches login cookies from AEM author instance.
 *
 * @since 3.0.0
 */
public class AemAuthorLoginSampler extends JsoupCookieSampler {

  private static final String DEFAULT_AUTHOR = "localhost";
  private static final int DEFAULT_AUTHOR_PORT = 4502;
  private static final String DEFAULT_PASSWORD = "admin";
  private static final String DEFAULT_USER = "admin";
  private static final Logger LOG = LoggerFactory.getLogger(AemAuthorLoginSampler.class);
  private static final String LOGIN_PATH = "/libs/granite/core/content/login.html/j_security_check";
  private static final String POST_DATA_CHARSET = "UTF-8";

  private boolean needsXhr;
  private String pass = DEFAULT_PASSWORD;
  private String user = DEFAULT_USER;


  /**
   * Login to 'http://localhost:4502'.
   */
  public AemAuthorLoginSampler() {
    this(DEFAULT_AUTHOR);
  }

  /**
   * Login using default port '4502'.
   *
   * @param authorHostname name of author instance
   */
  public AemAuthorLoginSampler(String authorHostname) {
    this(authorHostname, DEFAULT_AUTHOR_PORT);
  }

  /**
   * <p>Constructor for AemAuthorLoginSampler.</p>
   *
   * @param url to author instance
   * @param port of author instance
   */
  public AemAuthorLoginSampler(String url, int port) {
    super(buildLoginUrl(url, port));
  }

  /**
   * Set whether to use XmlHttpRequests to fetch cookies. Activating XHR
   * leads to a GET request executed prior to the actual cookie fetching
   * request.
   *
   * @param needsXhr whether to activate XHR
   */
  public void setNeedsXhr(boolean needsXhr) {
    this.needsXhr = needsXhr;
  }

  /**
   * <p>setPassword.</p>
   *
   * @param password a {@link java.lang.String} object.
   */
  public void setPassword(String password) {
    pass = password;
  }

  /**
   * <p>setUsername.</p>
   *
   * @param username a {@link java.lang.String} object.
   */
  public void setUsername(String username) {
    user = username;
  }

  private void openXhrConnection(Connection jsoupConnection) {
    try {
      if (LOG.isDebugEnabled()) {
        LOG.debug("opening XHR connection via GET: " + jsoupConnection.request().url());
      }
      jsoupConnection.header("X-Requested-With", "XMLHttpRequest");
      jsoupConnection.get();
      if (LOG.isTraceEnabled()) {
        LOG.trace("done opening XHR connection via GET " + toString(jsoupConnection.response()));
      }
    }
    catch (IOException ex) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("request failed: " + toString(jsoupConnection.request()));
        LOG.debug("response of failed request: " + toString(jsoupConnection.response()));
      }
      throw new GaleniumException("When attempting to initialize XHR.", ex);
    }
  }

  private static String toString(Request request) {
    StringBuilder toString = new StringBuilder()
        .append(request.method())
        .append(" ")
        .append(request.url());
    return toString.toString();
  }

  private static String toString(Response response) {
    StringBuilder toString = new StringBuilder()
        .append(response.method())
        .append(" ")
        .append(response.url())
        .append(": ")
        .append(response.statusCode())
        .append(" - ")
        .append(response.statusMessage());
    return toString.toString();
  }

  @Override
  protected Map<String, String> fetchCookies() {
    Connection jsoupConnection = getJsoupConnection();
    if (needsXhr()) {
      openXhrConnection(jsoupConnection);
    }
    return super.fetchCookies();
  }

  @Override
  protected Connection getJsoupConnection() {
    Connection connection = super.getJsoupConnection()
          .header("Accept", "*/*")
          .data("_charset_", "utf-8")
          .data("j_username", getUsername())
          .data("j_password", getPassword())
          .data("j_validate", "true")
          .header("Content-Type", "application/x-www-form-urlencoded; charset=" + POST_DATA_CHARSET)
          .postDataCharset(POST_DATA_CHARSET)
          .referrer(getUrl())
          .header("Cache-Control", "no-cache")
          .header("Pragma", "no-cache")
          .ignoreHttpErrors(true);
    return connection;
  }

  @Override
  protected Method getMethod() {
    return Method.POST;
  }

  protected String getPassword() {
    return pass;
  }

  protected String getUsername() {
    return user;
  }

  protected boolean needsXhr() {
    return needsXhr;
  }

  private static String buildLoginUrl(String authorHostUrl, int port) {
    if (port > 0) {
      return authorHostUrl + ":" + port + LOGIN_PATH;
    }
    return authorHostUrl + LOGIN_PATH;
  }

}
