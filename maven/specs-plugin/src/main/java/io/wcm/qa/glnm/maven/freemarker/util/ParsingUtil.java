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
package io.wcm.qa.glnm.maven.freemarker.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.galen.specs.GalenSpec;
import io.wcm.qa.glnm.galen.specs.GalenSpecParsingProvider;
import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Utility methods for parsing Galen specs.
 *
 * @since 1.0.0
 */
public final class ParsingUtil {

  private static final String PATTERN_LINE_WITH_TAGS_IN_SPEC = "@on";

  private ParsingUtil() {
    // do not instantiate
  }

  /**
   * Extracts all selectors defined in spec.
   *
   * @param specFile to parse
   * @return all defined objects as selectors
   */
  public static Collection<NestedSelector> getSelectorsFromSpec(File specFile) {
    GalenSpec galenSpec = new GalenSpec();
    galenSpec.setGalenSpecProvider(new GalenSpecParsingProvider(specFile.getPath()));
    return galenSpec.getObjects();
  }

  /**
   * <p>getTags.</p>
   *
   * @param specFile to parse
   * @return all tags used in spec
   */
  public static Collection<String> getTags(File specFile) {
    try {
      Bag<String> tags = new HashBag<>();
      List<String> lines = FileUtils.readLines(specFile, StandardCharsets.UTF_8);
      for (String line : lines) {
        String trimmedLine = line.trim();
        if (StringUtils.startsWith(trimmedLine, PATTERN_LINE_WITH_TAGS_IN_SPEC)) {
          String tagsAsString = StringUtils.removeStart(trimmedLine, PATTERN_LINE_WITH_TAGS_IN_SPEC);
          String[] tagsAsArray = tagsAsString.split(",");
          for (String tag : tagsAsArray) {
            if (StringUtils.isNotBlank(tag)) {
              String cleanedTag = tag.trim().replaceAll("[^A-Za-z0-9]*", "");
              if (StringUtils.isNotBlank(cleanedTag)) {
                tags.add(cleanedTag);
              }
            }
          }
        }
      }
      return tags.uniqueSet();
    }
    catch (IOException ex) {
      throw new GaleniumException("when parsing '" + specFile + "'");
    }
  }

}
