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
package io.wcm.qa.glnm.galen.specs;

import java.util.Collection;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.galen.validation.GalenValidation;
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
   * @param specPath to parse
   * @return all defined objects as selectors
   */
  public static Collection<NestedSelector> getSelectorsFromSpec(String specPath) {
    GalenSpec galenSpec = GalenValidation.readSpec(specPath);
    return galenSpec.getObjects();
  }

  /**
   * <p>
   * getTags.
   * </p>
   * @param specPath to parse
   * @return all tags used in spec
   */
  public static Collection<String> getTags(String specPath) {
    Bag<String> tags = new HashBag<>();
    for (String line : getSourceLines(specPath)) {
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

  private static String[] getSourceLines(String specPath) {
    String source = GalenParsing.getSource(specPath);
    if (StringUtils.isBlank(source)) {
      return new String[0];
    }
    return source.split("\n");
  }

}
