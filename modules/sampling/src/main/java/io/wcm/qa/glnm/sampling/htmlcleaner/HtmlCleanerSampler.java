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
package io.wcm.qa.glnm.sampling.htmlcleaner;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlSerializer;
import org.htmlcleaner.PrettyHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import io.wcm.qa.glnm.sampling.jsoup.JsoupRawStringSampler;

/**
 * Uses HTML cleaner to tidy up HTML samples fetched with {@link Jsoup}.
 */
public class HtmlCleanerSampler extends JsoupRawStringSampler {

  private CleanerProperties htmlCleanerProperties;
  private HtmlSerializer htmlSerializer;

  /**
   * @param url to sample from
   */
  public HtmlCleanerSampler(String url) {
    this(url, new CleanerProperties());
  }

  /**
   * @param url to sample from
   * @param cleanerProperties properties to configure {@link HtmlCleaner}
   */
  public HtmlCleanerSampler(String url, CleanerProperties cleanerProperties) {
    super(url);
    setBodyOnly(false);
    setHtmlCleanerProperties(cleanerProperties);
  }

  @Override
  protected String extractValueFromElement(Element element) {
    String jsoupHtml = super.extractValueFromElement(element);
    TagNode cleanedHtml = getHtmlCleaner().clean(jsoupHtml);
    return getHtmlSerializer().getAsString(cleanedHtml);
  }

  private HtmlCleaner getHtmlCleaner() {
    HtmlCleaner htmlCleaner = new HtmlCleaner(getHtmlCleanerProperties());
    return htmlCleaner;
  }

  public CleanerProperties getHtmlCleanerProperties() {
    return htmlCleanerProperties;
  }

  public void setHtmlCleanerProperties(CleanerProperties htmlCleanerProperties) {
    this.htmlCleanerProperties = htmlCleanerProperties;
  }

  /**
   * @return serializer to use for HTML string serialization
   */
  public HtmlSerializer getHtmlSerializer() {
    if (htmlSerializer == null) {
      htmlSerializer = new PrettyHtmlSerializer(getHtmlCleanerProperties());
    }
    return htmlSerializer;
  }

  public void setHtmlSerializer(HtmlSerializer htmlSerializer) {
    this.htmlSerializer = htmlSerializer;
  }

}
