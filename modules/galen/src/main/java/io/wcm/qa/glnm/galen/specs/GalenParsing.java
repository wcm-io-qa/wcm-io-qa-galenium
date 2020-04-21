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

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;

import com.galenframework.parser.SyntaxException;
import com.galenframework.speclang2.pagespec.PageSpecReader;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.google.common.collect.Lists;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.galen.mock.MockPage;

/**
 * Helper methods to parse specs from files or strings.
 *
 * @since 4.0.0
 */
final class GalenParsing {

  private static final Map<String, Object> EMPTY_JS_VARS = null;
  private static final Properties EMPTY_PROPERTIES = new Properties();

  private GalenParsing() {
    // do not instantiate
  }

  /**
   * Convenience method to read a Galen spec using current threads context. Basically a convenience mapping to
   * {@link com.galenframework.speclang2.pagespec.PageSpecReader#read(String, com.galenframework.page.Page, SectionFilter, Properties, Map, Map)}.
   * @param specPath path to spec file
   * @param tags include tags to use with spec
   * @return Galen page spec object
   * @since 4.0.0
   */
  static PageSpec fromPath(String specPath, String... tags) {
    try {
      SectionFilter filter = GalenSpecUtil.getDefaultIncludeTags();
      if (ArrayUtils.isNotEmpty(tags)) {
        filter.getIncludedTags().addAll(Lists.newArrayList(tags));
      }
      return new PageSpecReader().read(specPath, new MockPage(), filter, EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
    }
    catch (IOException | SyntaxException ex) {
      throw new GaleniumException("Exception when parsing spec: '" + specPath + "'", ex);
    }
  }

}
