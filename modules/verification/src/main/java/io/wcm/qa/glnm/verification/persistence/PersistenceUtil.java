/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.verification.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.exceptions.GaleniumException;

final class PersistenceUtil {

  private static final Logger LOG = LoggerFactory.getLogger(PersistenceUtil.class);

  private static final String POSTFIX_PROPERTIES = "properties";
  private static final String POSTFIX_TXT = ".txt";

  private PersistenceUtil() {
    // do not instantiate
  }

  private static File getOutputFile(Class clazz) {
    String resourceName = getPropertiesResourceName(clazz);
    File outputFile = getOutputFile(clazz, resourceName);
    return outputFile;
  }

  private static File getOutputFile(Class clazz, String resourceName) {
    String relativePath = ClassPathUtils.toFullyQualifiedPath(clazz, resourceName);
    String textOutputDirectory = GaleniumConfiguration.getTextComparisonOutputDirectory();
    File outputFile = FileUtils.getFile(textOutputDirectory, relativePath);
    return outputFile;
  }

  private static InputStream getPropertiesInputStream(Class clazz) {
    return clazz.getResourceAsStream(getPropertiesResourceName(clazz));
  }

  private static String getPropertiesResourceName(Class clazz) {
    String resourceName = clazz.getSimpleName() + "." + POSTFIX_PROPERTIES;
    if (LOG.isDebugEnabled()) {
      LOG.debug("properties resource name: " + resourceName);
    }
    return resourceName;
  }

  private static InputStream getTextFileInputStream(Class clazz, Differences differences) {
    return clazz.getResourceAsStream(getTextFileResourceName(differences));
  }

  static List<String> getLinesFromTextFile(Class clazz, Differences differences) {
    InputStream in = getTextFileInputStream(clazz, differences);
    if (in == null) {
      return Collections.emptyList();
    }
    try {
      return IOUtils.readLines(in, StandardCharsets.UTF_8);
    }
    catch (IOException ex) {
      throw new GaleniumException("Could not read sample for: " + clazz + " with " + differences, ex);
    }
  }

  static Properties getPropertiesFor(Class clazz) {

    try {
      Properties properties = new Properties();
      InputStream in = getPropertiesInputStream(clazz);
      properties.load(in);
      return properties;
    }
    catch (NullPointerException | IOException ex) {
      // notify, but still return empty properties
      LOG.info("Could not load properties for: " + clazz, ex);
      return new Properties();
    }
  }

  static String getTextFileResourceName(Differences differences) {
    String resourceName = differences.asFilePath() + POSTFIX_TXT;
    LOG.debug("file resource name: " + resourceName);
    return resourceName;
  }

  static void writeLinesToTextFile(Class clazz, Differences differences, List<String> lines) {
    String resourceName = getTextFileResourceName(differences);
    File outputFile = getOutputFile(clazz, resourceName);
    try {
      LOG.info("writing {} lines to '{}'", lines.size(), outputFile.getPath());
      FileUtils.forceMkdirParent(outputFile);
      FileUtils.writeLines(outputFile, lines);
    }
    catch (IOException ex) {
      LOG.error("could not persist samples.", ex);
    }
  }

  static void writePropertiesFor(Properties properties, Class clazz, boolean append) {
    writePropertiesFor(properties, null, clazz, append);
  }

  static void writePropertiesFor(Properties properties, String comments, Class clazz, boolean append) {
    try {
      File outputFile = getOutputFile(clazz);
      LOG.info("writing {} properties to '{}'", properties.size(), outputFile.getPath());
      FileUtils.forceMkdirParent(outputFile);
      Writer writer = new FileWriter(outputFile, append);
      properties.store(writer, comments);
    }
    catch (IOException ex) {
      LOG.error("could not persist samples.", ex);
    }
  }

}
