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

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * Convenience methods to set system properties.
 */
public final class ConfigurationUtil {

  private ConfigurationUtil() {
    // do not instantiate
  }

  /**
   * Adds a system property.
   * @param name of property
   * @param value of property
   */
  public static void addProperty(Object name, Object value) {
    if (name == null || value == null) {
      getLogger().info("skipping entry: " + name + "->" + value);
    }
    else {
      getLogger().info("adding entry: " + name + "->" + value);
      System.setProperty(name.toString(), value.toString());
    }
  }

  /**
   * Add properties from map to system properties.
   * @param newProperties to add
   */
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

  /**
   * Add system properties.
   * @param newProperties to add
   */
  public static void addToSystemProperties(Properties newProperties) {
    for (String name : newProperties.stringPropertyNames()) {
      addProperty(name, newProperties.getProperty(name));
    }
  }

}
