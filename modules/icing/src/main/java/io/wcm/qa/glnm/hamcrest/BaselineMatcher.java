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
import java.util.function.Function;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.persistence.SamplePersistence;

abstract class BaselineMatcher<M, S> extends TypeSafeMatcher<M>
    implements DifferentiatingMatcher<M> {

  private static final Logger LOG = LoggerFactory.getLogger(BaselineMatcher.class);
  private Function<M, S> baselineTransformer;
  private MutableDifferences differences = new MutableDifferences();
  private Function<Class, SamplePersistence<S>> persistenceProducer;

  BaselineMatcher(
      Differences differences,
      Function<Class, SamplePersistence<S>> persistenceProducer,
      Function<M, S> baselineTransformer) {
    getDifferences().addAll(differences);
    setPersistenceProducer(persistenceProducer);
    setBaselineTransformer(baselineTransformer);
  }

  BaselineMatcher(
      Function<Class, SamplePersistence<S>> persistenceProducer,
      Function<M, S> transformer) {
    this(new MutableDifferences(), persistenceProducer, transformer);
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

  private MutableDifferences getDifferences() {
    return differences;
  }

  private void persist(S item) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("persisting: " + item);
    }
    getPersistence().storeToBaseline(getDifferences(), item);
  }

  protected S baseline() {
    return getPersistence().loadFromBaseline(getDifferences());
  }

  protected Function<M, S> getBaselineTransformer() {
    return baselineTransformer;
  }

  protected SamplePersistence<S> getPersistence() {
    return getPersistenceSupplier().apply(getResourceClass());
  }

  protected Function<Class, SamplePersistence<S>> getPersistenceSupplier() {
    return persistenceProducer;
  }

  protected Class getResourceClass() {
    return getClass();
  }

  protected boolean matchesBaseline(S item) {
    return item.equals(baseline());
  }

  @Override
  protected boolean matchesSafely(M item) {
    if (matchesBaseline(toBaselineType(item))) {
      return true;
    }
    persist(toBaselineType(item));
    return false;
  }

  protected void setBaselineTransformer(Function<M, S> baselineTransformer) {
    this.baselineTransformer = baselineTransformer;
  }

  protected void setDifferences(MutableDifferences differences) {
    this.differences = differences;
  }

  protected void setPersistenceProducer(Function<Class, SamplePersistence<S>> persistenceProducer) {
    this.persistenceProducer = persistenceProducer;
  }

  protected S toBaselineType(M item) {
    return getBaselineTransformer().apply(item);
  }
}
