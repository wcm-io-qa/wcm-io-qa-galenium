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


import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.params.support.AnnotationConsumer;

import io.wcm.qa.glnm.junit.combinatorial.Combinable;
import io.wcm.qa.glnm.junit.combinatorial.CombinableProvider;

abstract class AbstractConsumingCombinableProvider<
    T,
    A extends Annotation,
    E extends Extension>
    implements
    CombinableProvider,
    AnnotationConsumer<A> {

  private T[] values;

  /** {@inheritDoc} */
  @Override
  public void accept(A t) {
    setValues(valuesFromAnnotation(t));
  }

  /** {@inheritDoc} */
  @Override
  public List<Combinable> combinables() {
    return stream(getValues())
        .map(extensionProducer())
        .map(Combinable::new)
        .collect(toList());
  }

  /** {@inheritDoc} */
  @Override
  public Class providedType() {
    return Extension.class;
  }

  protected abstract Function<T, E> extensionProducer();

  protected T[] getValues() {
    return values;
  }

  protected void setValues(T[] values) {
    this.values = values;
  }

  protected abstract T[] valuesFromAnnotation(A t);

}
