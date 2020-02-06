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

import static io.wcm.qa.glnm.interaction.Browser.getPerformanceLog;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.logging.LogEntry;

import io.wcm.qa.glnm.sampling.transform.JsonSampler;

/**
 * Utility methods around webdriver logs.
 *
 * @since 5.0.0
 */
public final class LogEntryUtil {

  private static final String MESSAGE_METHOD = "message.method";
  private static final String NETWORK_RESPONSE_RECEIVED = "Network.responseReceived";

  private LogEntryUtil() {
    // do not instantiate
  }

  /**
   * Get Message JSON as flat map.
   *
   * @param entry to parse
   * @return map containing all keys and values in one flat map
   * @since 5.0.0
   */
  public static Map<String, String> getFlatMessage(LogEntry entry) {
    String message = entry.getMessage();
    return JsonSampler.flatten(message);
  }

  /**
   * <p>getResponseEntries.</p>
   *
   * @return a {@link java.util.List} object.
   * @since 5.0.0
   */
  public static List<ResponseEntry> getResponseEntries() {
    return getPerformanceLog().stream()
        .filter(entry -> isResponseReceivedEntry(entry))
        .map(entry -> new ResponseEntry(entry))
        .collect(toList());
  }

  /**
   * Extracts value from message JSON.
   *
   * @param entry to extract from
   * @param key to fetch value for
   * @return value or null if not found
   * @since 5.0.0
   */
  public static String getValue(LogEntry entry, String key) {
    return getFlatMessage(entry).get(key);
  }

  private static boolean isResponseReceivedEntry(LogEntry entry) {
    return isResponseReceivedEntry(getFlatMessage(entry));
  }

  private static boolean isResponseReceivedEntry(Map<String, String> entry) {
    return StringUtils.equals(entry.get(MESSAGE_METHOD), NETWORK_RESPONSE_RECEIVED);
  }

}
