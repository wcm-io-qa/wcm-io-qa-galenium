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
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ClassPathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;

final class PersistenceUtil {

  private static final Logger LOG = LoggerFactory.getLogger(PersistenceUtil.class);

  private static final String POSTFIX_PROPERTIES = "properties";

  private PersistenceUtil() {
    // do not instantiate
  }

  private static InputStream getInputStream(Class clazz) {
    return clazz.getResourceAsStream(getPropertiesResourceName(clazz));
  }

  private static File getOutputFile(Properties properties, Class clazz) {
    String resourceName = getPropertiesResourceName(clazz);
    String relativePath = ClassPathUtils.toFullyQualifiedPath(clazz, resourceName);
    String textOutputDirectory = GaleniumConfiguration.getTextComparisonOutputDirectory();
    File outputFile = FileUtils.getFile(textOutputDirectory, relativePath);
    LOG.info("writing {} properties to '{}'", properties.size(), outputFile.getPath());
    return outputFile;
  }

  private static String getPropertiesResourceName(Class clazz) {
    return clazz.getSimpleName() + "." + POSTFIX_PROPERTIES;
  }

  static Properties getPropertiesFor(Class clazz) {

    try {
      Properties properties = new Properties();
      InputStream in = getInputStream(clazz);
      properties.load(in);
      return properties;
    }
    catch (NullPointerException | IOException ex) {
      // notify, but still return empty properties
      LOG.info("Could not load properties for: " + clazz, ex);
      return new Properties();
    }
  }

  static void writePropertiesFor(Properties properties, String comments, Class clazz) {
    try {
      File outputFile = getOutputFile(properties, clazz);
      FileUtils.forceMkdirParent(outputFile);
      Writer writer = new FileWriter(outputFile);
      properties.store(writer, comments);
    }
    catch (IOException ex) {
      LOG.error("could not persist samples.", ex);
    }
  }

  static void writePropertiesFor(Properties properties, Class clazz) {
    writePropertiesFor(properties, null, clazz);
  }

}
