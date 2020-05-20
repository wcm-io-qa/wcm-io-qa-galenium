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

import java.util.function.Function;
import java.util.function.Supplier;

import org.hamcrest.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.persistence.SamplePersistence;

abstract class BaselineMatcher<M, S> extends DifferentiatingMatcherBase<M> {

  private static final Logger LOG = LoggerFactory.getLogger(BaselineMatcher.class);
  private Function<M, S> baselineTransformer;
  private Supplier<SamplePersistence<S>> persistenceSupplier;

  BaselineMatcher(
      Differences differences,
      Supplier<SamplePersistence<S>> persistenceSupplier,
      Function<M, S> baselineTransformer) {
    getDifferences().addAll(differences);
    setPersistenceSupplier(persistenceSupplier);
    setBaselineTransformer(baselineTransformer);
  }

  BaselineMatcher(
      Supplier<SamplePersistence<S>> persistenceSupplier,
      Function<M, S> transformer) {
    this(new MutableDifferences(), persistenceSupplier, transformer);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    describeBaseline(description);
  }

  protected void describeBaseline(Description description) {
    description.appendText("baseline with key '");
    description.appendText(getDifferences().getKey());
    description.appendText("': ");
    description.appendValue(baseline());
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
    return getPersistenceSupplier().get();
  }

  protected Supplier<SamplePersistence<S>> getPersistenceSupplier() {
    return persistenceSupplier;
  }

  protected abstract boolean matchesBaseline(M item);

  @Override
  protected boolean matchesDifferentiated(M item) {
    if (matchesBaseline(item)) {
      return true;
    }
    persist(toBaselineType(item));
    return false;
  }

  protected void setBaselineTransformer(Function<M, S> baselineTransformer) {
    this.baselineTransformer = baselineTransformer;
  }

  protected void setPersistenceSupplier(Supplier<SamplePersistence<S>> persistenceSupplier) {
    this.persistenceSupplier = persistenceSupplier;
  }

  protected S toBaselineType(M item) {
    return getBaselineTransformer().apply(item);
  }
}
