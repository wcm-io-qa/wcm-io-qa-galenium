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

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.differences.base.Differences;
import io.wcm.qa.galenium.differences.generic.LayeredDifferences;
import io.wcm.qa.galenium.differences.generic.SortedDifferences;


public class TestNameDifferences implements Differences {

  private TestDeviceDifferences deviceDifferences;
  private ClassDifferences classDifferences;
  private SortedDifferences additionalDifferences = new SortedDifferences();

  public boolean addAdditionalDifference(Difference difference) {
    return this.getAdditionalDifferences().add(difference);
  }

  public void setRootPackage(String rootPackage) {
    getClassDifferences().setRootPackage(rootPackage);
  }

  public boolean addAdditionalDifferences(Differences toBeAppended) {
    return getAdditionalDifferences().addAll(toBeAppended);
  }

  public void setClass(Class testClass) {
    setClassDifferences(new ClassDifferences(testClass));
  }

  public void setTestDevice(TestDevice device) {
    setDeviceDifferences(new TestDeviceDifferences(device));
  }

  @Override
  public Iterator<Difference> iterator() {
    return getCombinedDifferences().iterator();
  }

  @Override
  public String asFilePath() {
    return getCombinedDifferences().asFilePath();
  }

  @Override
  public String asPropertyKey() {
    return getCombinedDifferences().asPropertyKey();
  }

  private Differences getCombinedDifferences() {
    LayeredDifferences combined = new LayeredDifferences();
    combined.setPrimary(getClassDifferences());
    combined.setSecondary(getDeviceDifferences());
    combined.setTertiary(getAdditionalDifferences());
    return combined;
  }

  private TestDeviceDifferences getDeviceDifferences() {
    return deviceDifferences;
  }

  private void setDeviceDifferences(TestDeviceDifferences deviceDifferences) {
    this.deviceDifferences = deviceDifferences;
  }

  private ClassDifferences getClassDifferences() {
    return classDifferences;
  }

  private void setClassDifferences(ClassDifferences classDifferences) {
    this.classDifferences = classDifferences;
  }

  private SortedDifferences getAdditionalDifferences() {
    return additionalDifferences;
  }

}
