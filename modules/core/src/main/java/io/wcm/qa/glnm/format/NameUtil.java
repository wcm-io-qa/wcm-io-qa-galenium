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
package io.wcm.qa.glnm.format;

import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.Marker;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Utility methods to get solid names for code, tests and files.
 */
public final class NameUtil {

  private static final String CLEANING_REGEX = "\\W+";
  private static final Marker MARKER = GaleniumReportUtil.getMarker("galenium.naming");

  private NameUtil() {
    // do not instantiate
  }

  static String getConstantName(String input) {
    String kebapInput = anyToKebap(input);
    return kebapInput;
  }

  private static String anyToKebap(String input) {
    throw new GaleniumException("name formatting not implemented yet.");
  }

  /**
   * Get string input sanitized for use in names.
   * @param input to clean and shorten
   * @param maxLength maximal length of returned clean version
   * @return cleaned version respecting the maximal length
   */
  public static String getSanitized(String input, int maxLength) {
    String cleaned = cleanCharacters(input);
    if (StringUtils.length(cleaned) <= maxLength) {
      return cleaned;
    }
    String abbreviated = StringUtils.abbreviateMiddle(cleaned, getFittingMd5String(input, maxLength), maxLength);
    getLogger().trace("abbreviated String: " + abbreviated);
    if (abbreviated.length() > maxLength) {
      throw new GaleniumException("could not abbreviate to " + maxLength + ": '" + abbreviated + "'");
    }
    return abbreviated;
  }

  private static String getFittingMd5String(String input, int maxTotalStringLength) {
    String md5Hash = Md5Util.getMd5AsAscii(getUtf8Bytes(input));
    getLogger().trace("abbreviating to " + maxTotalStringLength + " with: " + md5Hash);
    int maximumMd5Length = maxTotalStringLength / 3;
    if (md5Hash.length() > maximumMd5Length) {
      return md5Hash.substring(0, maximumMd5Length);
    }
    return md5Hash;
  }

  private static String cleanCharacters(String dirty) {
    if (StringUtils.isNotBlank(dirty)) {
      String clean = dirty.replaceAll(CLEANING_REGEX, "_");
      getLogger().trace("cleaned string: " + clean);
      return clean;
    }
    return StringUtils.stripToEmpty(dirty);
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getMarkedLogger(MARKER);
  }

  private static byte[] getUtf8Bytes(String input) {
    return input.getBytes(StandardCharsets.UTF_8);
  }

}
