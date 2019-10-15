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

/**
 * Uses the query parameters to URL as difference.
 *
 * @since 1.0.0
 */
public class UrlQueryDifference extends UrlDifference {

  /**
   * Uses current URL.
   *
   * @since 2.0.0
   */
  public UrlQueryDifference() {
    super();
  }

  /**
   * <p>Constructor for UrlQueryDifference.</p>
   *
   * @param url to extract parameters from
   * @since 2.0.0
   */
  public UrlQueryDifference(String url) {
    super(url);
  }

  @Override
  protected String getRawTag() {
    return getUrl().getQuery();
  }

}
