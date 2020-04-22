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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.jsoup.JsoupRawStringSampler;
import io.wcm.qa.glnm.sampling.transform.JsonSampler;

/**
 * Samples paths from JCQ query against AEM author.
 *
 * @since 3.0.0
 */
public class JcrQuerySampler extends JsonSampler<Sampler<String>> {

  private static final Predicate<String> HIT_KEY_FILTER = new HitKeyFilter();

  private static final Logger LOG = LoggerFactory.getLogger(JcrQuerySampler.class);

  private static final String PARAM_HITS = "p.hits";
  private static final String PARAM_LIMIT = "p.limit";
  private static final String PARAM_ORDERBY = "orderby";
  private static final String PARAM_PATH = "path";
  private static final String PATH_TO_QUERYBUILDER_JSON = "/bin/querybuilder.json";
  private static final String POSTFIX_OPERATION = ".operation";
  private static final String POSTFIX_VALUE = ".value";
  private static final String PREFIX_PROPERTY = "_property";
  private static final String VALUE_HITS_SELECTIVE = "selective";
  private static final String VALUE_LIKE = "like";
  private static final String VALUE_ORDERBYPATH = "path";
  private String hostName = "localhost";
  private boolean loginToAuthor = true;
  private int maxNumberOfResults = 1000;
  private String password = "admin";
  private String path;
  private int port = 4502;
  private final Map<String, String> propertiesLike = new HashMap<String, String>();
  private final Map<String, String> propertiesStrict = new HashMap<String, String>();
  private String protocol = "http";
  private String userName = "admin";

  /**
   * Constructor.
   */
  public JcrQuerySampler() {
    super();
  }

  /**
   * <p>addLikeProperty.</p>
   *
   * @param name of property to filter by
   * @param pattern jcr:like pattern to match value of property
   * @return this
   */
  public JcrQuerySampler addLikeProperty(String name, String pattern) {
    propertiesLike.put(name, pattern);
    return this;
  }

