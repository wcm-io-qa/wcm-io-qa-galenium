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

import io.wcm.qa.glnm.aem.AemComponentUtil;
import io.wcm.qa.glnm.sampling.jsoup.JsoupRawStringSampler;

/**
 * Sampler to fetch HTML from directly from an AEM component.
 */
public class AemComponentHtmlSampler extends JsoupRawStringSampler {

  /**
   * @param url to fetch HTML from
   */
  public AemComponentHtmlSampler(String url) {
    super(url);
    setBodyOnly(true);
  }

  /**
   * @param contentPath path to page
   * @param componentName name of component to check
   */
  public AemComponentHtmlSampler(String contentPath, String componentName) {
    this(AemComponentUtil.urlBuilder(contentPath, componentName).build().toString());
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
