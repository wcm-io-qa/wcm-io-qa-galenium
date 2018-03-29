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

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class ConfigurationUtil {

  private ConfigurationUtil() {
    // do not instantiate
  }

  public static void addToSystemProperties(Properties newProperties) {
    for (String name : newProperties.stringPropertyNames()) {
      addProperty(name, newProperties.getProperty(name));
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

  public static void addProperty(Object name, Object value) {
    if (name == null || value == null) {
      getLogger().info("skipping entry: " + name + "->" + value);
    }
    else {
      getLogger().info("adding entry: " + name + "->" + value);
      System.setProperty(name.toString(), value.toString());
    }
  }
}
