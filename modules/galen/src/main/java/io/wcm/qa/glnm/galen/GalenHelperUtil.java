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
package io.wcm.qa.glnm.galen;

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;
import static io.wcm.qa.glnm.selectors.base.SelectorFactory.fromLocator;
import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;
import static io.wcm.qa.glnm.webdriver.WebDriverManagement.getDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;

import com.galenframework.browser.Browser;
import com.galenframework.browser.SeleniumBrowser;
import com.galenframework.page.Page;
import com.galenframework.speclang2.pagespec.PageSpecReader;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.utils.GalenUtils;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GalenLayoutException;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.selectors.SelectorFromLocator;
import io.wcm.qa.glnm.selectors.base.NestedSelector;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Helper methods for dealing with Galen.
 */
public final class GalenHelperUtil {

  private static final List<String> EMPTY_TAG_LIST = Collections.emptyList();
  private static final Map<String, Object> EMPTY_JS_VARS = null;
  private static final Properties EMPTY_PROPERTIES = new Properties();
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
   * @param filtersToCombine list of SectionFilters
   * @return section filter containing all included and excluded tags of the passed filters
   */
  public static SectionFilter getCombinedSectionFilter(SectionFilter... filtersToCombine) {
    Bag<String> excludedTagBag = new HashBag<String>();
    Bag<String> includedTagBag = new HashBag<String>();
    for (SectionFilter sectionFilter : filtersToCombine) {
      excludedTagBag.addAll(sectionFilter.getExcludedTags());
      includedTagBag.addAll(sectionFilter.getIncludedTags());
    }
    List<String> excludedTagList = getBagAsUniqueList(excludedTagBag);
    List<String> includedTagList = getBagAsUniqueList(includedTagBag);
    List<String> intersection = ListUtils.intersection(includedTagList, excludedTagList);
    if (intersection.isEmpty()) {
      return new SectionFilter(includedTagList, excludedTagList);
    }
    String intersectionAsString = StringUtils.join(intersection, ", ");
    throw new GaleniumException("tags in included and excluded collide: [" + intersectionAsString + "]");
  }

  /**
   * Turn Galen syntax size string into Selenium {@link Dimension}.
   * @param size to parse
   * @return Selenium representation of size
   */
  public static Dimension getDimension(String size) {
    java.awt.Dimension parsedSize = GalenUtils.readSize(size);
    return new Dimension(parsedSize.width, parsedSize.height);
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
   * Get objects from {@link PageSpec}.
   * @param spec to extract objects from
   * @return selectors for all objects found in spec
   */
  public static Collection<NestedSelector> getObjects(PageSpec spec) {

    Map<String, SelectorFromLocator> objectMapping = getObjectMapping(spec);

    return extractCollectionFromMapping(objectMapping);
  }

  /**
   * Get tags device as Galen {@link SectionFilter}.
   * @param includeTags tags to use in filter
   * @return filter ready for use with Galen
   */
  public static SectionFilter getSectionFilter(String... includeTags) {
    List<String> tagList = new ArrayList<String>();
    if (includeTags != null) {
      Collections.addAll(tagList, includeTags);
    }
    return new SectionFilter(tagList, EMPTY_TAG_LIST);
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
   * Get tags from current device as Galen {@link SectionFilter}. Empty filter when no device set.
   * @return filter ready for use with Galen
   */
  public static SectionFilter getTags() {
    if (getTestDevice() != null) {
      return getSectionFilter(getTestDevice());
    }
    return getSectionFilter();
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
    return readSpec(specPath, tags, browser.getPage(), EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
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
   * @param specPath path to spec file
   * @param tags tag based filter
   * @param page page to retrieve objects from
   * @param properties properties for spec parsing
   * @param jsVars JS variables to use for spec parsing
   * @param objects predefined objects
   * @return Galen page spec object
   */
  public static PageSpec readSpec(String specPath, SectionFilter tags, Page page, Properties properties, Map<String, Object> jsVars,
      Map<String, Locator> objects) {
    try {
      return PAGE_SPEC_READER.read(specPath, page, tags, properties, jsVars, objects);
    }
    catch (IOException ex) {
      throw new GalenLayoutException("IOException when reading spec: '" + specPath + "'", ex);
    }
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

  private static Collection<NestedSelector> extractCollectionFromMapping(Map<String, SelectorFromLocator> objectMapping) {
    Collection<NestedSelector> objects = new ArrayList<>();
    Collection<SelectorFromLocator> values = objectMapping.values();
    for (SelectorFromLocator selector : values) {
      getLogger().debug("checking " + selector);
      if (selector.hasParent()) {
        getLogger().debug("has parent " + selector);
        NestedSelector parent = selector.getParent();
        getLogger().debug("parentName: '" + parent.elementName() + "'");
        String parentCss = parent.asString();
        getLogger().debug("parentCss: '" + parentCss + "'");
        SelectorFromLocator trueParent = objectMapping.get(parentCss);
        if (trueParent == null) {
          throw new GaleniumException("parent for '" + selector.elementName() + "' not found in spec ('" + parentCss + "')");
        }
        selector.setParent(trueParent);
        trueParent.addChild(selector);
      }
      else {
        getLogger().debug("no parent found.");
      }
      objects.add(selector);
      getLogger().debug("added: " + selector);
    }
    return objects;
  }

  private static List<String> getBagAsUniqueList(Bag<String> bag) {
    List<String> excludedTagList = new ArrayList<>();
    excludedTagList.addAll(bag.uniqueSet());
    return excludedTagList;
  }

  private static Map<String, SelectorFromLocator> getObjectMapping(PageSpec spec) {
    Map<String, SelectorFromLocator> objectMapping = new HashMap<String, SelectorFromLocator>();
    Map<String, Locator> objects = spec.getObjects();
    getLogger().debug("mapping " + objects.size() + " selector candidates.");
    for (Entry<String, Locator> entry : objects.entrySet()) {
      String name = cleanName(entry.getKey());
      Locator locator = entry.getValue();
      SelectorFromLocator selector = fromLocator(name, locator);
      String asString = selector.asString();
      if (objectMapping.containsKey(asString)) {
        getLogger().info("duplicate object:" + selector + " == " + objectMapping.get(asString));
      }
      else {
        objectMapping.put(asString, selector);
        getLogger().debug("mapped: " + selector);
      }
    }
    getLogger().info("mapped " + objectMapping.size() + " selectors.");
    return objectMapping;
  }

  private static String cleanName(String name) {
    getLogger().debug("mapping '" + name + "'");
    String[] nameParts = name.split("\\.");
    List<String> namePartList = new ArrayList<>();
    for (String namePart : nameParts) {
      if (namePart.matches(".*-[0-9][0-9]*")) {
        namePartList.add(namePart.replaceFirst("-[0-9][0-9]*$", ""));
      }
      else {
        namePartList.add(namePart);
      }
    }
    String cleanName = StringUtils.join(namePartList, ".");
    getLogger().debug("clean name for muliple object locator '" + cleanName + "'");
    return cleanName;
  }

}
