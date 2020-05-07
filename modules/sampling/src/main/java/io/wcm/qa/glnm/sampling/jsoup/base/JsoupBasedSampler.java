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
package io.wcm.qa.glnm.sampling.jsoup.base;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.base.CachingBasedSampler;

/**
 * Functionality that helps building Jsoup based samplers.
 *
 * @param <T> type of sample returned by sampler
 * @since 3.0.0
 */
public abstract class JsoupBasedSampler<T> extends CachingBasedSampler<T> {

  private static final Logger LOG = LoggerFactory.getLogger(JsoupBasedSampler.class);
  private JsoupConnectionProvider connectionProvider;
  private Sampler<Map<String, String>> cookieSampler;
  private Map<String, String> requestCookies = new HashMap<String, String>();
  private String url;

  /**
   * <p>Constructor for JsoupBasedSampler.</p>
   *
   * @param url to connect to
   * @since 3.0.0
   */
  public JsoupBasedSampler(String url) {
    setUrl(url);
  }

  /**
   * <p>Getter for the field <code>cookieSampler</code>.</p>
   *
   * @return a {@link io.wcm.qa.glnm.sampling.Sampler} object.
   * @since 3.0.0
   */
  public Sampler<Map<String, String>> getCookieSampler() {
    return cookieSampler;
  }

  /**
   * <p>Getter for the field <code>requestCookies</code>.</p>
   *
   * @return a {@link java.util.Map} object.
   * @since 3.0.0
   */
  public Map<String, String> getRequestCookies() {
    return requestCookies;
  }

  /**
   * <p>Getter for the field <code>url</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   * @since 3.0.0
   */
  public String getUrl() {
    return url;
  }

  protected void setConnectionProvider(JsoupConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  /**
   * <p>Setter for the field <code>cookieSampler</code>.</p>
   *
   * @param cookieSampler a {@link io.wcm.qa.glnm.sampling.Sampler} object.
   * @since 3.0.0
   */
  public void setCookieSampler(Sampler<Map<String, String>> cookieSampler) {
    this.cookieSampler = cookieSampler;
  }

  /**
   * <p>Setter for the field <code>requestCookies</code>.</p>
   *
   * @param cookies a {@link java.util.Map} object.
   * @since 3.0.0
   */
  public void setRequestCookies(Map<String, String> cookies) {
    this.requestCookies = cookies;
  }

  /**
   * @return {@link JsoupConnectionProvider} to use for connection
   */
  protected JsoupConnectionProvider getConnectionProvider() {
    if (connectionProvider == null) {
      connectionProvider = new JsoupConnectionProvider() {

        @Override
        public Connection getConnection() {
          return Jsoup.connect(getUrl());
        }

      };
    }
    return connectionProvider;
  }

  /**
   * @return connection used to fetch document
   */
  protected Connection getJsoupConnection() {
    Connection connection = getConnectionProvider().getConnection();
    if (useRequestCookies()) {
      connection.cookies(getRequestCookies());
    }
    if (useCookieSampler()) {
      connection.cookies(getCookieSampler().sampleValue());
    }
    connection.ignoreContentType(true);
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, InsecureTrustManagerFactory.INSTANCE.getTrustManagers(), null);
      SSLSocketFactory socketFactory = context.getSocketFactory();
      connection.sslSocketFactory(socketFactory);
    }
    catch (NoSuchAlgorithmException | KeyManagementException ex) {
      LOG.warn("Could not initialize SSL context.", ex);
    }
    return connection;
  }

  protected void setUrl(String newUrl) {
    this.url = newUrl;
  }

  protected boolean useCookieSampler() {
    return getCookieSampler() != null;
  }

  protected boolean useRequestCookies() {
    return getRequestCookies() != null;
  }

}
