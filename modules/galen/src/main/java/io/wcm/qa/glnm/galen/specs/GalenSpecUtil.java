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
package io.wcm.qa.glnm.galen.specs;

import static io.wcm.qa.glnm.selectors.base.SelectorFactory.fromLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.PageSpec;
import com.google.common.collect.Lists;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.selectors.SelectorFromLocator;
import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Utility methods for handling Galen specs.
 *
 * @since 4.0.0
 */
final class GalenSpecUtil {

  private static final Logger LOG = LoggerFactory.getLogger(GalenSpecUtil.class);

  static final Map<String, Object> EMPTY_JS_VARS = null;
  static final Properties EMPTY_PROPERTIES = new Properties();

  private GalenSpecUtil() {
    // do not instantiate
  }

  static SectionFilter getSectionFilter(String... tags) {
    if (ArrayUtils.isEmpty(tags)) {
      return getDefaultIncludeTags();
    }
    SectionFilter sectionFilter = getDefaultIncludeTags();
    List<String> includedTags = sectionFilter.getIncludedTags();
    if (CollectionUtils.isEmpty(includedTags)) {
      sectionFilter.setIncludedTags(Arrays.asList(tags));
    } else {
      CollectionUtils.addAll(includedTags, tags);
    }
    return sectionFilter;
  }

  static GalenSpecRun createRun(GalenSpec spec, LayoutReport report) {
    return new GalenSpecRun(spec, report);
  }

  private static String cleanName(String name) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("mapping '" + name + "'");
    }
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
    if (LOG.isDebugEnabled()) {
      LOG.debug("clean name for muliple object locator '" + cleanName + "'");
    }
    return cleanName;
  }

  private static List<String> emptyList() {
    return Lists.newArrayList();
  }

  private static SectionFilter emptySectionFilter() {
    return new SectionFilter(emptyList(), emptyList());
  }

  private static Collection<NestedSelector> extractCollectionFromMapping(Map<String, SelectorFromLocator> objectMapping) {
    Collection<NestedSelector> objects = new ArrayList<>();
    Collection<SelectorFromLocator> values = objectMapping.values();
    for (SelectorFromLocator selector : values) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("checking " + selector);
      }
      if (selector.hasParent()) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("has parent " + selector);
        }
        NestedSelector parent = selector.getParent();
        if (LOG.isDebugEnabled()) {
          LOG.debug("parentName: '" + parent.elementName() + "'");
        }
        String parentCss = parent.asString();
        if (LOG.isDebugEnabled()) {
          LOG.debug("parentCss: '" + parentCss + "'");
        }
        SelectorFromLocator trueParent = objectMapping.get(parentCss);
        if (trueParent == null) {
          throw new GaleniumException("parent for '" + selector.elementName() + "' not found in spec ('" + parentCss + "')");
        }
        selector.setParent(trueParent);
        trueParent.addChild(selector);
      }
      else if (LOG.isDebugEnabled()) {
        LOG.debug("no parent found.");
      }
      objects.add(selector);
      if (LOG.isDebugEnabled()) {
        LOG.debug("added: " + selector);
      }
    }
    return objects;
  }

  private static Map<String, SelectorFromLocator> getObjectMapping(PageSpec spec) {
    Map<String, SelectorFromLocator> objectMapping = new HashMap<String, SelectorFromLocator>();
    Map<String, Locator> objects = spec.getObjects();
    if (LOG.isDebugEnabled()) {
      LOG.debug("mapping " + objects.size() + " selector candidates.");
    }
    for (Entry<String, Locator> entry : objects.entrySet()) {
      String name = cleanName(entry.getKey());
      Locator locator = entry.getValue();
      SelectorFromLocator selector = fromLocator(name, locator);
      String asString = selector.asString();
      if (objectMapping.containsKey(asString)) {
        if (LOG.isInfoEnabled()) {
          LOG.info("duplicate object:" + selector + " == " + objectMapping.get(asString));
        }
      }
      else {
        objectMapping.put(asString, selector);
        if (LOG.isDebugEnabled()) {
          LOG.debug("mapped: " + selector);
        }
      }
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("mapped " + objectMapping.size() + " selectors.");
    }
    return objectMapping;
  }

  /**
   * Get tags device as Galen {@link com.galenframework.speclang2.pagespec.SectionFilter}.
   * @param tagsForThisRun tags to use in filter
   * @return filter ready for use with Galen
   * @since 4.0.0
   */
  static SectionFilter asSectionFilter(List<String> tagsForThisRun) {
    List<String> tagList = new ArrayList<String>();
    if (ListUtils.emptyIfNull(tagList).isEmpty()) {
      return emptySectionFilter();
    }
    tagList.addAll(tagsForThisRun);
    return new SectionFilter(tagList, emptyList());
  }

  /**
   * Get tags from current device as Galen {@link com.galenframework.speclang2.pagespec.SectionFilter}. Empty filter
   * when no device set.
   *
   * @return filter ready for use with Galen
   * @since 4.0.0
   */
  static SectionFilter getDefaultIncludeTags() {
    return emptySectionFilter();
  }

  /**
   * Get objects from {@link com.galenframework.specs.page.PageSpec}.
   *
   * @param spec to extract objects from
   * @return selectors for all objects found in spec
   * @since 4.0.0
   */
  static Collection<NestedSelector> getObjects(PageSpec spec) {

    Map<String, SelectorFromLocator> objectMapping = getObjectMapping(spec);

    return extractCollectionFromMapping(objectMapping);
  }
}
