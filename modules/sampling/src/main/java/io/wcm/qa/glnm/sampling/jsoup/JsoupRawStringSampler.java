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

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.jsoup.base.JsoupElementBasedSampler;

/**
 * Strips the Jsoup boiler plate and returns just the raw text from the response. Useful for non-HTML samples.
 */
public class JsoupRawStringSampler extends JsoupElementBasedSampler<JsoupDocumentSampler, Document, String> {

  private boolean bodyOnly;

  /**
   * @param url to fetch document from
   */
  public JsoupRawStringSampler(String url) {
    super(new JsoupDocumentSampler(url));
  }

  @Override
  protected Element extractElement(Document inputSample) {
    return super.extractElement(inputSample);
  }

  @Override
  protected String extractValueFromElement(Element element) {
    /*
     * Structure of HTML is roughly:
     * <html>
     *   <head />
     *   <body />
     * </html>
     */
    if (isBodyOnly()) {
      Element htmlElement = element.child(0);
      Element bodyElement = htmlElement.child(1);
      return bodyElement.html();
    }

    return element.html();
  }

  public Sampler<Map<String, String>> getCookieSampler() {
    return getInput().getCookieSampler();
  }

  /**
   * @param cookieSampler passed to document sampler
   */
  public void setCookieSampler(Sampler<Map<String, String>> cookieSampler) {
    getInput().setCookieSampler(cookieSampler);
  }

  public boolean isBodyOnly() {
    return bodyOnly;
  }

  public void setBodyOnly(boolean bodyOnly) {
    this.bodyOnly = bodyOnly;
  }
}
