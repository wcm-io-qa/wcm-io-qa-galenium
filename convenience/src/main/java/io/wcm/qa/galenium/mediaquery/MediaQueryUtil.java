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
package io.wcm.qa.galenium.mediaquery;

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

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.util.ConfigurationUtil;
import io.wcm.qa.galenium.util.GaleniumContext;

public final class MediaQueryUtil {

  private static final int CONFIGURED_MAX_WIDTH = GaleniumConfiguration.getMediaQueryMaximalWidth();
  private static final int CONFIGURED_MIN_WIDTH = GaleniumConfiguration.getMediaQueryMinimalWidth();
  private static final MediaQuery DEFAULT_MEDIA_QUERY = getNewMediaQuery("DEFAULT_MQ", CONFIGURED_MIN_WIDTH, CONFIGURED_MAX_WIDTH);

  private static final Map<String, Collection<MediaQuery>> mediaQueryMapFileName = new HashMap<>();
  private static final Map<Properties, Collection<MediaQuery>> mediaQueryMapProperties = new HashMap<>();

  private MediaQueryUtil() {
    // do not instantiate
  }

  public static MediaQuery getCurrentMediaQuery() {
    WebDriver driver = GaleniumContext.getDriver();
    if (driver == null) {
      return DEFAULT_MEDIA_QUERY;
    }
    Collection<MediaQuery> mediaQueries = getMediaQueries();
    for (MediaQuery mediaQuery : mediaQueries) {
      if (matchesCurrentTestDevice(mediaQuery)) {
        return mediaQuery;
      }
    }
    return DEFAULT_MEDIA_QUERY;
  }

  public static Collection<MediaQuery> getMediaQueries() {
    String propertiesFilePath = GaleniumConfiguration.getMediaQueryPropertiesPath();
    if (StringUtils.isBlank(propertiesFilePath)) {
      throw new GaleniumException("path to media query properties is empty");
    }
    Collection<MediaQuery> mediaQueries = getMediaQueries(propertiesFilePath);
    return mediaQueries;
  }

  public static Collection<MediaQuery> getMediaQueries(File mediaQueryPropertyFile) {
    if (mediaQueryPropertyFile == null) {
      throw new GaleniumException("media query properties file is null.");
    }
    if (!mediaQueryPropertyFile.isFile()) {
      throw new GaleniumException("media query properties file is not a file.");
    }
    return getMediaQueries(mediaQueryPropertyFile.getPath());
  }

  public static Collection<MediaQuery> getMediaQueries(Properties mediaQueryProperties) {
    if (mediaQueryMapProperties.containsKey(mediaQueryProperties)) {
      return mediaQueryMapProperties.get(mediaQueryProperties);
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
    mediaQueryMapProperties.put(mediaQueryProperties, mediaQueries);
    return mediaQueries;
  }

  public static Collection<MediaQuery> getMediaQueries(String propertiesFilePath) {
    if (mediaQueryMapFileName.containsKey(propertiesFilePath)) {
      return mediaQueryMapFileName.get(propertiesFilePath);
    }
    Properties mediaQueryProperties = ConfigurationUtil.loadProperties(propertiesFilePath);
    Collection<MediaQuery> mediaQueries = getMediaQueries(mediaQueryProperties);
    mediaQueryMapFileName.put(propertiesFilePath, mediaQueries);
    return mediaQueries;
  }

  public static MediaQuery getMediaQueryByName(String name) {
    Collection<MediaQuery> mediaQueries = getMediaQueries();
    for (MediaQuery mediaQuery : mediaQueries) {
      if (StringUtils.equals(name, mediaQuery.getName())) {
        return mediaQuery;
      }
    }
    throw new GaleniumException("did not find media query for '" + name + "'");
  }

  public static MediaQueryInstance getNewMediaQuery(String mediaQueryName, int lowerBound, int upperBound) {
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

  private static boolean matchesCurrentTestDevice(MediaQuery mediaQuery) {
    TestDevice testDevice = GaleniumContext.getTestDevice();
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

    public MediaQueryInstance(String mediaQueryName, int lowerBound, int upperBound) {
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
