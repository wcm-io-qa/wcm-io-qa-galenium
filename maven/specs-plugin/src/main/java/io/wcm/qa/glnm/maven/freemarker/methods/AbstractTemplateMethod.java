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
package io.wcm.qa.glnm.maven.freemarker.methods;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;

/**
 * Abstract base class for implentations of template methods.
 * @param <S>
 */
public abstract class AbstractTemplateMethod<S> implements TemplateMethodModelEx {

  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments == null || arguments.isEmpty()) {
      throw new GaleniumException(this.getClass().getSimpleName() + " needs selector.");
    }
    TemplateModel model = (TemplateModel)arguments.get(0);
    Object unwrapped = DeepUnwrap.unwrap(model);
    if (unwrapped == null) {
      throw new GaleniumException("unwrapped object to null.");
    }
    try {
      return attemptExec(unwrapped);
    }
    catch (ClassCastException ex) {
      GaleniumReportUtil.getLogger().warn("found class: " + unwrapped.getClass());
      throw new GaleniumException("argument could not be unwrapped.", ex);
    }
  }

  @SuppressWarnings("unchecked")
  protected Object attemptExec(Object unwrapped) {
    return exec((S)unwrapped);
  }

  protected abstract String exec(S input);

}
