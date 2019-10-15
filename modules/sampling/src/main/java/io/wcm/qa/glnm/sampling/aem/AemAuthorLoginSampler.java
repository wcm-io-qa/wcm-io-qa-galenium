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

import org.jsoup.Connection;
import org.jsoup.Connection.Method;

import io.wcm.qa.glnm.sampling.jsoup.JsoupCookieSampler;

/**
 * Fetches login cookies from AEM author instance.
 */
public class AemAuthorLoginSampler extends JsoupCookieSampler {

  private static final String POST_DATA_CHARSET = "UTF-8";
  private static final String LOGIN_PATH = "/libs/granite/core/content/login.html/j_security_check";
  private String user = "admin";
  private String pass = "admin";


  /**
   * Login to 'http://localhost:4502'.
   */
  public AemAuthorLoginSampler() {
    this("http://localhost");
  }

  /**
   * Login using default port '4502'.
   * @param url to author instance
   */
  public AemAuthorLoginSampler(String url) {
    this(url, 4502);
  }

  /**
   * @param url to author instance
   * @param port of author instance
   */
  public AemAuthorLoginSampler(String url, int port) {
    super(buildLoginUrl(url, port));
  }

  public void setPassword(String password) {
    pass = password;
  }

  public void setUsername(String username) {
    user = username;
  }

  @Override
  protected Connection getJsoupConnection() {
    Connection connection = super.getJsoupConnection()
        .data("_charset_", "utf-8")
        .data("j_username", getUsername())
        .data("j_password", getPassword())
        .data("j_validate", "true")
        .header("Content-Type", "application/x-www-form-urlencoded; charset=" + POST_DATA_CHARSET)
        .postDataCharset(POST_DATA_CHARSET)
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

  private static String buildLoginUrl(String authorHostUrl, int port) {
    if (port > 0) {
      return authorHostUrl + ":" + port + LOGIN_PATH;
    }
    return authorHostUrl + LOGIN_PATH;

  }

}
