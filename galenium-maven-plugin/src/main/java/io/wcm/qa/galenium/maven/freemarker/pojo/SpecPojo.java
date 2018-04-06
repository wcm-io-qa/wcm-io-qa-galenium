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
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import io.wcm.qa.galenium.maven.freemarker.util.FormatUtil;
import io.wcm.qa.galenium.selectors.NestedSelector;

public class SpecPojo {

  private List<NestedSelector> selectors = new ArrayList<>();
  private File specFile;

  public SpecPojo(File specFile, Collection<NestedSelector> selectors) {
    setSpecFile(specFile);
    addSelectors(selectors);
  }

  public String getClassName() {
    File file = getSpecFile();
    return FormatUtil.getClassName(file);
  }

  public String getPackageName() {
    String baseName = FilenameUtils.getBaseName(getSpecFile().getPath());
    return baseName.toLowerCase().replaceAll("[^a-z0-9]", "");
  }

  public Collection<NestedSelector> getRootSelectors() {
    Collection<NestedSelector> rootSelectors = new ArrayList<>();
    for (NestedSelector selector : selectors) {
      if (!selector.hasParent()) {
        rootSelectors.add(selector);
      }
    }
    return rootSelectors;
  }

  public List<NestedSelector> getSelectors() {
    return selectors;
  }

  public File getSpecFile() {
    return specFile;
  }

  private void addSelectors(Collection<NestedSelector> newSelectors) {
    selectors.addAll(newSelectors);
  }

  private void setSpecFile(File specFile) {
    this.specFile = specFile;
  }

}
