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
package io.wcm.qa.galenium.storage.cookies.fetcher;

import java.util.Arrays;
import java.util.Collection;

import io.wcm.qa.galenium.interaction.Aem;
import io.wcm.qa.galenium.storage.cookies.CookieFetcher;

/**
 * Fetches author login cookie.
 */
public class AuthorLoginFetcher implements CookieFetcher {

  private static final Collection<String> COOKIE_NAMES = Arrays.asList("login-token");
  private String name;
  private String pass;
  private String initialUrl;
  private String finalUrl;

  /**
   * @param loginUrl URL to call and to check for
   * @param name AEM author user
   * @param pass AEM author password
   */
  public AuthorLoginFetcher(String loginUrl, String name, String pass) {
    this(loginUrl, loginUrl, name, pass);
  }

  /**
   * @param initialUrl to call
   * @param finalUrl to check for after login
   * @param name AEM author user
   * @param pass AEM author password
   */
  public AuthorLoginFetcher(String initialUrl, String finalUrl, String name, String pass) {
    setInitialUrl(initialUrl);
    setFinalUrl(finalUrl);
    setName(name);
    setPass(pass);
  }

  @Override
  public boolean fetchItems() {
    return Aem.loginToAuthor(getInitialUrl(), getFinalUrl(), getName(), getPass());
  }

  @Override
  public Collection<String> getItemNames() {
    return COOKIE_NAMES;
  }

  @Override
  public String getFetcherName() {
    return "author-login_" + name;
  }

  public String getName() {
    return name;
  }

  public String getPass() {
    return pass;
  }

  public String getInitialUrl() {
    return initialUrl;
  }

  protected void setName(String name) {
    this.name = name;
  }

  protected void setPass(String pass) {
    this.pass = pass;
  }

  protected void setInitialUrl(String url) {
    this.initialUrl = url;
  }

  public String getFinalUrl() {
    return finalUrl;
  }

  protected void setFinalUrl(String finalUrl) {
    this.finalUrl = finalUrl;
  }

}
