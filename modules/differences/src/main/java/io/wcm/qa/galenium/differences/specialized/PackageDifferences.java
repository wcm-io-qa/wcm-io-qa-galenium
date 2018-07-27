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
package io.wcm.qa.galenium.differences.specialized;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.differences.base.Differences;
import io.wcm.qa.galenium.differences.difference.StringDifference;
import io.wcm.qa.galenium.differences.generic.MutableDifferences;


public class PackageDifferences implements Differences {

  private MutableDifferences differences;
  private boolean dirty;
  private Package mainPackage;
  private Package rootPackage;

  public PackageDifferences(Package p) {
    setPackage(p);
  }

  public PackageDifferences(Package p, Package root) {
    this(p);
    setRootPackage(root);
  }

  @Override
  public String asFilePath() {
    initialize();
    return getDifferences().asFilePath();
  }

  @Override
  public String asPropertyKey() {
    initialize();
    return getDifferences().asPropertyKey();
  }

  public Package getMainPackage() {
    return mainPackage;
  }

  public Package getRootPackage() {
    return rootPackage;
  }

  @Override
  public Iterator<Difference> iterator() {
    initialize();
    return getDifferences().iterator();
  }

  public void setPackage(Package p) {
    setMainPackage(p);
    setDirty();
  }

  public void setRootPackage(Package rootPackage) {
    this.rootPackage = rootPackage;
    setDirty();
  }

  private MutableDifferences getDifferences() {
    return differences;
  }

  private String getRelativePackageName() {
    String mainName = getMainPackage().getName();
    if (hasRootPackage()) {
      String rootName = getRootPackage().getName();
      return StringUtils.removeStart(mainName, rootName);
    }
    return mainName;
  }

  private boolean hasRootPackage() {
    return getRootPackage() != null;
  }

  private void initialize() {
    if (isDirty()) {
      String[] splitUpPackageName = getRelativePackageName().split("\\.");
      for (String namePart : splitUpPackageName) {
        setDifferences(new MutableDifferences());
        getDifferences().add(new StringDifference(namePart));
      }
      setClean();
    }
  }

  private boolean isDirty() {
    return dirty;
  }

  private void setClean() {
    this.dirty = false;
  }

  private void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

  private void setDirty() {
    this.dirty = true;
  }

  private void setMainPackage(Package mainPackage) {
    this.mainPackage = mainPackage;
    setDirty();
  }

}
