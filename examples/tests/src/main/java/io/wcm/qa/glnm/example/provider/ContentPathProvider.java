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
package io.wcm.qa.glnm.example.provider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.aem.AllPagesForTemplateSampler;

/**
 * DataProvider for AEM content paths using JCR query on author.
 */
public final class ContentPathProvider {

  /**
   * All pages created with the example application.
   */
  public static final String ALL_PAGES = "AllPages";
  public static final String ALL_PAGES_FOR_EXAMPLE_TEMPLATES = "AllPagesForTemplates";

  private static final Logger LOG = LoggerFactory.getLogger(ContentPathProvider.class);
  private static final String ROOT_PATH = "/content/wcm-io-samples";
  private static final String TEMPLATE_NAME_PATTERN = "/apps/wcm-io-samples/core/templates/%";
  private static final String WEBAPP_CONTENT_PATH = "src/main/webapp";

  private ContentPathProvider() {
    // do not instantiate
  }

  /**
   * @return all pages created with the example application
   */
  public static Iterable<String> allPagesForTemplates() {
    LOG.debug("Data providing: " + ALL_PAGES_FOR_EXAMPLE_TEMPLATES);
    Sampler<Iterable<String>> sampler = new AllPagesForTemplateSampler(TEMPLATE_NAME_PATTERN, ROOT_PATH);
    return sampler.sampleValue();
  }

  /**
   * @return all pages created with the example application
   */
  public static Collection<String> collectHtmlPaths() {
    File directory = new File(WEBAPP_CONTENT_PATH);
    if (!directory.isDirectory()) {
      LOG.warn("not a directory: " + directory);
    }
    String[] extensions = ArrayUtils.toArray("html");
    boolean recursive = true;
    Collection<File> htmlFiles = FileUtils.listFiles(directory, extensions, recursive);
    Collection<String> htmlFilePaths = new ArrayList<String>();
    for (File file : htmlFiles) {
      LOG.debug("found: " + file);
      htmlFilePaths.add(file.getPath());
    }
    return htmlFilePaths;
  }
}
