<#assign classNameForSpec = className(spec) />
<#assign packageNameForSpec = packageName(spec) />
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
package ${packageNameForSpec};

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFromLocator;
import io.wcm.qa.galenium.selectors.SelectorFactory;

/**
 * Java version of '${spec.filename}'. 
 */
public class ${classNameForSpec} {

  private static final File specFile = new File("${spec.getSpecFile().getPath()}");

  <#list spec.tags as tag>
    public ${classNameForSpec} with${className(tag)}() {

    }
  </#list>
}
