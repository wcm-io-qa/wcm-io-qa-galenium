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
import org.hamcrest.Matchers;

import io.wcm.qa.glnm.persistence.SamplePersistence;

class BaselineDirectMatcher<T> extends BaseliningMatcher<T, T> {

  BaselineDirectMatcher(Supplier<SamplePersistence<T>> persistenceSupplier) {
    super(Matchers::is, persistenceSupplier, Function.identity());
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    describeBaseline(description);
  }

}
