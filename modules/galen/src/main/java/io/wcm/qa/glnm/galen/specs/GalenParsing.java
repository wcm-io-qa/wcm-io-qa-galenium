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

import static org.apache.commons.lang3.StringUtils.join;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.parser.SyntaxException;
import com.galenframework.speclang2.pagespec.PageSpecReader;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.utils.GalenUtils;
import com.google.common.collect.Lists;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
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
  private static final Logger LOG = LoggerFactory.getLogger(GalenParsing.class);

  private GalenParsing() {
    // do not instantiate
  }

  private static PageSpec fromStream(String specPath, InputStream stream, String... tags) throws IOException {
    SectionFilter filter = GalenSpecUtil.getDefaultIncludeTags();
    if (ArrayUtils.isNotEmpty(tags)) {
      filter.getIncludedTags().addAll(Lists.newArrayList(tags));
    }
    String source = join(getSource(specPath), "\n");
    return new PageSpecReader().read(stream, source, null, new MockPage(), filter, EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
  }

  private static List<String> getSourceFromResource(String specPath) {
    try {
      InputStream resource = getStream(specPath);
      return IOUtils.readLines(resource, StandardCharsets.UTF_8);
    }
    catch (IOException | IllegalArgumentException ex) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("when fetching: " + specPath, ex);
      }
      return null;
    }
  }

  private static String prependSpecFolder(String specPath) {
    String specFolder = GaleniumConfiguration.getGalenSpecPath();
    String relativePath = StringUtils.removeStart(specPath, "/");
    return FilenameUtils.concat(specFolder, relativePath);
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
      InputStream stream = getStream(specPath);
      if (stream == null) {
        throw new GaleniumException("Could not find spec at '" + specPath + "'");
      }
      return fromStream(specPath, stream, tags);
    }
    catch (IOException | SyntaxException ex) {
      throw new GaleniumException("Exception when parsing spec: '" + specPath + "'", ex);
    }
  }

  static String getSource(String specPath) {
    return join(getSourceLines(specPath));
  }

  static List<String> getSourceLines(String specPath) {
    List<String> source = getSourceFromResource(specPath);
    if (source != null) {
      return source;
    }
    String withSpecFolder = prependSpecFolder(specPath);
    if (LOG.isDebugEnabled()) {
      LOG.debug("trying with prepended path: '" + withSpecFolder + "'");
    }
    return getSourceFromResource(withSpecFolder);
  }

  static InputStream getStream(String specPath) {
    InputStream stream = GalenUtils.findFileOrResourceAsStream(specPath);
    if (stream != null) {
      return stream;
    }
    return GalenUtils.findFileOrResourceAsStream(prependSpecFolder(specPath));
  }

}
