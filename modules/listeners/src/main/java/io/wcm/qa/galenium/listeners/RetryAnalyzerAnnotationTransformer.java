/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * Sets {@link io.wcm.qa.galenium.listeners.RetryAnalyzer} on test classes to facilitate retries without too much boiler plate.
 *
 * @since 1.0.0
 */
public class RetryAnalyzerAnnotationTransformer implements IAnnotationTransformer {

  /** {@inheritDoc} */
  @Override
  public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    if (annotation.getRetryAnalyzer() == null) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }

}
