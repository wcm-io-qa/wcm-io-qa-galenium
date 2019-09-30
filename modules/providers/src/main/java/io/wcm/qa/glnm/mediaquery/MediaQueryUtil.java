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
package io.wcm.qa.glnm.mediaquery;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.configuration.PropertiesUtil;
import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.util.GaleniumContext;

/**
 * Some convenience methods around {@link MediaQuery}.
 */
public final class MediaQueryUtil {

  private static final int CONFIGURED_MAX_WIDTH = GaleniumConfiguration.getMediaQueryMaximalWidth();
  private static final int CONFIGURED_MIN_WIDTH = GaleniumConfiguration.getMediaQueryMinimalWidth();
  private static final MediaQuery DEFAULT_MEDIA_QUERY = getNewMediaQuery("DEFAULT_MQ", CONFIGURED_MIN_WIDTH, CONFIGURED_MAX_WIDTH);

  private static final Map<String, Collection<MediaQuery>> MAP_MEDIA_QUERIES_FILENAMES = new HashMap<>();
  private static final Map<Properties, Collection<MediaQuery>> MAP_MEDIA_QUERIES_PROPERTIES = new HashMap<>();

  private MediaQueryUtil() {
    // do not instantiate
  }

  /**
   * @return the media query used in current test device
   */
  public static MediaQuery getCurrentMediaQuery() {
    WebDriver driver = GaleniumContext.getDriver();
    if (driver == null) {
      return DEFAULT_MEDIA_QUERY;
    }
    return getMatchingMediaQuery(GaleniumContext.getTestDevice());
  }

  /**
   * Returns matching media query from configuration.
   * @param testDevice to match
   * @return media query
   */
  public static MediaQuery getMatchingMediaQuery(TestDevice testDevice) {
    Collection<MediaQuery> mediaQueries = getMediaQueries();
    for (MediaQuery mediaQuery : mediaQueries) {
      if (matchesTestDevice(mediaQuery, testDevice)) {
        return mediaQuery;
      }
    }
    return DEFAULT_MEDIA_QUERY;
  }

  /**
   * @return get all defined media queries
   */
  public static Collection<MediaQuery> getMediaQueries() {
    String propertiesFilePath = GaleniumConfiguration.getMediaQueryPropertiesPath();
    if (StringUtils.isBlank(propertiesFilePath)) {
      throw new GaleniumException("path to media query properties is empty");
    }
    Collection<MediaQuery> mediaQueries = getMediaQueries(propertiesFilePath);
    return mediaQueries;
  }

  /**
   * @param mediaQueryPropertyFile to get media query definitions from
   * @return all media queries configured in properties file
   */
  public static Collection<MediaQuery> getMediaQueries(File mediaQueryPropertyFile) {
    if (mediaQueryPropertyFile == null) {
      throw new GaleniumException("media query properties file is null.");
    }
    if (!mediaQueryPropertyFile.isFile()) {
      throw new GaleniumException("media query properties file is not a file.");
    }
    return getMediaQueries(mediaQueryPropertyFile.getPath());
  }


  /**
   * @param mediaQueryProperties to get media query definitions from
   * @return all media queries configured in properties
   */
  public static Collection<MediaQuery> getMediaQueries(Properties mediaQueryProperties) {
    if (MAP_MEDIA_QUERIES_PROPERTIES.containsKey(mediaQueryProperties)) {
      return MAP_MEDIA_QUERIES_PROPERTIES.get(mediaQueryProperties);
    }
    Collection<MediaQuery> mediaQueries = new ArrayList<MediaQuery>();
    SortedMap<Integer, String> sortedMediaQueryMap = getSortedMediaQueryMap(mediaQueryProperties);
    Set<Entry<Integer, String>> entrySet = sortedMediaQueryMap.entrySet();
    int lowerBound = CONFIGURED_MIN_WIDTH;
    for (Entry<Integer, String> entry : entrySet) {
      String mediaQueryName = entry.getValue();
      int upperBound = entry.getKey();
      mediaQueries.add(getNewMediaQuery(mediaQueryName, lowerBound, upperBound));
      lowerBound = upperBound + 1;
    }
    if (getLogger().isDebugEnabled()) {
      getLogger().debug("generated " + mediaQueries.size() + " media queries");
      for (MediaQuery mediaQuery : mediaQueries) {
        getLogger().debug("  " + mediaQuery);
      }
    }
    MAP_MEDIA_QUERIES_PROPERTIES.put(mediaQueryProperties, mediaQueries);
    return mediaQueries;
  }

