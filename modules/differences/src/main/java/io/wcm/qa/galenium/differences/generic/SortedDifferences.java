/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import io.wcm.qa.galenium.differences.base.Difference;

/**
 * Differences are stored in a sorted way to get a consistent hierarchy.
 *
 * @since 1.0.0
 */
public class SortedDifferences extends MutableDifferences {

  private static final DifferenceNameComparator DEFAULT_DIFFERENCE_COMPARATOR = new DifferenceNameComparator();
  private Comparator<Difference> comparator;
  private SortedSet<Difference> differences;

  /**
   * Use default comparator.
   */
  public SortedDifferences() {
    this(DEFAULT_DIFFERENCE_COMPARATOR);
  }

  /**
   * Use default comparator.
   *
   * @param comparator a {@link java.util.Comparator} object.
   */
  public SortedDifferences(Comparator<Difference> comparator) {
    setDifferences(new TreeSet<Difference>(comparator));
    setComparator(comparator);
  }

  /**
   * <p>Getter for the field <code>comparator</code>.</p>
   *
   * @return the configured comparator or {@link io.wcm.qa.galenium.differences.generic.DifferenceNameComparator} if none is set
   */
  public Comparator<Difference> getComparator() {
    if (comparator == null) {
      return DEFAULT_DIFFERENCE_COMPARATOR;
    }
    return comparator;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<Difference> getDifferences() {
    return differences;
  }

  /**
   * Set a new comparator and reorder already existing differences.
   *
   * @param comparator to use from now on
   */
  public void setComparator(Comparator<Difference> comparator) {
    this.comparator = comparator;
    TreeSet<Difference> newDifferences = new TreeSet<Difference>(comparator);
    newDifferences.addAll(getDifferences());
    setDifferences(newDifferences);
  }

  private void setDifferences(SortedSet<Difference> differences) {
    this.differences = differences;
  }
}
