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
package io.wcm.qa.glnm.maven.freemarker.pojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;

import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.galen.GalenHelperUtil;
import io.wcm.qa.glnm.maven.freemarker.util.FormatUtil;
import io.wcm.qa.glnm.maven.freemarker.util.ParsingUtil;
import io.wcm.qa.glnm.selectors.base.NestedSelector;
import io.wcm.qa.glnm.util.FileHandlingUtil;

/**
 * Holds data from Galen spec to use in generation.
 */
public class SpecPojo {

  private PageSpec pageSpec;
  private Collection<SelectorPojo> selectors;
  private File specFile;
  private Collection<String> tags;

  /**
   * @param specFile to retrieve data from
   */
  public SpecPojo(File specFile) {
    setSpecFile(specFile);
  }

  public String getBaseName() {
    return FilenameUtils.getBaseName(getFilename());
  }

  /**
   * @return class name extracted from spec file
   */
  public String getClassName() {
    File file = getSpecFile();
    return FormatUtil.getClassName(file);
  }

  /**
   * @return spec file name
   */
  public String getFilename() {
    return getSpecFile().getName();
  }

  /**
   * @return package name from spec file path
   */
  public String getPackageNamePart() {
    String baseName = getBaseName();
    return baseName.toLowerCase().replaceAll("[^a-z0-9]", "");
  }

  /**
   * @return parsed Galen spec
   */
  public PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = ParsingUtil.readSpec(getSpecFile());
    }
    return pageSpec;
  }

  /**
   * @return relative path to Galen spec file
   */
  public String getRelativeFilePath() {
    String relativePath = FileHandlingUtil.constructRelativePath(getSpecRoot(), getSpecFile());
    return getUnixStyleFilePath(relativePath);
  }

  /**
   * @return root objects from Galen spec
   */
  public Collection<SelectorPojo> getRootSelectors() {
    Collection<SelectorPojo> rootSelectors = new ArrayList<>();
    for (NestedSelector selector : getSelectors()) {
      if (!selector.hasParent()) {
        rootSelectors.add(new SelectorPojo(this, selector));
      }
    }
    return rootSelectors;
  }

  /**
   * @return all selectors from spec
   */
  public Collection<SelectorPojo> getSelectors() {
    if (selectors == null) {
      selectors = new ArrayList<SelectorPojo>();
      for (NestedSelector selector : GalenHelperUtil.getObjects(getPageSpec())) {
        selectors.add(new SelectorPojo(this, selector));
      }
    }
    return selectors;
  }

  public File getSpecFile() {
    return specFile;
  }

  /**
   * @return tags used in Galen spec
   */
  public Collection<String> getTags() {
    if (tags == null) {
      tags = ParsingUtil.getTags(getSpecFile());
    }
    if (tags.isEmpty()) {
      return null;
    }
    return tags;
  }

  private File getSpecRoot() {
    return new File(GaleniumConfiguration.getGalenSpecPath());
  }

  private void setSpecFile(File specFile) {
    this.specFile = specFile;
  }

  private static String getUnixStyleFilePath(String path) {
    return FilenameUtils.normalize(path, true);
  }

}
