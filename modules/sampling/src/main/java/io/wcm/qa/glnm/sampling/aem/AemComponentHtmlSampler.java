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

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.wcm.qa.glnm.aem.AemComponentUtil;
import io.wcm.qa.glnm.sampling.network.JsoupDocumentSampler;
import io.wcm.qa.glnm.sampling.network.base.JsoupElementBasedSampler;

/**
 * Sampler to fetch HTML from directly from an AEM component.
 */
public class AemComponentHtmlSampler extends JsoupElementBasedSampler<JsoupDocumentSampler, Document, String> {

  /**
   * @param url to fetch HTML from
   */
  public AemComponentHtmlSampler(String url) {
    super(new JsoupDocumentSampler(url));
  }

  /**
   * @param contentPath path to page
   * @param componentName name of component to check
   */
  public AemComponentHtmlSampler(String contentPath, String componentName) {
    this(AemComponentUtil.urlBuilder(contentPath, componentName).build().toString());
  }

  @Override
  protected String extractValueFromElement(Element element) {
    /*
     * Structure of HTML is roughly:
     * <html>
     *   <head></head>
     *   <body>
     *     <actual-component-code/>
     *   </body>
     * </html>
     */
    getLogger().debug("0 extracting from:\n " + element.outerHtml());
    Element htmlElement = element.child(0);
    getLogger().debug("1 extracting from:\n " + htmlElement.outerHtml());
    Element bodyElement = htmlElement.child(1);
    getLogger().debug("2 extracting from:\n " + bodyElement.outerHtml());
    String html = bodyElement.html();
    return html;
  }

  public Map<String, String> getRequestCookies() {
    return getInput().getRequestCookies();
  }

  /**
   * @param requestCookies to use when fetching document
   */
  public void setRequestCookies(Map<String, String> requestCookies) {
    getInput().setRequestCookies(requestCookies);
  }

}
