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
package io.wcm.qa.glnm.example;

import java.io.IOException;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.wcm.qa.glnm.verification.base.VerificationBase;
import io.wcm.qa.glnm.verification.diff.FixedStringDiffVerification;
import io.wcm.qa.glnm.verification.util.Check;

public class ComponentSourceIT extends AbstractExampleBase {

  private static final String LOGIN_PATH = "/libs/granite/core/content/login.html/j_security_check";
  private static final String HOST_BASE_URL = "http://localhost:4502";
  private static final String URL_LOGIN = HOST_BASE_URL + LOGIN_PATH;
  private static final String LOGIN_TOKEN = "login-token";
  private String loginCookieValue;

  public ComponentSourceIT() {
    super(null);
  }

  @Test
  public void checkStageSource() throws IOException {
    Document document = fetchUrl(HOST_BASE_URL + "/content/wcm-io-samples/en/jcr:content/stage.html?wcmmode=disabled");
    String html = extractComponentHtml(document);

    VerificationBase<List<String>> verification = new FixedStringDiffVerification("Sample Stage", html);
    verification.setCaching(true);
    Check.verify(verification);
  }

  private String extractComponentHtml(Document document) {
    /*
     * Structure of HTML is roughly:
     * <html>
     *   <head></head>
     *   <body>
     *     <actual-component-code/>
     *   </body>
     * </html>
     */
    Element htmlElement = document.child(0);
    Element bodyElement = htmlElement.child(1);
    String html = bodyElement.html();
    return html;
  }

  @BeforeClass
  public void loginToAuthor() throws IOException {
    Connection loginConnection = Jsoup
        .connect(URL_LOGIN)
        .data("_charset_", "utf-8")
        .data("j_username", "admin")
        .data("j_password", "admin")
        .data("j_validate", "true")
        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        .postDataCharset("UTF-8")
        .header("X-Requested-With", "XMLHttpRequest")
        .ignoreHttpErrors(true);
    loginConnection.post();
    getLogger().debug("login POST done.");
    Response response = loginConnection.response();
    getLogger().info(response.statusCode() + ": " + response.statusMessage());
    setLoginCookieValue(response.cookie(LOGIN_TOKEN));
    Check.verify(new FixedStringDiffVerification("Login POST status", response.statusMessage()));
  }

  public void setLoginCookieValue(String loginCookieValue) {
    this.loginCookieValue = loginCookieValue;
  }

  private Document fetchUrl(String url) throws IOException {
    Document document = Jsoup
        .connect(url)
        .cookie(LOGIN_TOKEN, getLoginCookieValue())
        .get();
    getLogger().info(document.title());
    return document;
  }

  protected String getLoginCookieValue() {
    return loginCookieValue;
  }

  @Override
  protected String getRelativePath() {
    return null;
  }
}
