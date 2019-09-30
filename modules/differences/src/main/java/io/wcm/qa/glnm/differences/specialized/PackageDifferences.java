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
package io.wcm.qa.glnm.differences.specialized;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Package name as differences. Allows relative package names by setting root package name.
 */
public class PackageDifferences implements Differences {

  private MutableDifferences differences;
  private boolean dirty;
  private Package mainPackage;
  private String rootPackage;

  /**
   * @param p to base name on
   */
  public PackageDifferences(Package p) {
    if (p == null) {
      throw new GaleniumException("Cannot derive difference from null package");
    }
    setPackage(p);
  }

  /**
   * @param p to base name on
   * @param root to remove from beginning of package name
   */
  public PackageDifferences(Package p, String root) {
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

  public String getRootPackage() {
    return rootPackage;
  }

  @Override
  public Iterator<Difference> iterator() {
    initialize();
    return getDifferences().iterator();
  }

  /**
   * @param p to base name on
   */
  public void setPackage(Package p) {
    setMainPackage(p);
    setDirty();
  }

  /**
   * @param rootPackage to remove from beginning of package name
   */
  public void setRootPackage(String rootPackage) {
    this.rootPackage = rootPackage;
    setDirty();
  }

  @Override
  public String toString() {
    initialize();
    return "Package(" + getDifferences() + ")";
  }

  private MutableDifferences getDifferences() {
    return differences;
  }

  private String getRelativePackageName() {
    String mainName = getMainPackage().getName();
    if (hasRootPackage()) {
      String rootName = getRootPackage();
      if (StringUtils.equals(mainName, rootName)) {
        return "";
      }
      return StringUtils.removeStart(mainName, rootName + ".");
    }
    return mainName;
  }

  private boolean hasRootPackage() {
    return getRootPackage() != null;
  }

  private void initialize() {
    if (isDirty()) {
      MutableDifferences freshDifferences = new MutableDifferences();
      String[] splitUpPackageName = getRelativePackageName().split("\\.");
      for (String namePart : splitUpPackageName) {
        freshDifferences.add(new StringDifference(namePart));
      }
      setDifferences(freshDifferences);
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
