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

import static io.wcm.qa.glnm.galen.util.GalenHelperUtil.resizeViewport;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;


/**
 * <p>
 * Sets viewport on current browser before test execution.
 * </p>
 * @since 5.0.0
 */
class ViewportWidthExtension implements BeforeTestExecutionCallback {

  private final int width;

  /**
   * <p>
   * Constructor.
   * </p>
   * @param width to resize viewport to
   */
  ViewportWidthExtension(int width) {
    this.width = width;
  }

  /** {@inheritDoc} */
  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    resizeViewport(width);
  }

}
