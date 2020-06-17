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

import java.util.List;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.params.support.AnnotationConsumer;

import io.wcm.qa.glnm.junit.combinatorial.Combinable;
import io.wcm.qa.glnm.junit.combinatorial.CombinableProvider;

class ViewportWidthProvider implements
    CombinableProvider,
    AnnotationConsumer<ViewportWidths> {

  private int[] widths;

  /** {@inheritDoc} */
  @Override
  public List<Combinable> combinables() {
    return stream(widths).boxed()
        .map(ViewportWidthExtension::new)
        .map(Combinable::new)
        .collect(toList());
  }

  /** {@inheritDoc} */
  @Override
  public Class providedType() {
    return Extension.class;
  }

  /** {@inheritDoc} */
  @Override
  public void accept(ViewportWidths t) {
    widths = t.value();
  }

}
