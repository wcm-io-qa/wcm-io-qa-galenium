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

import com.galenframework.specs.Spec;
import com.galenframework.specs.page.ObjectSpecs;
import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.specs.page.SpecGroup;

import io.wcm.qa.galenium.maven.freemarker.util.FormatUtil;
import io.wcm.qa.galenium.maven.freemarker.util.ParsingUtil;
import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.util.GalenHelperUtil;

public class SpecPojo {

  private Collection<NestedSelector> selectors;
  private File specFile;
  private PageSpec pageSpec;

  public SpecPojo(File specFile) {
    setSpecFile(specFile);
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
    for (NestedSelector selector : getSelectors()) {
      if (!selector.hasParent()) {
        rootSelectors.add(selector);
      }
    }
    return rootSelectors;
  }

  public String getFilename() {
    return getSpecFile().getName();
  }

  public String getBasename() {
    return FilenameUtils.getBaseName(getFilename());
  }

  public PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = ParsingUtil.readSpec(getSpecFile());
    }
    return pageSpec;
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
    Collection<String> tags = new ArrayList<>();
    List<PageSection> sections = getPageSpec().getSections();
    for (PageSection pageSection : sections) {
      List<ObjectSpecs> objects = pageSection.getObjects();
      for (ObjectSpecs objectSpecs : objects) {
        List<SpecGroup> specGroups = objectSpecs.getSpecGroups();
        for (SpecGroup specGroup : specGroups) {
          List<Spec> specs = specGroup.getSpecs();
          for (Spec spec : specs) {

          }
        }
      }
    }
    return tags;
  }

  private void setSpecFile(File specFile) {
    this.specFile = specFile;
  }

}
