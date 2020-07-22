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

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.galen.specs.ParsingUtil;
import io.wcm.qa.glnm.maven.freemarker.util.FormatUtil;
import io.wcm.qa.glnm.selectors.base.NestedSelector;
import io.wcm.qa.glnm.util.FileHandlingUtil;

/**
 * Holds data from Galen spec to use in generation.
 *
 * @since 1.0.0
 */
public class SpecPojo {

  private Collection<SelectorPojo> selectors;
  private String specPath;
  private Collection<String> tags;

  /**
   * <p>Constructor for SpecPojo.</p>
   *
   * @param specPath to retrieve data from
   */
  public SpecPojo(String specPath) {
    setSpecPath(specPath);
  }

  /**
   * <p>getBaseName.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getBaseName() {
    return FilenameUtils.getBaseName(getFilename());
  }

  /**
   * <p>getClassName.</p>
   *
   * @return class name extracted from spec file
   */
  public String getClassName() {
    String path = getSpecPath();
    return FormatUtil.getClassName(path);
  }

  /**
   * <p>getFilename.</p>
   *
   * @return spec file name
   */
  public String getFilename() {
    return FilenameUtils.getName(getSpecPath());
  }

  /**
   * <p>getPackageNamePart.</p>
   *
   * @return package name from spec file path
   */
  public String getPackageNamePart() {
    String baseName = getBaseName();
    return baseName.toLowerCase().replaceAll("[^a-z0-9]", "");
  }

  /**
   * <p>getRelativeFilePath.</p>
   *
   * @return relative path to Galen spec file
   */
  public String getRelativeFilePath() {
    String relativePath = FileHandlingUtil.constructRelativePath(getSpecRoot(), new File(getSpecPath()));
    return getUnixStyleFilePath(relativePath);
  }

  /**
   * <p>getRootSelectors.</p>
   *
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
   * <p>Getter for the field <code>selectors</code>.</p>
   *
   * @return all selectors from spec
   */
  public Collection<SelectorPojo> getSelectors() {
    if (selectors == null) {
      selectors = new ArrayList<SelectorPojo>();
      for (NestedSelector selector : ParsingUtil.getSelectorsFromSpec(getSpecPath())) {
        selectors.add(new SelectorPojo(this, selector));
      }
    }
    return selectors;
  }

  /**
   * <p>Getter for the field <code>specFile</code>.</p>
   *
   * @return a {@link java.io.File} object.
   */
  public String getSpecPath() {
    return specPath;
  }

  /**
   * <p>Getter for the field <code>tags</code>.</p>
   *
   * @return tags used in Galen spec
   */
  public Collection<String> getTags() {
    if (tags == null) {
      tags = ParsingUtil.getTags(getSpecPath());
    }
    if (tags.isEmpty()) {
      return null;
    }
    return tags;
  }

  private File getSpecRoot() {
    return new File(GaleniumConfiguration.getGalenSpecPath());
  }

  private void setSpecPath(String specPath) {
    this.specPath = specPath;
  }

  private static String getUnixStyleFilePath(String path) {
    return FilenameUtils.normalize(path, true);
  }

}
