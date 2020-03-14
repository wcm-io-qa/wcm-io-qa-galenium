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

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


/**
 * <p>
 * Will persist samples for new baseline based on current test run.
 * </p>
 *
 * @since 5.0.0
 */
public class BaselinePersistenceExtension implements AfterAllCallback {

  /**
   * {@inheritDoc}
   *
   * Persist all samples after all tests were run.
   */
  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    PersistingCacheUtil.persistNewBaseline();
  }

}
