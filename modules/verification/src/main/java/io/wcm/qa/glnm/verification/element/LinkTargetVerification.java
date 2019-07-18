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
package io.wcm.qa.glnm.verification.element;

import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Make sure a link has a certain href value.
 */
public class LinkTargetVerification extends AttributeVerification {

  private static final String ATTRIBUTE_NAME_HREF = "href";

  /**
   * @param selector to identify element
   */
  public LinkTargetVerification(Selector selector) {
    super(selector, ATTRIBUTE_NAME_HREF);
  }

  /**
   * @param selector to identify element
   * @param expectedValue to verify against
   */
  public LinkTargetVerification(Selector selector, String expectedValue) {
    super(selector, ATTRIBUTE_NAME_HREF, expectedValue);
  }

}