  /**
   * <p>addStrictProperty.</p>
   *
   * @param name of property to filter by
   * @param value exact value of property
   * @return this
   */
  public JcrQuerySampler addStrictProperty(String name, String value) {
    propertiesStrict.put(name, value);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, String> freshSample() {
    String url = buildUrl();
    JsoupRawStringSampler stringSampler = new JsoupRawStringSampler(url);
    stringSampler.setCookieSampler(getCookieSampler());
    stringSampler.setBodyOnly(true);
    setInput(stringSampler);
    Map<String, String> freshSample = super.freshSample();
    Set<Entry<String, String>> entrySet = freshSample.entrySet();
    if (LOG.isTraceEnabled()) {
    for (Entry<String, String> entry : entrySet) {
      String key = entry.getKey();
      String value = entry.getValue();
        LOG.trace("JSON: " + key + " -> " + value);
      }
    }

    return Maps.filterKeys(freshSample, HIT_KEY_FILTER);
  }

  /**
   * <p>Getter for the field <code>hostName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * <p>Getter for the field <code>maxNumberOfResults</code>.</p>
   *
   * @return a int.
   */
  public int getMaxNumberOfResults() {
    return maxNumberOfResults;
  }

  /**
   * <p>Getter for the field <code>password</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPassword() {
    return password;
  }

  /**
   * <p>Getter for the field <code>path</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getPath() {
    return path;
  }

  /**
   * <p>Getter for the field <code>port</code>.</p>
   *
   * @return a int.
   */
  public int getPort() {
    return port;
  }

  /**
   * <p>Getter for the field <code>protocol</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * <p>Getter for the field <code>userName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getUserName() {
    return userName;
  }

  /**
   * <p>isLoginToAuthor.</p>
   *
   * @return a boolean.
   */
  public boolean isLoginToAuthor() {
    return loginToAuthor;
  }

  /**
   * <p>Setter for the field <code>hostName</code>.</p>
   *
   * @param host to use for login and query
   */
  public void setHostName(String host) {
    this.hostName = host;
  }

  /**
   * <p>Setter for the field <code>loginToAuthor</code>.</p>
   *
   * @param login whether to login before querying
   */
  public void setLoginToAuthor(boolean login) {
    this.loginToAuthor = login;
  }

  /**
   * <p>Setter for the field <code>maxNumberOfResults</code>.</p>
   *
   * @param maxResults limit on number of returned results (default: 1000)
   */
  public void setMaxNumberOfResults(int maxResults) {
    this.maxNumberOfResults = maxResults;
  }

  /**
   * <p>Setter for the field <code>password</code>.</p>
   *
   * @param pass to use for login
   */
  public void setPassword(String pass) {
    this.password = pass;
  }

  /**
   * <p>Setter for the field <code>path</code>.</p>
   *
   * @param rootPath to use as constraint
   */
  public void setPath(String rootPath) {
    this.path = rootPath;
  }

  /**
   * <p>Setter for the field <code>port</code>.</p>
   *
   * @param portNumber to use for query and login
   */
  public void setPort(int portNumber) {
    this.port = portNumber;
  }

  /**
   * <p>Setter for the field <code>protocol</code>.</p>
   *
   * @param scheme to use for query and login
   */
  public void setProtocol(String scheme) {
    this.protocol = scheme;
  }

  /**
   * <p>Setter for the field <code>userName</code>.</p>
   *
   * @param user to use for login
   */
  public void setUserName(String user) {
    this.userName = user;
  }

  private String getQuery() {
    ArrayList<String> params = new ArrayList<String>();

    // static param names
    appendParam(params, PARAM_PATH, getPath());
    appendParam(params, PARAM_HITS, VALUE_HITS_SELECTIVE);
    appendParam(params, PARAM_LIMIT, Integer.toString(getMaxNumberOfResults()));
    appendParam(params, PARAM_ORDERBY, VALUE_ORDERBYPATH);

    // dynamic param names
    int propertyCounter = 1;
    for (Entry<String, String> entry : propertiesLike.entrySet()) {
      String prefix = propertyCounter++ + PREFIX_PROPERTY;
      appendParam(params, prefix, entry.getKey());
      appendParam(params, prefix + POSTFIX_VALUE, entry.getValue());
      appendParam(params, prefix + POSTFIX_OPERATION, VALUE_LIKE);
    }
    for (Entry<String, String> entry : propertiesStrict.entrySet()) {
      String prefix = propertyCounter++ + PREFIX_PROPERTY;
      String key = prefix + entry.getKey();
      appendParam(params, prefix, key);
      appendParam(params, prefix + POSTFIX_VALUE, entry.getValue());
    }

    return StringUtils.join(params, '&');
  }

  protected String buildUrl() {
    try {

      URI queryUri = new URI(
          getProtocol(),
          null,
          getHostName(),
          getPort(),
          PATH_TO_QUERYBUILDER_JSON,
          getQuery(),
          null);

      return queryUri.toURL().toString();
    }
    catch (URISyntaxException | MalformedURLException ex) {
      throw new GaleniumException("could not build query URL.", ex);
    }
  }

  protected Sampler<Map<String, String>> getCookieSampler() {
    String url = getProtocol() + "://" + getHostName();
    AemAuthorLoginSampler loginSampler = new AemAuthorLoginSampler(url, getPort());
    loginSampler.setUsername(getUserName());
    loginSampler.setPassword(getPassword());
    return loginSampler;
  }

  private static void appendParam(ArrayList<String> params, String key, String value) {
    params.add(
        new StringBuilder()
            .append(key)
            .append("=")
            .append(value)
            .toString());
  }

  private static final class HitKeyFilter implements Predicate<String> {

    @Override
    public boolean apply(@Nullable String input) {
      return StringUtils.startsWith(input, "hits[");
    }
  }

}
