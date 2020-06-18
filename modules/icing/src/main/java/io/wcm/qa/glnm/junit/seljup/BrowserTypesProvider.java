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
package io.wcm.qa.glnm.junit.seljup;

import java.util.function.Function;

import org.junit.jupiter.params.support.AnnotationConsumer;

import io.github.bonigarcia.seljup.BrowserType;
import io.wcm.qa.glnm.junit.combinatorial.CombinableProvider;

/**
 * Provides browser types for combinatorial tests.
 *
 * @since 5.0.0
 */
public class BrowserTypesProvider
    extends AbstractConsumingCombinableProvider<
        BrowserType,
        BrowserTypes,
        BrowserInjectionExtension>
    implements
    CombinableProvider,
    AnnotationConsumer<BrowserTypes> {

  @Override
  protected Function<BrowserType, BrowserInjectionExtension> extensionProducer() {
    return BrowserInjectionExtension::new;
  }

  @Override
  protected BrowserType[] valuesFromAnnotation(BrowserTypes t) {
    return t.value();
  }


}
