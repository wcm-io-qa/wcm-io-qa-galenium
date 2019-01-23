/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.galenium.persistence.util;

import java.util.Properties;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;

/**
 * Handles storing and retrieving text samples from persistence layer. Samples are retrieved from a {@link Properties}
 * file located at {@link GaleniumConfiguration#getTextComparisonFile()} in
 * {@link GaleniumConfiguration#getTextComparisonInputDirectory()}.
 */
public final class TextSampleManager {

  private static final SampleManager TEXT_SAMPLE_MANAGER = SampleManager.getInstance("/expected.properties");

  private TextSampleManager() {
    // do not instantiate

  }

  /**
   * Adds a new text sample for optional persisting.
   * @param key this door is opened with the key to your imagination
   * @param value added
   */
  public static void addNewTextSample(String key, String value) {
    TEXT_SAMPLE_MANAGER.addNewTextSample(key, value);
  }

  /**
   * @param key to look up text
   * @return text expected for this key
   */
  public static String getExpectedText(String key) {
    return TEXT_SAMPLE_MANAGER.getExpectedText(key);
  }

  public static Properties getExpectedTexts() {
    return TEXT_SAMPLE_MANAGER.getExpectedTexts();
  }

  /**
   * @param prefix used to filter properties
   * @return new Properties containing only entries starting with the prefix
   */
  public static Properties getExpectedTextsForPrefix(String prefix) {
    return TEXT_SAMPLE_MANAGER.getExpectedTextsForPrefix(prefix);
  }

  /**
   * Write all stored new text samples to disk for easy replacement of existing expected values.
   */
  public static void persistNewTextSamples() {
    TEXT_SAMPLE_MANAGER.persistNewTextSamples();
  }

}
