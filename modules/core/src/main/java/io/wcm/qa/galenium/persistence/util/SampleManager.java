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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.properties.SortedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.slf4j.Logger;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.configuration.PropertiesUtil;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;

/**
 * Handles storing and retrieving text samples from persistence layer. Samples are retrieved from a {@link Properties}
 * file located at {@link GaleniumConfiguration#getTextComparisonFile()} in
 * {@link GaleniumConfiguration#getTextComparisonInputDirectory()}.
 */
public final class SampleManager {

  private static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
  private static final String FILE_NAME_ROOT_DIR_EXPECTED_TEXTS = GaleniumConfiguration.getTextComparisonInputDirectory();
  private static final String FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS = GaleniumConfiguration.getTextComparisonOutputDirectory();
  private static final String PATH_SEPARATOR = "/";

  private static Map<String, SampleManager> instances = new HashMap<>();

  private SortedProperties expectedTexts = new SortedProperties();
  private SortedProperties sampledTexts = new SortedProperties();

  private File outputFile;

  /**
   * creates an object and registers it, so it will be called, when all tests have finished. If the filename already has
   * an instance it will be returned instead
   */
  public static synchronized SampleManager getInstance(String filename) {

    SampleManager sampleManager = instances.get(filename);
    if (sampleManager == null) {
      sampleManager = new SampleManager(filename);
      instances.put(filename, sampleManager);
    }

    return sampleManager;
  }

  /** @return all instances that were created of this class */
  public static synchronized Map<String, SampleManager> getAllInstances() {
    return instances;
  }

  private SampleManager(String filename) {
    StringBuilder expectedTextsFilePath = new StringBuilder();
    expectedTextsFilePath.append(FILE_NAME_ROOT_DIR_EXPECTED_TEXTS);
    if (!(FILE_NAME_ROOT_DIR_EXPECTED_TEXTS.endsWith(PATH_SEPARATOR) || filename.startsWith(PATH_SEPARATOR))) {
      expectedTextsFilePath.append(PATH_SEPARATOR);
    }
    expectedTextsFilePath.append(filename);
    PropertiesUtil.loadProperties(expectedTexts, expectedTextsFilePath.toString());
    outputFile = new File(FILE_NAME_ROOT_DIR_SAVE_SAMPLED_TEXTS, filename);
  }

  /**
   * Adds a new text sample for optional persisting.
   * @param key this door is opened with the key to your imagination
   * @param value added
   */
  public void addNewTextSample(String key, String value) {
    getLogger().trace("adding new text sample: " + key + "->'" + value + "'");
    sampledTexts.setProperty(key, value);
  }

  /**
   * @param key to look up text
   * @return text expected for this key
   */
  public String getExpectedText(String key) {
    return expectedTexts.getProperty(key);
  }

  public Properties getExpectedTexts() {
    return expectedTexts;
  }

  /**
   * @param prefix used to filter properties
   * @return new Properties containing only entries starting with the prefix
   */
  public Properties getExpectedTextsForPrefix(String prefix) {
    return PropertiesUtil.getAllPropertiesWithPrefix(expectedTexts, prefix);
  }


  /**
   * Write all expected values. The newly sampled ones and the old ones
   */
  public void persistAllExpected() {
    if (sampledTexts.isEmpty()) {
      getLogger().debug("no text samples to persist.");
    }
    else {

      try {
        List<String> allExpected = new ArrayList<String>();
        allExpected.addAll(convertPropertiesToEncodedLines(expectedTexts.entrySet()));
        allExpected.addAll(convertPropertiesToEncodedLines(sampledTexts.entrySet()));
        Collections.sort(allExpected);
        FileUtils.writeLines(outputFile, allExpected, "\n");

        getLogger().debug("saved sample texts to '" + outputFile + "'");
      }
      catch (IOException ex) {
        getLogger().error("Could not save sample texts to '" + outputFile + "'");
      }

    }
  }

  static List<String> convertPropertiesToEncodedLines(Set<Entry<Object, Object>> entrySet) {
    List<String> list = new ArrayList<String>();
    for (Entry<Object, Object> entry : entrySet) {
      String convertedValue = PropertyFileUtil.saveConvert((String)entry.getValue(), false, true);
      list.add(entry.getKey() + "=" + convertedValue);
    }
    return list;
  }

  /**
   * Write all stored new text samples to disk for easy replacement of existing expected values.
   */
  public void persistNewTextSamples() {
    if (sampledTexts.isEmpty()) {
      getLogger().debug("no text samples to persist.");
    }
    else {
      writeNewTextSamples();
      reencodeToUnixLineEndings();
    }
  }


  private void writeNewTextSamples() {
    WriterOutputStream writerOutputStream = null;
    try {
      getLogger().debug("Persisting " + sampledTexts.size() + " text samples.");
      writerOutputStream = getOutputStream();
      expectedTexts.store(writerOutputStream, "Expected texts");
      sampledTexts.store(writerOutputStream, "Sampled texts");
    }
    catch (IOException ex) {
      getLogger().error("Could not save sample texts to '" + outputFile + "'");
    }
    finally {
      if (writerOutputStream != null) {
        try {
          writerOutputStream.close();
        }
        catch (IOException ex) {
          getLogger().warn("error when closing file output stream: '" + outputFile + "'");
        }
      }
    }
  }

  /** to reduce changes in the file we force the lineendings to be in unix format */
  private void reencodeToUnixLineEndings() {

    File temp = null;
    BufferedReader reader = null;
    BufferedWriter writer = null;

    try {
      temp = new File(outputFile.getAbsolutePath() + ".unixLines");
      temp.createNewFile();

      reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(outputFile))));
      writer = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(new FileOutputStream(temp))));

      String line;
      while ((line = reader.readLine()) != null) {
        writer.write(line);
        writer.write("\n");
      }
      reader.close();
      writer.close();

      Files.delete(outputFile.toPath());
      Files.move(temp.toPath(), outputFile.toPath());
      getLogger().debug("successfully reencoded to unix lineendings: " + outputFile);

    }
    catch (IOException e) {
      getLogger().warn("could not reencode: '" + outputFile + "'", e);
    }
    finally {
      FileUtils.deleteQuietly(temp);
      IOUtils.closeQuietly(reader);
      IOUtils.closeQuietly(writer);
    }

  }

  private Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  private WriterOutputStream getOutputStream() throws IOException {
    WriterOutputStream writerOutputStream;
    outputFile.getParentFile().mkdirs();
    FileWriter fileWriter = new FileWriter(outputFile, true);
    writerOutputStream = new WriterOutputStream(fileWriter, CHARSET_UTF8);
    return writerOutputStream;
  }


}
