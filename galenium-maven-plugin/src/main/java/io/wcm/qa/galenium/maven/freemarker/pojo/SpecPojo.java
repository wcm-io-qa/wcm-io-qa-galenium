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
import io.wcm.qa.galenium.selectors.Selector;

public class SpecPojo {


  private List<SelectorPojo> selectors = new ArrayList<>();
  private File specFile;

  public SpecPojo(File specFile, Collection<Selector> selectors) {
    setSpecFile(specFile);
    setSelectors(selectors);
  }

  public String getClassName() {
    return FormatUtil.kebapToUpperCamel(FilenameUtils.getBaseName(getSpecFile().getPath()));
  }

  public Collection<SelectorPojo> getSelectors() {
    return selectors;
  }

  public File getSpecFile() {
    return specFile;
  }

  private void setSelectors(Collection<Selector> selectors) {
    for (Selector selector : selectors) {
      getSelectors().add(new SelectorPojo(selector));
    }
  }

  private void setSpecFile(File specFile) {
    this.specFile = specFile;
  }

}
