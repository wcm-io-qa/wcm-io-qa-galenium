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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.MARKER_ERROR;
import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.input.ReaderInputStream;

public class ConfigurationUtil {

  private static final Charset CHARSET_UTF8 = Charset.forName("utf-8");

  private ConfigurationUtil() {
    // do not instantiate
  }

  public static void addProperty(Object name, Object value) {
    if (name == null || value == null) {
      getLogger().info("skipping entry: " + name + "->" + value);
    }
    else {
      getLogger().info("adding entry: " + name + "->" + value);
      System.setProperty(name.toString(), value.toString());
    }
  }

  public static void addToSystemProperties(Map<String, String> newProperties) {
    Set<Entry<String, String>> entrySet = newProperties.entrySet();
    for (Entry<String, String> entry : entrySet) {
      if (entry != null) {
        String name = entry.getKey();
        String value = entry.getValue();
        addProperty(name, value);
      }
      else {
        getLogger().warn("entry for system property was null.");
      }
    }
  }

  public static void addToSystemProperties(Properties newProperties) {
    for (String name : newProperties.stringPropertyNames()) {
      addProperty(name, newProperties.getProperty(name));
    }
  }

  public static Properties loadProperties(Properties properties, String filePath) {
    try {
      File propertiesFile = new File(filePath);
      if (propertiesFile.exists() && propertiesFile.isFile()) {
        getLogger().debug("initializing properties from " + filePath);
        Reader reader = new FileReader(propertiesFile);
        properties.load(new ReaderInputStream(reader, CHARSET_UTF8));
        if (getLogger().isDebugEnabled()) {
          Enumeration<?> propertyNames = properties.propertyNames();
          while (propertyNames.hasMoreElements()) {
            Object key = propertyNames.nextElement();
            getLogger().debug("from properties file: " + key);
          }
        }
      }
      else {
        getLogger().debug("did not find properties at '" + filePath + "'");
      }
    }
    catch (IOException ex) {
      getLogger().debug(MARKER_ERROR, "Could not initialize properties: '" + filePath + "'", ex);
    }
    return properties;
  }

  public static Properties loadProperties(String filePath) {
    return loadProperties(new Properties(), filePath);
  }
}