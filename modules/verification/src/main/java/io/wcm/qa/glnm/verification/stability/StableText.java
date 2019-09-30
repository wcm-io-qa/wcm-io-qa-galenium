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
package io.wcm.qa.glnm.verification.stability;

import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.glnm.sampling.element.TextSampler;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.base.Verifiable;


/**
 * Verifies stable text of element. Useful when waiting for animated text changes of elements to finish.
 */
public class StableText extends Stability<String> implements Verifiable {

  /**
   * Constructor.
   * @param selector to check text of
   */
  public StableText(Selector selector) {
    super(new TextSampler(selector));
  }

  @Override
  protected boolean checkForEquality(String oldValue, String newValue) {
    return StringUtils.equals(oldValue, newValue);
  }

}
