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
package io.wcm.qa.galenium.maven.freemarker.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.galen.GalenHelperUtil;
import io.wcm.qa.galenium.maven.freemarker.util.FormatUtil;
import io.wcm.qa.galenium.maven.freemarker.util.ParsingUtil;
import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.util.FileHandlingUtil;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

public class SpecPojo {

  private PageSpec pageSpec;
  private Collection<NestedSelector> selectors;
  private File specFile;
  private Collection<String> tags;

  public SpecPojo(File specFile) {
    setSpecFile(specFile);
  }

  public String getBasename() {
    return FilenameUtils.getBaseName(getFilename());
  }

  public String getClassName() {
    File file = getSpecFile();
    return FormatUtil.getClassName(file);
  }

  public String getFilename() {
    return getSpecFile().getName();
  }

  public String getPackageName() {
    String baseName = FilenameUtils.getBaseName(getSpecFile().getPath());
    return baseName.toLowerCase().replaceAll("[^a-z0-9]", "");
  }

  public PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = ParsingUtil.readSpec(getSpecFile());
    }
    return pageSpec;
  }

  public Collection<NestedSelector> getRootSelectors() {
    Collection<NestedSelector> rootSelectors = new ArrayList<>();
    for (NestedSelector selector : getSelectors()) {
      if (!selector.hasParent()) {
        rootSelectors.add(selector);
      }
    }
    return rootSelectors;
  }

  public Collection<NestedSelector> getSelectors() {
    if (selectors == null) {
      selectors = GalenHelperUtil.getObjects(getPageSpec());
    }
    return selectors;
  }

  public File getSpecFile() {
    return specFile;
  }

  public Collection<String> getTags(){
    if (tags == null) {
      tags = ParsingUtil.getTags(getSpecFile());
    }
    if (tags.isEmpty()) {
      return null;
    }
    return tags;
  }

  public String getRelativeFilePath() {
    String relativePath = FileHandlingUtil.constructRelativePath(getSpecRootDirectory(), getSpecFile());
    return getUnixStyleFilePath(relativePath);
  }

  private File getSpecRootDirectory() {
    File file = new File(GaleniumConfiguration.getGalenSpecPath());
    if (file.isDirectory()) {
      return file;
    }
    throw new GaleniumException("spec root is not a directory: " + file);
  }

  private void setSpecFile(File specFile) {
    this.specFile = specFile;
  }

  private static String getUnixStyleFilePath(String path) {
    return FilenameUtils.normalize(path, true);
  }

}
