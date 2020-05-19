/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.jsoup.base.JsoupBasedSampler;

/**
 * Samples response code for calling URL via Jsoup.
 *
 * @since 5.0.0
 */
public class JsoupResponseCodeSampler extends JsoupBasedSampler<Integer> {

  private static final Logger LOG = LoggerFactory.getLogger(JsoupResponseCodeSampler.class);

  /**
   * <p>Constructor for JsoupResponseCodeSampler.</p>
   *
   * @param url a {@link java.lang.String} object.
   */
  public JsoupResponseCodeSampler(String url) {
    super(url);
  }

  @Override
  protected Connection getJsoupConnection() {
    Connection jsoupConnection = super.getJsoupConnection();
    jsoupConnection.ignoreHttpErrors(true);
    jsoupConnection.ignoreContentType(true);
    return jsoupConnection;
  }

  @Override
  protected Integer freshSample() {
    Response execution;
    try {
      execution = getJsoupConnection().execute();
      return execution.statusCode();
    }
    catch (IOException ex) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("when sampling: " + getUrl(), ex);
      }
      return getNullValue();
    }
  }

}
