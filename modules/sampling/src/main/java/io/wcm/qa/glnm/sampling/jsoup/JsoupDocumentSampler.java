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

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.jsoup.base.JsoupBasedSampler;

/**
 * Uses {@link Jsoup} to fetch HTML from a URL.
 */
public class JsoupDocumentSampler extends JsoupBasedSampler<Document> {

  /**
   * @param url to fetch HTML from
   */
  public JsoupDocumentSampler(String url) {
    super(url);
  }

  @Override
  public Document freshSample() {
    return getDocument();
  }

  /**
   * @return document from URL or rethrows {@link IOException} as {@link GaleniumException}
   */
  protected Document getDocument() {
    try {
      Connection connection = getJsoupConnection();
      if (connection == null) {
        throw new GaleniumException("cannot get document from null connection.");
      }
      getLogger().info("fetching document from '" + connection.request().url() + "'");
      Document document = connection.get();
      return document;
    }
    catch (IOException ex) {
      if (ex instanceof HttpStatusException) {
        getLogger().warn("STATUS: " + ((HttpStatusException)ex).getStatusCode());
      }
      getLogger().debug("Could not fetch Document: " + getUrl(), ex);
      throw new GaleniumException("When trying to fetch URL: '" + getUrl() + "'", ex);
    }
  }

}
