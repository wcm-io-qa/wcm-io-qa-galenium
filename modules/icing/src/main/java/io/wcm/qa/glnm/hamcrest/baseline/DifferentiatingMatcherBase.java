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
package io.wcm.qa.glnm.hamcrest.baseline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.hamcrest.AllureAwareMatcher;

abstract class DifferentiatingMatcherBase<T> extends AllureAwareMatcher<T>
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

  private String strippedClassName() {
    String simpleName = getClass().getSimpleName();
    if (StringUtils.isEmpty(simpleName)) {
      simpleName = getClass().getSuperclass().getSimpleName();
    }
    String strippedName = StringUtils.removeEnd(simpleName, "Matcher");
    strippedName = StringUtils.removeStart(strippedName, "Baseline");
    return strippedName;
  }

  /**
   * @param item to generate optional differences from
   */
  protected Collection<? extends Difference> differencesFor(T item) {
    return new ArrayList<Difference>();
  }

  protected void differentiate(T item) {
    String strippedName = strippedClassName();
    getDifferences().add(new StringDifference(strippedName.toLowerCase()));
    getDifferences().addAll(differencesFor(item));
  }

  protected MutableDifferences getDifferences() {
    return differences;
  }

  protected abstract boolean matchesDifferentiated(T item);

  @Override
  protected boolean matchesWithReporting(T item) {
    differentiate(item);
    return matchesDifferentiated(item);
  }

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

}
