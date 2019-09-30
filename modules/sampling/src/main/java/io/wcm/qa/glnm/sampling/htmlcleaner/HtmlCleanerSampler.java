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

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlSerializer;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import io.wcm.qa.glnm.sampling.htmlcleaner.visitors.CssClassSorter;
import io.wcm.qa.glnm.sampling.htmlcleaner.visitors.HtmlAttributeSorter;
import io.wcm.qa.glnm.sampling.jsoup.JsoupRawStringSampler;

/**
 * Uses HTML cleaner to tidy up HTML samples fetched with {@link Jsoup}.
 */
public class HtmlCleanerSampler extends JsoupRawStringSampler {

  private CleanerProperties htmlCleanerProperties;

  private HtmlSerializer htmlSerializer;

  private boolean sortAttributes = true;

  private boolean sortCssClasses = true;

  private List<TagNodeVisitor> visitors = new ArrayList<TagNodeVisitor>();

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

  /**
   * @param visitor to clean HTML
   */
  public void addVisitor(TagNodeVisitor visitor) {
    getVisitors().add(visitor);
  }

  public CleanerProperties getHtmlCleanerProperties() {
    return htmlCleanerProperties;
  }

  /**
   * @return serializer to use for HTML string serialization
   */
  public HtmlSerializer getHtmlSerializer() {
    if (htmlSerializer == null) {
      htmlSerializer = new SimpleHtmlSerializer(getHtmlCleanerProperties());
    }
    return htmlSerializer;
  }

  public boolean isSortAttributes() {
    return sortAttributes;
  }

  public boolean isSortCssClasses() {
    return sortCssClasses;
  }

  public void setHtmlCleanerProperties(CleanerProperties htmlCleanerProperties) {
    this.htmlCleanerProperties = htmlCleanerProperties;
  }

  public void setHtmlSerializer(HtmlSerializer htmlSerializer) {
    this.htmlSerializer = htmlSerializer;
  }

  public void setSortAttributes(boolean sortAttributes) {
    this.sortAttributes = sortAttributes;
  }

  public void setSortCssClasses(boolean sortCssClasses) {
    this.sortCssClasses = sortCssClasses;
  }

  private HtmlCleaner getHtmlCleaner() {
    HtmlCleaner htmlCleaner = new HtmlCleaner(getHtmlCleanerProperties());
    return htmlCleaner;
  }

  @Override
  protected String extractValueFromElement(Element element) {
    String jsoupHtml = super.extractValueFromElement(element);
    HtmlCleaner htmlCleaner = getHtmlCleaner();
    TagNode cleanedHtml = htmlCleaner.clean(jsoupHtml);
    if (isSortCssClasses()) {
      cleanedHtml.traverse(new CssClassSorter());
    }
    if (isSortAttributes()) {
      cleanedHtml.traverse(new HtmlAttributeSorter());
    }
    for (TagNodeVisitor visitor : getVisitors()) {
      cleanedHtml.traverse(visitor);
    }
    return getHtmlSerializer().getAsString(cleanedHtml);
  }

  protected List<TagNodeVisitor> getVisitors() {
    return visitors;
  }

}
