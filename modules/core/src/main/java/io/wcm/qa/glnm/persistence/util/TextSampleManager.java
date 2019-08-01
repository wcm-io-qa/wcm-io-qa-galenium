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
package io.wcm.qa.glnm.persistence.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections4.properties.SortedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.configuration.PropertiesUtil;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Handles storing and retrieving text samples from persistence layer. Samples are retrieved from a {@link Properties}
 * file located at {@link GaleniumConfiguration#getTextComparisonFile()} in
 * {@link GaleniumConfiguration#getTextComparisonInputDirectory()}.
 */
public final class TextSampleManager {

  private static final Charset CHARSET_UTF8 = StandardCharsets.UTF_8;
  private static final SortedProperties EXPECTED_TEXTS = new SortedProperties();
  private static final String FILE_NAME_EXPECTED_TEXTS = GaleniumConfiguration.getTextComparisonFile();
  private static final String FILE_NAME_ROOT_DIR_EXPECTED_TEXTS = GaleniumConfiguration.getTextComparisonInputDirectory();
  private static final String FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS = GaleniumConfiguration.getTextComparisonOutputDirectory();
  private static final File OUTPUT_FILE = new File(FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS, FILE_NAME_EXPECTED_TEXTS);
  private static final String PATH_SEPARATOR = "/";
  private static final SortedProperties SAMPLED_TEXTS = new SortedProperties();

  static {
    StringBuilder expectedTextsFilePath = new StringBuilder();
    expectedTextsFilePath.append(FILE_NAME_ROOT_DIR_EXPECTED_TEXTS);
    if (!(FILE_NAME_ROOT_DIR_EXPECTED_TEXTS.endsWith(PATH_SEPARATOR) || FILE_NAME_EXPECTED_TEXTS.startsWith(PATH_SEPARATOR))) {
      expectedTextsFilePath.append(PATH_SEPARATOR);
    }
    expectedTextsFilePath.append(FILE_NAME_EXPECTED_TEXTS);
    PropertiesUtil.loadProperties(EXPECTED_TEXTS, expectedTextsFilePath.toString());
  }

  private TextSampleManager() {
    // do not instantiate
  }

  /**
   * Writes text samples directly to file.
   * @param key used to construct file name
   * @param lines sample data to persist
   */
  public static void addNewMultiLineSample(String key, List<String> lines) {
    String sampleFileName = FilenameUtils.concat(FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS, key + ".txt");
    File sampleFile = new File(sampleFileName);
    try {
      if (sampleFile.exists()) {
        sampleFile.delete();
      }
      for (String line : lines) {
        String data = line;
        FileUtils.write(sampleFile, data + "\n", CHARSET_UTF8, true);
      }
    }
    catch (IOException ex) {
      getLogger().error("could not persist sample for key '" + key + "'", ex);
    }
  }

  /**
   * Adds a new text sample for optional persisting.
   * @param key this door is opened with the key to your imagination
   * @param value added
   */
  public static void addNewTextSample(String key, String value) {
    String escapeHtml = GaleniumReportUtil.escapeHtml(value);

    getLogger().trace("adding new text sample: " + key + "->'" + StringUtils.abbreviateMiddle(escapeHtml, "...", 200) + "'");
    SAMPLED_TEXTS.setProperty(key, value);
  }

  /**
   * @param key to look up text
   * @return text expected for this key
   */
  public static List<String> getExpectedLines(String key) {
    try {
      String sampleFileName = FilenameUtils.concat(FILE_NAME_ROOT_DIR_EXPECTED_TEXTS, key + ".txt");
      List<String> readLines = FileUtils.readLines(new File(sampleFileName), CHARSET_UTF8);
      List<String> decodedLines = new ArrayList<String>();
      for (String line : readLines) {
        decodedLines.add(line);
      }

      return decodedLines;
    }
    catch (IOException ex) {
      getLogger().info("could not read sample for key '" + key + "'", ex);
    }
    return Collections.emptyList();
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
   * @param prefix used to filter properties
   * @return new Properties containing only entries starting with the prefix
   */
  public static Properties getExpectedTextsForPrefix(String prefix) {
    return PropertiesUtil.getAllPropertiesWithPrefix(EXPECTED_TEXTS, prefix);
  }

  /**
   * Write all stored new text samples to disk for easy replacement of existing expected values.
   */
  public static void persistNewTextSamples() {
    if (SAMPLED_TEXTS.isEmpty()) {
      getLogger().debug("no text samples to persist.");
    }
    else {
      writeNewTextSamples();
      reencodeToUnixLineEndings();
    }
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  private static WriterOutputStream getOutputStream() throws IOException {
    WriterOutputStream writerOutputStream;
    OUTPUT_FILE.getParentFile().mkdirs();
    FileWriter fileWriter = new FileWriter(OUTPUT_FILE, true);
    writerOutputStream = new WriterOutputStream(fileWriter, CHARSET_UTF8);
    return writerOutputStream;
  }

  /** to reduce changes in the file we force the lineendings to be in unix format */
  private static void reencodeToUnixLineEndings() {

    File temp = null;
    BufferedReader reader = null;
    BufferedWriter writer = null;

    try {
      temp = new File(OUTPUT_FILE.getAbsolutePath() + ".unixLines");
      temp.createNewFile();

      reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(OUTPUT_FILE))));
      writer = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(temp))));

      String line;
      while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.write("\n");
      }
      reader.close();
      writer.close();

      Files.delete(OUTPUT_FILE.toPath());
      Files.move(temp.toPath(), OUTPUT_FILE.toPath());
      getLogger().debug("successfully reencoded to unix lineendings: " + OUTPUT_FILE);

    }
    catch (IOException e) {
      getLogger().warn("could not reencode: '" + OUTPUT_FILE + "'", e);
    }
    finally {
      FileUtils.deleteQuietly(temp);
      IOUtils.closeQuietly(reader);
      IOUtils.closeQuietly(writer);
    }

  }

  private static void writeNewTextSamples() {
    WriterOutputStream writerOutputStream = null;
    try {
      getLogger().debug("Persisting " + SAMPLED_TEXTS.size() + " text samples.");
      writerOutputStream = getOutputStream();
      EXPECTED_TEXTS.store(writerOutputStream, "Expected texts");
      SAMPLED_TEXTS.store(writerOutputStream, "Sampled texts");
    }
    catch (IOException ex) {
      getLogger().error("Could not save sample texts to '" + OUTPUT_FILE + "'");
    }
    finally {
      if (writerOutputStream != null) {
        try {
          writerOutputStream.close();
        }
        catch (IOException ex) {
          getLogger().warn("error when closing file output stream: '" + OUTPUT_FILE + "'");
        }
      }
    }
  }
}
