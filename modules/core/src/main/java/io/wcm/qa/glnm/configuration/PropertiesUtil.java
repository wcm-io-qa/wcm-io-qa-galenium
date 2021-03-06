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
package io.wcm.qa.glnm.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.io.input.ReaderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper methods for dealing with Properties.
 *
 * @since 1.0.0
 */
public final class PropertiesUtil {

  private static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
  private static final Logger LOG = LoggerFactory.getLogger(PropertiesUtil.class);
  private static final String REGEX_WILDCARD = ".*";

  private PropertiesUtil() {
    // do not instantiate
  }

  /**
   * Filter properties by key part.
   *
   * @param properties to filter
   * @param searchString to filter by
   * @return only properties containing the search string in their key
   * @since 3.0.0
   */
  public static Properties getAllPropertiesContaining(Properties properties, String searchString) {
    return getFilteredProperties(properties, Pattern.compile(REGEX_WILDCARD + searchString + REGEX_WILDCARD));
  }

  /**
   * Filter properties by key prefix.
   *
   * @param properties to filter
   * @param prefix to filter by
   * @return only properties with a key starting with the prefix string
   * @since 3.0.0
   */
  public static Properties getAllPropertiesWithPrefix(Properties properties, String prefix) {
    return getFilteredProperties(properties, Pattern.compile(prefix + REGEX_WILDCARD));
  }

  /**
   * Filter properties by regular expression.
   *
   * @param properties to filter
   * @param filter regex pattern to filter by
   * @return only properties with a key matching the regular expression
   * @since 3.0.0
   */
  public static Properties getFilteredProperties(Properties properties, Pattern filter) {
    Properties filteredProperties = new Properties();
    for (Entry<Object, Object> property : properties.entrySet()) {
      Object keyObject = property.getKey();
      if (keyObject instanceof String) {
        String key = (String)keyObject;
        if (filter.matcher(key).matches()) {
          filteredProperties.put(key, property.getValue());
        }
      }
      else {
        LOG.debug("Key was not a String when filtering: '" + keyObject + "'");
      }
    }
    return filteredProperties;
  }

  /**
   * Load properties from file.
   *
   * @param properties to fill from file
   * @param filePath to properties file
   * @return properties from file
   * @since 3.0.0
   */
  public static Properties loadProperties(Properties properties, String filePath) {
    try {
      InputStream stream = getInputStream(filePath);
      if (stream != null) {
        properties.load(stream);
        if (LOG.isTraceEnabled()) {
          Enumeration<?> propertyNames = properties.propertyNames();
          while (propertyNames.hasMoreElements()) {
            Object key = propertyNames.nextElement();
            if (LOG.isTraceEnabled()) {
              LOG.trace("from properties file: " + key);
            }
          }
        }
      }
      else if (LOG.isDebugEnabled()) {
        LOG.debug("did not find properties at '" + filePath + "'");
      }
    }
    catch (IOException ex) {
      LOG.warn("Could not initialize properties: '" + filePath + "'", ex);
    }
    return properties;
  }

  /**
   * Load properties from file.
   *
   * @param filePath to properties file
   * @return properties from file
   * @since 3.0.0
   */
  public static Properties loadProperties(String filePath) {
    return loadProperties(new Properties(), filePath);
  }

  private static InputStream getInputStream(String filePath) throws FileNotFoundException {
    File propertiesFile = new File(filePath);
    InputStream stream;
    if (propertiesFile.exists() && propertiesFile.isFile()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("initializing properties from file '" + filePath + "'");
      }
      Reader reader = new FileReader(propertiesFile);
      stream = new ReaderInputStream(reader, CHARSET_UTF8);
    }
    else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("initializing properties from resource '" + filePath + "'");
      }
      stream = PropertiesUtil.class.getResourceAsStream(filePath);
    }
    if (stream != null) {
      if (LOG.isInfoEnabled()) {
        LOG.info("got input stream for " + filePath);
      }
    }
    else {
      if (LOG.isInfoEnabled()) {
        LOG.info("no input stream for " + filePath);
      }
    }
    return stream;
  }

}
