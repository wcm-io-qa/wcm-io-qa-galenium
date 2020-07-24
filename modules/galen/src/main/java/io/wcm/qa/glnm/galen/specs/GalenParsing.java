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

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.apache.commons.io.FilenameUtils.separatorsToUnix;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.apache.commons.lang3.RegExUtils.replacePattern;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.removeEnd;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.commons.lang3.StringUtils.trim;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
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

  // Galen uses deprecated method to read with JVM default charset. Using same behavior here.
  private static final Charset GALEN_PARSING_CHARSET = Charset.defaultCharset();
  private static final Map<String, Object> EMPTY_JS_VARS = null;
  private static final Properties EMPTY_PROPERTIES = new Properties();
  private static final Logger LOG = LoggerFactory.getLogger(GalenParsing.class);

  private GalenParsing() {
    // do not instantiate
  }

  private static List<String> getSourceFromResource(String specPath) {
    try {
      InputStream resource = getStream(specPath);
      if (resource == null) {
        if (LOG.isTraceEnabled()) {
          LOG.trace("no stream found when fetching: " + specPath);
        }
        return null;
      }
      List<String> lines = IOUtils.readLines(resource, GALEN_PARSING_CHARSET);
      return rewriteImports(lines, specPath);
    }
    catch (IOException | IllegalArgumentException ex) {
      if (LOG.isTraceEnabled()) {
        LOG.trace("when fetching: " + specPath, ex);
      }
      return null;
    }
  }

  private static List<String> rewriteImports(List<String> lines, String specPath) {
    return emptyIfNull(lines)
        .stream()
        .map(s -> rewriteImport(s, specPath))
        .collect(toList());
  }

  private static String rewriteImport(String inputLine, String importingSpecPath) {
    String trimmedInputLine = trim(inputLine);
    if (startsWith(trimmedInputLine, "@import ")) {
      String importedPath = StringUtils.removeStart(trimmedInputLine, "@import ");
      if (LOG.isDebugEnabled()) {
        LOG.debug("rewriting import: " + inputLine);
        LOG.debug("imported spec path: " + importedPath);
        LOG.debug("importing spec path: " + importingSpecPath);
      }
      String importingFolder = FilenameUtils.getFullPath(importingSpecPath);
      String rewrittenImportedPath = separatorsToUnix(combine(importingFolder, importedPath));
      if (LOG.isDebugEnabled()) {
        LOG.debug("rewritten imported spec path: " + rewrittenImportedPath);
      }
      String rewrittenLine = replacePattern(
          inputLine,
          "@import .*$",
          "@import " + rewrittenImportedPath);
      if (LOG.isDebugEnabled()) {
        LOG.debug("rewritten import: " + rewrittenLine);
      }
      return rewrittenLine;
    }
    return inputLine;
  }

  private static String prependSpecFolder(String specPath) {
    String specFolder = GaleniumConfiguration.getGalenSpecPath();
    String relativePath = StringUtils.removeStart(specPath, "/");
    return combine(specFolder, relativePath);
  }

  private static String combine(String specFolder, String relativePath) {
    String combined = removeEnd(specFolder, "/") + "/" + relativePath;
    String normalized = FilenameUtils.normalize(combined, true);
    if (LOG.isDebugEnabled()) {
      LOG.debug("combining: '" + specFolder + "' + '" + relativePath + "' -> '" + normalized + "'");
    }
    return normalized;
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
      String source = getSource(specPath);
      if (source == null) {
        throw new GaleniumException("Could not find spec at '" + specPath + "'");
      }
      if (StringUtils.isBlank(source)) {
        throw new GaleniumException("Found empty spec at '" + specPath + "'");
      }
      InputStream stream = toInputStream(source, GALEN_PARSING_CHARSET);
      //      String contextPath = FilenameUtils.getPath(specPath);
      SectionFilter filter = getFilter(tags);
      return new PageSpecReader().read(stream, source, null, new MockPage(), filter, EMPTY_PROPERTIES, EMPTY_JS_VARS, null);
    }
    catch (IOException | SyntaxException ex) {
      throw new GaleniumException("Exception when parsing spec: '" + specPath + "'", ex);
    }
  }

  private static SectionFilter getFilter(String... tags) {
    SectionFilter filter = GalenSpecUtil.getDefaultIncludeTags();
    if (ArrayUtils.isNotEmpty(tags)) {
      filter.getIncludedTags().addAll(Lists.newArrayList(tags));
    }
    return filter;
  }

  static String getSource(String specPath) {
    return join(getSourceLines(specPath), "\n");
  }

  static List<String> getSourceLines(String specPath) {
    List<String> source = getSourceFromResource(specPath);
    if (source != null && !source.isEmpty()) {
      return source;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("did not find at path: '" + specPath + "'");
    }
    String withSpecFolder = prependSpecFolder(specPath);
    if (LOG.isDebugEnabled()) {
      LOG.debug("trying with prepended path: '" + withSpecFolder + "'");
    }
    return getSourceFromResource(withSpecFolder);
  }

  static InputStream getStream(String specPath) {
    return GalenUtils.findFileOrResourceAsStream(specPath);
  }

}
