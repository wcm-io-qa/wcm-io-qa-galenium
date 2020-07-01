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
package io.wcm.qa.glnm.context;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Keeps important data for each thread. Simplifies integration without need for rigid inheritance hierarchies. Takes a
 * lot of hassle out of multi-threaded runs.
 *
 * @since 1.0.0
 */
public class GaleniumContext {

  private static final Logger LOG = LoggerFactory.getLogger(GaleniumContext.class);
  private static final ThreadLocal<GaleniumContext> THREAD_LOCAL_CONTEXT = new ThreadLocal<GaleniumContext>() {
    @Override
    protected GaleniumContext initialValue() {
      return new GaleniumContext();
    }
  };

  private final Map<String, Object> additionalMappings = new HashMap<String, Object>();
  private WebDriver driver;

  /**
   * WebDriver to use for all things Galenium. This includes interaction with Galen and Selenium. Usually the WebDriver
   * handling can be left to Galenium. Listeners should initialize the driver.
   *
   * @param driver WebDriver to use for everything
   * @since 3.0.0
   */
  public void setDriver(WebDriver driver) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("setting driver: " + driver);
    }
    this.driver = driver;
  }

  /**
   * Get a custom object for the current thread.
   *
   * @param key used for retrieving custom object
   * @return the value to which the specified key is mapped, or null if this thread context contains no mapping for the
   *         key
   * @since 3.0.0
   */
  public static Object get(String key) {
    return THREAD_LOCAL_CONTEXT.get().additionalMappings.get(key);
  }

  /**
   * <p>
   * getContext.
   * </p>
   *
   * @return {@link io.wcm.qa.glnm.context.GaleniumContext} object for this thread
   * @since 4.0.0
   */
  public static GaleniumContext getContext() {
    return THREAD_LOCAL_CONTEXT.get();
  }

  /**
   * <p>Getter for the field <code>driver</code>.</p>
   *
   * @return driver to use with Selenium and Galen
   * @since 3.0.0
   */
  public static WebDriver getDriver() {
    return THREAD_LOCAL_CONTEXT.get().driver;
  }

  /**
   * Store any object in the current threads context.
   *
   * @param key used to store and retrieve object
   * @param customObject custom object
   * @return the previous value associated with key in this thread, or null if there was no mapping for key
   * @since 3.0.0
   */
  public static Object put(String key, Object customObject) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("setting additional context for '" + key + "'");
    }
    return THREAD_LOCAL_CONTEXT.get().additionalMappings.put(key, customObject);
  }

}
