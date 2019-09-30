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
package io.wcm.qa.glnm.differences.difference.sut;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;

/**
 * Uses path and query part of URL as difference.
 */
public class RelativeUrlWithQueryDifference extends UrlDifference {

  /**
   * Uses current URL.
   */
  public RelativeUrlWithQueryDifference() {
    super();
  }

  /**
   * Uses URL build from parameter string.
   * @param url to extract path and query from
   */
  public RelativeUrlWithQueryDifference(String url) {
    super(url);
  }

  @Override
  protected String getRawTag() {
    URL url = getUrl();
    String relativePath = url.getPath();
    if (StringUtils.isNotBlank(url.getRef())) {
      relativePath += url.getRef();
    }
    if (StringUtils.isNotBlank(url.getQuery())) {
      relativePath += url.getQuery();
    }
    return relativePath;
  }

}
