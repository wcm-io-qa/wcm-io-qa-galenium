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
package io.wcm.qa.glnm.persistence;

import java.util.NoSuchElementException;

import io.wcm.qa.glnm.differences.base.Differences;


class IntegerPersistence extends SamplePersistenceBase<Integer> {

  IntegerPersistence(Class clazz) {
    super(clazz);
  }

  /** {@inheritDoc} */
  @Override
  public Integer loadFromBaseline(Differences key) {
    try {
      return baseline().getInteger(keyWithContextDifferences(key), Integer.valueOf(0));
    }
    catch (NoSuchElementException ex) {
      return null;
    }
  }

  /** {@inheritDoc} */
  @Override
  public void storeToBaseline(Differences key, Integer sample) {
    super.storeToBaseline(key, sample);
  }

}
