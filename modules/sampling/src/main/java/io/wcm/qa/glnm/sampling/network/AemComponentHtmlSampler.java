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
package io.wcm.qa.glnm.sampling.network;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Sampler to fetch HTML from directly from an AEM component.
 */
public class AemComponentHtmlSampler extends JsoupElementSamplerBase<JsoupDocumentSampler, Document, String> {

  /**
   * @param url to fetch HTML from
   */
  public AemComponentHtmlSampler(String url) {
    super(new JsoupDocumentSampler(url));
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
    Element htmlElement = element.child(0);
    Element bodyElement = htmlElement.child(1);
    String html = bodyElement.html();
    return html;
  }

}
