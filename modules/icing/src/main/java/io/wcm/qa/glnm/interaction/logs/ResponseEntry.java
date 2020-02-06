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
package io.wcm.qa.glnm.interaction.logs;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openqa.selenium.logging.LogEntry;

/**
 * <p>ResponseEntry class.</p>
 *
 * @since 5.0.0
 */
public class ResponseEntry {

  private static final int INVALID_STATUS = -1;
  private static final String MESSAGE_PARAMS_DOCUMENT_URL = "message.params.documentURL";
  private static final String MESSAGE_PARAMS_REQUEST_METHOD = "message.params.request.method";
  private static final String MESSAGE_PARAMS_REQUEST_URL = "message.params.request.url";
  private static final String MESSAGE_PARAMS_RESPONSE_STATUS = "message.params.response.status";

  private Map<String, String> message;
  private long timestamp;

  ResponseEntry(LogEntry entry) {
    setMessage(LogEntryUtil.getFlatMessage(entry));
    setTimestamp(entry.getTimestamp());
  }

  /**
   * <p>getDocumentUrl.</p>
   *
   * @return document URL from log message
   * @since 5.0.0
   */
  public String getDocumentUrl() {
    return get(MESSAGE_PARAMS_DOCUMENT_URL);
  }

  /**
   * <p>getRequestMethod.</p>
   *
   * @return HTTP request method from log message
   * @since 5.0.0
   */
  public String getRequestMethod() {
    return get(MESSAGE_PARAMS_REQUEST_METHOD);
  }

  /**
   * <p>getRequestUrl.</p>
   *
   * @return request URL from log message
   * @since 5.0.0
   */
  public String getRequestUrl() {
    return get(MESSAGE_PARAMS_REQUEST_URL);
  }

  /**
   * <p>getResponseStatus.</p>
   *
   * @return HTTP response status from log message
   * @since 5.0.0
   */
  public int getResponseStatus() {
    try {
      return Integer.parseInt(get(MESSAGE_PARAMS_RESPONSE_STATUS));
    }
    catch (NumberFormatException ex) {
      return INVALID_STATUS;
    }
  }

  /**
   * <p>Getter for the field <code>timestamp</code>.</p>
   *
   * @return a long.
   * @since 5.0.0
   */
  public long getTimestamp() {
    return timestamp;
  }

  private String get(String key) {
    return getMessage().get(key);
  }

  private Map<String, String> getMessage() {
    return message;
  }

  private void setMessage(Map<String, String> message) {
    this.message = message;
  }

  private void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("method", getRequestMethod())
        .append("status", getResponseStatus())
        .append("url", getRequestUrl())
        .build();
  }
}
