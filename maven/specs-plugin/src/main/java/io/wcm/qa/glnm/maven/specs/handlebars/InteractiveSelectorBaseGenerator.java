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

import java.util.Collection;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Context.Builder;

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.maven.specs.pojo.InteractionMethodPojo;
import io.wcm.qa.glnm.maven.specs.pojo.InteractionPojo;

/**
 * Generator for interactive selector base class.
 */
public class InteractiveSelectorBaseGenerator extends CodeGenerator {

  /**
   * Constructor.
   */
  public InteractiveSelectorBaseGenerator() {
    super("selector-base");
  }

  @Override
  protected Context getContext() {
    InteractionPojo interactionPojo = new InteractionPojo(Element.class);
    Builder contextBuilder = Context.newBuilder("model-root");
    Collection<InteractionMethodPojo> methods = interactionPojo.getMethods();
    contextBuilder.combine("methods", methods);
    contextBuilder.combine("packageName", getRootPackageName());
    Context context = contextBuilder.build();
    return context;
  }

}
