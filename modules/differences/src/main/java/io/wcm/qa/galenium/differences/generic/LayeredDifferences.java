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
package io.wcm.qa.galenium.differences.generic;

import java.util.Iterator;

import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.differences.base.Differences;

/**
 * Three layers of differences.
 *
 * @since 1.0.0
 */
public class LayeredDifferences implements Differences {

  private Differences primary;
  private Differences secondary;
  private Differences tertiary;

  /** {@inheritDoc} */
  @Override
  public String asFilePath() {
    return getCombinedDifferences().asFilePath();
  }

  /** {@inheritDoc} */
  @Override
  public String asPropertyKey() {
    return getCombinedDifferences().asPropertyKey();
  }

  /**
   * <p>getCombinedDifferences.</p>
   *
   * @return all three layers as one {@link io.wcm.qa.galenium.differences.base.Differences} object
   * @since 2.0.0
   */
  public Differences getCombinedDifferences() {
    MutableDifferences mutableDifferences = new MutableDifferences();
    mutableDifferences.addAll(getPrimary());
    mutableDifferences.addAll(getSecondary());
    mutableDifferences.addAll(getTertiary());
    return mutableDifferences;
  }

  /**
   * <p>Getter for the field <code>primary</code>.</p>
   *
   * @return a {@link io.wcm.qa.galenium.differences.base.Differences} object.
   * @since 2.0.0
   */
  public Differences getPrimary() {
    return primary;
  }

  /**
   * <p>Getter for the field <code>secondary</code>.</p>
   *
   * @return a {@link io.wcm.qa.galenium.differences.base.Differences} object.
   * @since 2.0.0
   */
  public Differences getSecondary() {
    return secondary;
  }

  /**
   * <p>Getter for the field <code>tertiary</code>.</p>
   *
   * @return a {@link io.wcm.qa.galenium.differences.base.Differences} object.
   * @since 2.0.0
   */
  public Differences getTertiary() {
    return tertiary;
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<Difference> iterator() {
    return getCombinedDifferences().iterator();
  }

  /**
   * <p>Setter for the field <code>primary</code>.</p>
   *
   * @param primary a {@link io.wcm.qa.galenium.differences.base.Differences} object.
   * @since 2.0.0
   */
  public void setPrimary(Differences primary) {
    this.primary = primary;
  }

  /**
   * <p>Setter for the field <code>secondary</code>.</p>
   *
   * @param secondary a {@link io.wcm.qa.galenium.differences.base.Differences} object.
   * @since 2.0.0
   */
  public void setSecondary(Differences secondary) {
    this.secondary = secondary;
  }

  /**
   * <p>Setter for the field <code>tertiary</code>.</p>
   *
   * @param tertiary a {@link io.wcm.qa.galenium.differences.base.Differences} object.
   * @since 2.0.0
   */
  public void setTertiary(Differences tertiary) {
    this.tertiary = tertiary;
  }

  /** {@inheritDoc} */
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
