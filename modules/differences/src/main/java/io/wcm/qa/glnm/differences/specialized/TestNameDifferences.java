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

import org.testng.ITest;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.difference.testcase.TestMethodNameDifference;
import io.wcm.qa.glnm.differences.generic.LayeredDifferences;
import io.wcm.qa.glnm.differences.generic.SortedDifferences;

/**
 * Used to generate test names from Differences. Uses class, package and test device information. Allows additional
 * differences.
 */
public class TestNameDifferences implements Differences {

  private SortedDifferences additionalDifferences = new SortedDifferences();
  private ClassDifferences classDifferences;
  private int classNameMaxLength = -1;
  private TestDeviceDifferences deviceDifferences;

  /**
   * Constructor.
   */
  public TestNameDifferences() {
    getAdditionalDifferences().add(new TestMethodNameDifference());
  }

  /**
   * Will be appended to end of test name.
   * @param difference to add
   * @return true if this collection changed as a result of the call
   */
  public boolean addAdditionalDifference(Difference difference) {
    return getAdditionalDifferences().add(difference);
  }

  /**
   * Will be appended to end of test name.
   * @param toBeAppended multiple differences to add
   * @return true if this collection changed as a result of the call
   */
  public boolean addAdditionalDifferences(Differences toBeAppended) {
    return getAdditionalDifferences().addAll(toBeAppended);
  }

  @Override
  public String asFilePath() {
    return getCombinedDifferences().asFilePath();
  }

  @Override
  public String asPropertyKey() {
    return getCombinedDifferences().asPropertyKey();
  }

  public int getClassNameMaxLength() {
    return classNameMaxLength;
  }

  @Override
  public Iterator<Difference> iterator() {
    return getCombinedDifferences().iterator();
  }

  /**
   * Package and class name will be used in test name.
   * @param testClass to use for class and package name
   */
  public void setClass(Class testClass) {
    setClassDifferences(new ClassDifferences(testClass));
  }

  /**
   * @param maxLength to restrict class name part of test name to
   */
  public void setClassNameMaxLength(int maxLength) {
    classNameMaxLength = maxLength;
    if (getClassDifferences() != null && maxLength > 0) {
      getClassDifferences().setClassNameMaxLength(maxLength);
    }
  }

  /**
   * Name part from package will be relative to root package.
   * @param rootPackage package name to use in relativization
   */
  public void setRootPackage(String rootPackage) {
    getClassDifferences().setRootPackage(rootPackage);
  }

  /**
   * Browser and viewport width are added to middle of test name.
   * @param device to use for differences
   */
  public void setTestDevice(TestDevice device) {
    setDeviceDifferences(new TestDeviceDifferences(device));
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("TestName(class=");
    stringBuilder.append(getClassDifferences().toString());
    stringBuilder.append("|device=");
    stringBuilder.append(getDeviceDifferences().toString());
    stringBuilder.append("|additional=");
    stringBuilder.append(getAdditionalDifferences().toString());
    stringBuilder.append(")");
    return stringBuilder.toString();
  }

  private SortedDifferences getAdditionalDifferences() {
    return additionalDifferences;
  }

  private ClassDifferences getClassDifferences() {
    if (classDifferences == null) {
      setClass(ITest.class);
    }
    return classDifferences;
  }

  private Differences getCombinedDifferences() {
    LayeredDifferences combined = new LayeredDifferences();
    combined.setPrimary(getClassDifferences());
    combined.setSecondary(getDeviceDifferences());
    combined.setTertiary(getAdditionalDifferences());
    return combined;
  }

  private TestDeviceDifferences getDeviceDifferences() {
    if (deviceDifferences == null) {
      deviceDifferences = new TestDeviceDifferences();
    }
    return deviceDifferences;
  }

  private void setClassDifferences(ClassDifferences classDifferences) {
    if (getClassNameMaxLength() > 0) {
      classDifferences.setClassNameMaxLength(getClassNameMaxLength());
    }
    this.classDifferences = classDifferences;
  }

  private void setDeviceDifferences(TestDeviceDifferences deviceDifferences) {
    this.deviceDifferences = deviceDifferences;
  }
}
