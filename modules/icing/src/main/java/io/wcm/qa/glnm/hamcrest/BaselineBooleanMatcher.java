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

import io.wcm.qa.glnm.persistence.Persistence;
import io.wcm.qa.glnm.persistence.SamplePersistence;

final class BaselineBooleanMatcher extends BaselineDirectMatcher<Boolean> {

  @Override
  protected SamplePersistence<Boolean> getPersistence() {
    return Persistence.forBoolean(getClass());
  }
}
