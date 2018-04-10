<#macro innerClass selector>
  /**
   * Java version of '${selector.elementName()}' from ${spec.filename}. 
   */
  public static class ${className(selector)} extends SelectorFromLocator {

    /**
     * Constructor.
     */
    public ${className(selector)}() {
      super("${selector.elementName()}", <@newLocator selector.asLocator() />);    
    }
  
    <#list selector.children as selector>
      <@constant selector />
    </#list>
  
    <#list selector.children as selector>
      <@innerClass selector />
    </#list>
  }
</#macro>
<#macro constant selector>
  /** ${className(selector)}: '${escapeXml(selector.asString())}' */
  public static final className(selector) ${constantName(selector)} = new ${className(selector)}();
</#macro>
<#macro newLocator locator>
new Locator("${locator.locatorType}", "${locator.locatorValue}", ${locator.index})
     <#if locator.parent??>
     .withParent(<@newLocator locator.parent />)
     </#if>
     <#if locator.corrections??>
     .withCorrections(<@correctionsRect locator.corrections />)
     </#if>
</#macro>
<#macro correctionsRect corrections>
   new CorrectionsRect(<@correction corrections.top />, <@correction corrections.left />, <@correction corrections.width />, <@correction corrections.height />)
</#macro>
<#macro correction correction>
   new Correction(${correction.value}, ${correction.type})
</#macro>
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
package ${packageName(spec)};

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFromLocator;
import io.wcm.qa.galenium.selectors.SelectorFactory;

/**
 * Java version of '${this.elementName()}' from ${spec.filename}. 
 */
public class ${className(this)} extends SelectorFromLocator {

  /** Special constant to access ${className(this)} as it is a root element. */
  public static final SelectorFromLocator SELF = new ${className(this)}();

  /**
   * Constructor.
   */
  public ${className(this)}() {
    super("${this.elementName()}", <@newLocator this.asLocator() />);    
  }

  <#list this.children as selector>
    <@constant selector />
  </#list>

  <#list this.children as selector>
    <@innerClass selector />
  </#list>

}
