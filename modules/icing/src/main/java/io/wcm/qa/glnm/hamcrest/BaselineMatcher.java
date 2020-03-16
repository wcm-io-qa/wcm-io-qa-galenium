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

import java.util.Iterator;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.persistence.SamplePersistence;

abstract class BaselineMatcher<T> extends TypeSafeMatcher<T>
    implements DifferentiatingMatcher<T> {

  private static final Logger LOG = LoggerFactory.getLogger(BaselineMatcher.class);
  private MutableDifferences differences = new MutableDifferences();

  BaselineMatcher() {
    this(new MutableDifferences());
  }

  BaselineMatcher(Differences differences) {
    this.getDifferences().addAll(differences);
  }

  /** {@inheritDoc} */
  @Override
  public void add(Difference difference) {
    getDifferences().add(difference);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    description.appendText("matches baseline with key '");
    description.appendText(getDifferences().getKey());
    description.appendText("': ");
    description.appendValue(baseline());
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

  private T baseline() {
    return getPersistence().loadFromBaseline(getDifferences());
  }

  private MutableDifferences getDifferences() {
    return differences;
  }

  private void persist(T item) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("persisting: " + item);
    }
    getPersistence().storeToBaseline(getDifferences(), item);
  }

  protected abstract SamplePersistence<T> getPersistence();

  protected Class getResourceClass() {
    return getClass();
  }

  protected boolean matchesBaseline(T item) {
    return item.equals(getPersistence().loadFromBaseline(getDifferences()));
  }

  @Override
  protected boolean matchesSafely(T item) {
    if (matchesBaseline(item)) {
      return true;
    }
    persist(item);
    return false;
  }

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }
}
