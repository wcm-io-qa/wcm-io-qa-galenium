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
package io.wcm.qa.glnm.listeners;

import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

import io.wcm.qa.glnm.persistence.util.TextSampleManager;

/**
 * Handles persisting text samples at the end of test run.
 */
public class TextSamplePersistenceListener extends TestListenerAdapter {

  @Override
  public void onFinish(ITestContext testContext) {
    TextSampleManager.persistNewTextSamples();
  }

}