  /**
   * @param propertiesFilePath to get properties file with media query definitions from
   * @return all media queries configured in properties file
   */
  public static Collection<MediaQuery> getMediaQueries(String propertiesFilePath) {
    if (MAP_MEDIA_QUERIES_FILENAMES.containsKey(propertiesFilePath)) {
      return MAP_MEDIA_QUERIES_FILENAMES.get(propertiesFilePath);
    }
    Properties mediaQueryProperties = PropertiesUtil.loadProperties(propertiesFilePath);
    Collection<MediaQuery> mediaQueries = getMediaQueries(mediaQueryProperties);
    MAP_MEDIA_QUERIES_FILENAMES.put(propertiesFilePath, mediaQueries);
    return mediaQueries;
  }

  /**
   * @param name of media query to retrieve
   * @return media query matching name
   */
  public static MediaQuery getMediaQueryByName(String name) {
    Collection<MediaQuery> mediaQueries = getMediaQueries();
    for (MediaQuery mediaQuery : mediaQueries) {
      if (StringUtils.equals(name, mediaQuery.getName())) {
        return mediaQuery;
      }
    }
    throw new GaleniumException("did not find media query for '" + name + "'");
  }

  /**
   * Factory method to create {@link MediaQuery}
   * @param mediaQueryName name to use for media query
   * @param lowerBound to use as lower bound in media query
   * @param upperBound to use as upper bound in media query
   * @return a new media query instance
   */
  public static MediaQuery getNewMediaQuery(String mediaQueryName, int lowerBound, int upperBound) {
    if (lowerBound < CONFIGURED_MIN_WIDTH) {
      throw new GaleniumException("MediaQuery: illegally low lower bound for '" + mediaQueryName + "': " + lowerBound + " < " + CONFIGURED_MIN_WIDTH);
    }
    if (upperBound < CONFIGURED_MIN_WIDTH) {
      throw new GaleniumException("MediaQuery: illegally low upper bound for '" + mediaQueryName + "': " + upperBound + " < " + CONFIGURED_MIN_WIDTH);
    }
    if (upperBound > CONFIGURED_MAX_WIDTH) {
      throw new GaleniumException("MediaQuery: illegally high upper bound for '" + mediaQueryName + "': " + upperBound + " > " + CONFIGURED_MAX_WIDTH);
    }
    if (lowerBound > upperBound) {
      throw new GaleniumException("illegal media query lower and upper bound combination for '" + mediaQueryName + "': " + lowerBound + " > " + upperBound);
    }
    return new MediaQueryInstance(mediaQueryName, lowerBound, upperBound);
  }

  private static Integer getIntegerValue(Entry<Object, Object> entry) {
    Object value = entry.getValue();
    if (value == null) {
      throw new GaleniumException("value null for '" + entry.getKey() + "'");
    }
    try {
      return Integer.parseInt(value.toString());
    }
    catch (NumberFormatException ex) {
      throw new GaleniumException("could not parse to integer: '" + value, ex);
    }
  }

  private static Logger getLogger() {
    return GaleniumReportUtil.getLogger();
  }

  private static SortedMap<Integer, String> getSortedMediaQueryMap(Properties mediaQueryProperties) {
    SortedMap<Integer, String> mediaQueryMap = new TreeMap<Integer, String>();
    Set<Entry<Object, Object>> mqEntries = mediaQueryProperties.entrySet();
    for (Entry<Object, Object> entry : mqEntries) {
      Integer intValue = getIntegerValue(entry);
      String mediaQueryName = entry.getKey().toString();
      mediaQueryMap.put(intValue, mediaQueryName);
    }
    return mediaQueryMap;
  }

  private static boolean matchesTestDevice(MediaQuery mediaQuery, TestDevice testDevice) {
    if (testDevice == null) {
      return false;
    }
    Dimension screenSize = testDevice.getScreenSize();
    if (screenSize.getWidth() < mediaQuery.getLowerBound()) {
      return false;
    }
    if (screenSize.getWidth() > mediaQuery.getUpperBound()) {
      return false;
    }
    return true;
  }

  private static final class MediaQueryInstance implements MediaQuery {

    private int high;
    private int low;
    private String name;

    MediaQueryInstance(String mediaQueryName, int lowerBound, int upperBound) {
      name = mediaQueryName;
      low = lowerBound;
      high = upperBound;
    }

    @Override
    public int getLowerBound() {
      return low;
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public int getUpperBound() {
      return high;
    }

    @Override
    public String toString() {
      return name + "(lower: " + low + ", upper: " + high + ")";
    }
  }

}
