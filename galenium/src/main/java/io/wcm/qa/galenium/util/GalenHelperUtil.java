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
package io.wcm.qa.galenium.util;

import static io.wcm.qa.galenium.selectors.SelectorFactory.fromLocator;
import static io.wcm.qa.galenium.util.GaleniumContext.getTestDevice;
import static io.wcm.qa.galenium.webdriver.WebDriverManager.getDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.galenframework.browser.Browser;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.speclang2.pagespec.PageSpecReader;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.galenium.exceptions.GalenLayoutException;
import io.wcm.qa.galenium.selectors.Selector;

/**
 * Helper methods for dealing with Galen.
 */
public final class GalenHelperUtil {

  private static final Map<String, Object> EMPTY_JS_VARS = null;
  private static final Properties EMPTY_PROPERTIES = new Properties();
  private static final List<String> EMPTY_TAG_LIST = Collections.emptyList();
  private static final PageSpecReader PAGE_SPEC_READER = new PageSpecReader();

  private GalenHelperUtil() {
    // do not instantiate
  }

  public static Browser getBrowser() {
    return new SeleniumBrowser(GaleniumContext.getDriver());
  }

  /**
   * Turns test device into {@link SeleniumBrowser} as expected by Galen.
   * @param device to turn into browser
   * @return browser object for use with Galen
   */
  public static Browser getBrowser(TestDevice device) {
    return new SeleniumBrowser(getDriver(device));
  }

  /**
   * Get objects from {@link PageSpec}.
   * @param spec to extract objects from
   * @return selectors for all objects found in spec
   */
  public static Collection<Selector> getObjects(PageSpec spec) {
    Collection<Selector> objects = new ArrayList<>();
    for (Entry<String, Locator> entry : spec.getObjects().entrySet()) {
      objects.add(fromLocator(entry.getKey(), entry.getValue()));
    }
    return objects;
  }

  /**
   * Get named object from spec.
   * @param spec to extract object from
   * @param objectName name to use for extraction
   * @return selector representing named object from spec
   */
  public static Selector getObject(PageSpec spec, String objectName) {
    Locator objectLocator = spec.getObjectLocator(objectName);
    return fromLocator(objectName, objectLocator);
  }

  /**
   * Get tags from device as Galen {@link SectionFilter}.
   * @param device to get tags from
   * @return filter ready for use with Galen
   */
  public static SectionFilter getSectionFilter(TestDevice device) {
    return new SectionFilter(device.getTags(), EMPTY_TAG_LIST);
  }

  /**
   * Get tags from current device as Galen {@link SectionFilter}.
   * @return current tags as Galen filter
   */
  public static SectionFilter getTags() {
    List<String> tags = Collections.emptyList();
    if (getTestDevice() != null) {
      tags = getTestDevice().getTags();
    }
    return new SectionFilter(tags, Collections.emptyList());
  }

  /**
   * Read a spec from path. Basically a convenience mapping to
   * {@link PageSpecReader#read(String, com.galenframework.page.Page, SectionFilter, Properties, Map, Map)}.
   * @param browser to get current page from
   * @param specPath path to spec file
   * @param tags tag based filter
   * @return Galen page spec object
   */
  public static PageSpec readSpec(Browser browser, String specPath, SectionFilter tags) {
    try {
      return PAGE_SPEC_READER.read(specPath, browser.getPage(), tags, EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
    }
    catch (IOException ex) {
      throw new GalenLayoutException("IOException when reading spec", ex);
    }
  }

  /**
   * Convenience method to read a Galen spec using current threads context. Basically a convenience mapping to
   * {@link PageSpecReader#read(String, com.galenframework.page.Page, SectionFilter, Properties, Map, Map)}.
   * @param specPath path to spec file
   * @return Galen page spec object
   */
  public static PageSpec readSpec(String specPath) {
    return readSpec(getBrowser(), specPath, getTags());
  }

  /**
   * Read a spec from path. Basically a convenience mapping to
   * {@link PageSpecReader#read(String, com.galenframework.page.Page, SectionFilter, Properties, Map, Map)}.
   * @param device to use to get page
   * @param specPath path to spec file
   * @param tags tag based filter
   * @return Galen page spec object
   */
  public static PageSpec readSpec(TestDevice device, String specPath, SectionFilter tags) {
    return readSpec(getBrowser(device), specPath, tags);
  }

}
