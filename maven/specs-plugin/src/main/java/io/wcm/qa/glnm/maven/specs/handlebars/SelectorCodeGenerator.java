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
package io.wcm.qa.glnm.maven.specs.handlebars;

import com.github.jknack.handlebars.Context;

import io.wcm.qa.glnm.maven.specs.pojo.SelectorPojo;
import io.wcm.qa.glnm.maven.specs.util.FormatUtil;

public class SelectorCodeGenerator extends CodeGenerator {

  private SelectorPojo selector;

  public SelectorCodeGenerator(SelectorPojo selector) {
    super("selector");
    this.setSelector(selector);
  }

  public SelectorPojo getSelector() {
    return selector;
  }

  @Override
  protected String getRootPackageName() {
    return super.getRootPackageName();
  }

  public void setSelector(SelectorPojo selector) {
    this.selector = selector;
  }

  @Override
  protected Context getContext() {
    Context newContext = Context.newContext(getSelector());
    newContext.combine("rootPackageName",
        getRootPackageName());
    newContext.combine("packageName",
        FormatUtil.getSelectorsPackageName(
            getRootPackageName(),
            getSelector().getSpec()));
    return newContext;
  }

}
