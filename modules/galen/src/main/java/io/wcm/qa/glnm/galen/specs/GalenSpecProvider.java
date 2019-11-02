/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.galen.specs;

import com.galenframework.specs.page.PageSpec;

/**
 * Provides a {@link com.galenframework.specs.page.PageSpec} from either a file or constructed in code.
 *
 * @since 4.0.0
 */
public interface GalenSpecProvider {

  /**
   * <p>getPageSpec.</p>
   *
   * @return an executable spec
   */
  PageSpec getPageSpec();

}