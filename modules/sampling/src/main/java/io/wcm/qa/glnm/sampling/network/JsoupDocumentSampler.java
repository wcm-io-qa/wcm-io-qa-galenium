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

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.base.CachingBasedSampler;

/**
 * Uses {@link Jsoup} to fetch HTML from a URL.
 */
public class JsoupDocumentSampler extends CachingBasedSampler<Document> {

  private String url;

  /**
   * @param url to fetch HTML from
   */
  public JsoupDocumentSampler(String url) {
    super();
    this.setUrl(url);
  }

  public String getUrl() {
    return url;
  }

  @Override
  public Document sampleValue() {
    return getDocument();
  }

  /**
   * @return document from URL or rethrows {@link IOException} as {@link GaleniumException}
   */
  protected Document getDocument() {
    try {
      return getJsoupConnection().get();
    }
    catch (IOException ex) {
      throw new GaleniumException("When trying to fetch URL: '" + getUrl() + "'");
    }
  }

  /**
   * @return connection used to fetch document
   */
  protected Connection getJsoupConnection() {
    return Jsoup.connect(getUrl());
  }

  protected void setUrl(String url) {
    this.url = url;
  }

}
