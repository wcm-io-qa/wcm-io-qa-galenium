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

import java.net.MalformedURLException;
import java.net.URL;

import io.wcm.qa.glnm.differences.base.DifferenceBase;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Uses current URL as difference.
 */
public abstract class UrlDifference extends DifferenceBase {

  private String url;

  /**
   * Uses current URL from driver.
   */
  public UrlDifference() {
    this(null);
  }

  /**
   * Uses URL from param.
   * @param url URL to use as difference
   */
  public UrlDifference(String url) {
    this.url = url;
  }

  @Override
  protected String getRawTag() {
    return getUrlAsString();
  }

  protected URL getUrl() {
    try {
      return new URL(getUrlAsString());
    }
    catch (MalformedURLException ex) {
      throw new GaleniumException("could not parse URL: '" + getUrlAsString() + "'", ex);
    }
  }

  protected String getUrlAsString() {
    if (url == null) {
      url = GaleniumContext.getDriver().getCurrentUrl();
    }
    return url;
  }


}
