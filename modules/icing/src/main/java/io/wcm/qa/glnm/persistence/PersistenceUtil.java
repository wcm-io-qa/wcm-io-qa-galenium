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
package io.wcm.qa.glnm.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
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

  private static String getPropertiesResourceName(Class clazz) {
    String resourceName = clazz.getSimpleName() + "." + POSTFIX_PROPERTIES;
    if (LOG.isDebugEnabled()) {
      LOG.debug("properties resource name: " + resourceName);
    }
    return resourceName;
  }

  private static Reader getReaderForProperties(Class clazz) {
    InputStream in = clazz.getResourceAsStream(getPropertiesResourceName(clazz));
    if (in == null) {
      return null;
    }
    return new InputStreamReader(in);
  }

  @SuppressWarnings("PMD.AvoidCatchingNPE")
  static PropertiesConfiguration getPropertiesFor(Class clazz) {

    try {
      PropertiesConfiguration configuration = new Configurations().propertiesBuilder().getConfiguration();
      Reader readerForProperties = getReaderForProperties(clazz);
      if (readerForProperties != null) {
        configuration.read(readerForProperties);
      }
      return configuration;
    }
    catch (ConfigurationException | IOException ex) {
      // notify, but still return empty properties
      if (LOG.isInfoEnabled()) {
        LOG.info("Could not load properties for: " + clazz, ex);
      }
      return new PropertiesConfiguration();
    }
  }

  static void writeSamplesForClass(PropertiesConfiguration samples, Class clazz) {
    Writer writer = null;
    try {
      File outputFile = getOutputFile(clazz);
      if (LOG.isInfoEnabled()) {
        LOG.info("writing {} properties to '{}'", samples.size(), outputFile.getPath());
      }
      FileUtils.forceMkdirParent(outputFile);
      writer = new FileWriter(outputFile, false);
      // TODO: refactor to use FileHandler
      samples.write(writer);
    }
    catch (IOException | ConfigurationException ex) {
      LOG.error("could not persist samples.", ex);
    }
    finally {
      if (writer != null) {
        try {
          writer.close();
        }
        catch (IOException ex) {
          LOG.error("when closing writer while persisting samples.", ex);
        }
      }
    }
  }

}
