/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.hamcrest;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.hamcrest.TypeSafeMatcher;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;

abstract class DifferentiatingMatcherBase<T> extends TypeSafeMatcher<T>
    implements DifferentiatingMatcher<T> {

  private MutableDifferences differences = new MutableDifferences();

  /** {@inheritDoc} */
  @Override
  public void add(Difference difference) {
    getDifferences().add(difference);
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return getDifferences().getKey();
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  /** {@inheritDoc} */
  @Override
  public void prepend(Difference difference) {
    MutableDifferences newDifferences = new MutableDifferences();
    newDifferences.add(difference);
    newDifferences.addAll(getDifferences());
    setDifferences(newDifferences);
  }

  protected MutableDifferences getDifferences() {
    return differences;
  }

  protected abstract boolean matchesDifferentiated(T item);

  @Override
  protected boolean matchesSafely(T item) {
    differentiate(item);
    return matchesDifferentiated(item);
  }

  protected void differentiate(T item) {
    getDifferences().addAll(differencesFor(item));
  }

  /**
   * @param item to generate optional differences from
   */
  protected Collection<? extends Difference> differencesFor(T item) {
    return Collections.emptyList();
  }

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

}
