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
package io.wcm.qa.galenium.sampling.text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.configuration.PropertiesUtil;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Handles storing and retrieving text samples from persistence layer. Samples are retrieved from a {@link Properties}
 * file located at {@link GaleniumConfiguration#getTextComparisonFile()} in
 * {@link GaleniumConfiguration#getTextComparisonDirectory()}.
 */
public final class TextSampleManager {

  private static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
  private static final Properties EXPECTED_TEXTS = new Properties();
  private static final String FILE_NAME_EXPECTED_TEXTS = GaleniumConfiguration.getTextComparisonFile();
  private static final String FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS = GaleniumConfiguration.getTextComparisonDirectory();
  private static final Properties SAMPLED_TEXTS = new Properties();

  static {
    PropertiesUtil.loadProperties(EXPECTED_TEXTS, FILE_NAME_EXPECTED_TEXTS);
  }

  private TextSampleManager() {
    // do not instantiate
  }

  /**
   * Adds a new text sample for optional persisting.
   * @param key this door is opened with the key to your imagination
   * @param value added
   */
  public static void addNewTextSample(String key, String value) {
    getLogger().trace("adding new text sample: " + key + "->'" + value + "'");
    SAMPLED_TEXTS.setProperty(key, value);
  }

  /**
   * @param key to look up text
   * @return text expected for this key
   */
  public static String getExpectedText(String key) {
    return EXPECTED_TEXTS.getProperty(key);
  }

  public static Properties getExpectedTexts() {
    return EXPECTED_TEXTS;
  }

  /**
   * Write all stored new text samples to disk for easy replacement of existing expected values.
   */
  public static void persistNewTextSamples() {
    if (SAMPLED_TEXTS.isEmpty()) {
      getLogger().debug("no text samples to persist.");
    }
    else {
      WriterOutputStream writerOutputStream = null;
      try {
        getLogger().debug("Persisting " + SAMPLED_TEXTS.size() + " text samples.");
        String difference = StringUtils.difference(GaleniumConfiguration.getGalenSpecPath(), FILE_NAME_EXPECTED_TEXTS);
        File file = new File(FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS, difference);
        file.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(file, true);
        writerOutputStream = new WriterOutputStream(fileWriter, CHARSET_UTF8);

        EXPECTED_TEXTS.store(writerOutputStream, "Expected texts");
        SAMPLED_TEXTS.store(writerOutputStream, "Sampled texts");
      }
      catch (IOException ex) {
        getLogger().error("Could not save sample texts.");
      }
      finally {
        if (writerOutputStream != null) {
          try {
            writerOutputStream.close();
          }
          catch (IOException ex) {
            getLogger().warn("error when closing file output stream.");
          }
        }
      }
    }
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }
}
