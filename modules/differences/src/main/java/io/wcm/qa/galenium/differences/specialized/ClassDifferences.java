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

import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.differences.base.Differences;
import io.wcm.qa.galenium.differences.difference.ClassNameDifference;
import io.wcm.qa.galenium.differences.generic.MutableDifferences;

/**
 * Class and package name differences constructed from class object.
 *
 * @since 1.0.0
 */
public class ClassDifferences implements Differences {

  private ClassNameDifference classDifference;
  private PackageDifferences packageDifferences;

  /**
   * <p>Constructor for ClassDifferences.</p>
   *
   * @param clazz to extract class and package name from
   */
  public ClassDifferences(Class clazz) {
    setClass(clazz);
  }

  /** {@inheritDoc} */
  @Override
  public String asFilePath() {
    return getDifferences().asFilePath();
  }

  /** {@inheritDoc} */
  @Override
  public String asPropertyKey() {
    return getDifferences().asPropertyKey();
  }

  /**
   * <p>getRootPackage.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getRootPackage() {
    return getPackageDifferences().getRootPackage();
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  /**
   * <p>setClass.</p>
   *
   * @param clazz to extract class and package name from
   */
  public void setClass(Class clazz) {
    setClassDifference(new ClassNameDifference(clazz));
    setPackage(clazz.getPackage());
  }

  /**
   * Set maximum length of class name in rendered keys.
   *
   * @param maxLength when to start shortening class name differnce key
   */
  public void setClassNameMaxLength(int maxLength) {
    getClassDifference().setMaxTagLength(maxLength);
  }

  /**
   * <p>setPackage.</p>
   *
   * @param p to extract package name from
   */
  public void setPackage(Package p) {
    setPackageDifferences(new PackageDifferences(p));
  }

  /**
   * <p>setRootPackage.</p>
   *
   * @param rootPackage will be removed from beginning of package name
   */
  public void setRootPackage(String rootPackage) {
    getPackageDifferences().setRootPackage(rootPackage);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Class(package=");
    stringBuilder.append(getPackageDifferences().toString());
    stringBuilder.append("|class=");
    stringBuilder.append(getClassDifference().toString());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }

  private ClassNameDifference getClassDifference() {
    if (classDifference == null) {
      useDefaultClass();
    }
    return classDifference;
  }

  private Differences getDifferences() {
    MutableDifferences differences = new MutableDifferences();
    differences.addAll(getPackageDifferences());
    differences.add(getClassDifference());
    return differences;
  }

  private PackageDifferences getPackageDifferences() {
    if (packageDifferences == null) {
      useDefaultClass();
    }
    return packageDifferences;
  }

  protected void useDefaultClass() {
    setClass(Object.class);
  }

  private void setClassDifference(ClassNameDifference classDifference) {
    this.classDifference = classDifference;
  }

  private void setPackageDifferences(PackageDifferences packageDifferences) {
    this.packageDifferences = packageDifferences;
  }
}
