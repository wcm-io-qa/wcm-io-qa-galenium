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
import com.github.jknack.handlebars.Template;

public abstract class CodeGenerator {

  private String templateName;
  private String packageName = "";

  protected CodeGenerator(String templateName) {
    this.templateName = templateName;
  }

  /**
   * Generate the code.
   * @return the generated code.
   */
  public String generate() {
    Template template = getTemplate();
    Context context = getContext();
    return HandleBarsUtil.generateSourceCode(template, context);
  }

  protected Template getTemplate() {
    return HandleBarsUtil.compileTemplate(templateName, getClass().getPackage());
  }

  protected abstract Context getContext();

  protected String getTemplateName() {
    return templateName;
  }

  protected String getRootPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

}
