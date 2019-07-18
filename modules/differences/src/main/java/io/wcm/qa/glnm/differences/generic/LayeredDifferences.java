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
package io.wcm.qa.glnm.differences.generic;

import java.util.Iterator;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;

/**
 * Three layers of differences.
 */
public class LayeredDifferences implements Differences {

  private Differences primary;
  private Differences secondary;
  private Differences tertiary;

  @Override
  public String asFilePath() {
    return getCombinedDifferences().asFilePath();
  }

  @Override
  public String asPropertyKey() {
    return getCombinedDifferences().asPropertyKey();
  }

  /**
   * @return all three layers as one {@link Differences} object
   */
  public Differences getCombinedDifferences() {
    MutableDifferences mutableDifferences = new MutableDifferences();
    mutableDifferences.addAll(getPrimary());
    mutableDifferences.addAll(getSecondary());
    mutableDifferences.addAll(getTertiary());
    return mutableDifferences;
  }

  public Differences getPrimary() {
    return primary;
  }

  public Differences getSecondary() {
    return secondary;
  }

  public Differences getTertiary() {
    return tertiary;
  }

  @Override
  public Iterator<Difference> iterator() {
    return getCombinedDifferences().iterator();
  }

  public void setPrimary(Differences primary) {
    this.primary = primary;
  }

  public void setSecondary(Differences secondary) {
    this.secondary = secondary;
  }

  public void setTertiary(Differences tertiary) {
    this.tertiary = tertiary;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(primary=");
    stringBuilder.append(getPrimary());
    stringBuilder.append("|secondary=");
    stringBuilder.append(getSecondary());
    stringBuilder.append("|tertiary=");
    stringBuilder.append(getTertiary());
    return stringBuilder.toString();
  }
}
