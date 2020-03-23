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

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.wcm.qa.glnm.context.GaleniumContext;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.difference.StringDifference;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.differences.specialized.ClassDifferences;


/**
 * <p>
 * Will persist samples for new baseline based on current test run.
 * </p>
 *
 * @since 5.0.0
 */
public class BaselinePersistenceExtension implements AfterAllCallback, BeforeEachCallback {

  private static final String TEST_DIFFERENCES = "testDifferences";

  /**
   * {@inheritDoc}
   *
   * Persist all samples after all tests were run.
   */
  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    PersistingCacheUtil.persistNewBaseline();
  }

  /**
   * {@inheritDoc}
   *
   * Add context relative Differences.
   */
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Class<?> requiredTestClass = context.getRequiredTestClass();
    ClassDifferences classDifferences = new ClassDifferences(requiredTestClass);
    Method requiredTestMethod = context.getRequiredTestMethod();
    StringDifference methodDifference = new StringDifference(requiredTestMethod.getName());
    MutableDifferences differences = new MutableDifferences();
    differences.addAll(classDifferences);
    differences.add(methodDifference);
    GaleniumContext.put(TEST_DIFFERENCES, differences);
  }

  /**
   * Context relative differences make the samples local to one test method.
   *
   * @return a {@link io.wcm.qa.glnm.differences.base.Differences} object.
   */
  public static Differences getContextDifferences() {
    return (Differences)GaleniumContext.get(TEST_DIFFERENCES);
  }
}
