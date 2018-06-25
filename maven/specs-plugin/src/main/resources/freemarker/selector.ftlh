<#macro innerClass selector parentConstantName>
  /**
   * Java version of '${selector.elementName()}' from ${spec.filename}. 
   */
  public static class ${className(selector)} extends SelectorFromLocator {

    <#list selector.children as child>
      <@constant child />
    </#list>
  
    /**
     * Constructor.
     */
    public ${className(selector)}() {
      super("${selector.elementName()}", <@newLocator selector.asRelative().asLocator() />);
      setParent(${parentConstantName});    
      setBy(By.cssSelector("${escapeJava(selector.asString())}"));    
    }
    
    <#list interaction.methods as method>
       ${method.head} {
         ${method.body}
       }
    </#list>
  
    <#list selector.children as child>
      <@innerClass child constantName(selector) />
    </#list>
    
  }
</#macro>
<#macro constant selector>
  /** ${className(selector)}: '${escapeXml(selector.asString())}' */
  public static final ${className(selector)} ${constantName(selector)} = new ${className(selector)}();
</#macro>
<#macro newLocator locator>
new Locator("${locator.locatorType}", "${escapeJava(locator.locatorValue)}", ${locator.index})
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
package ${packageName(spec)};

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFromLocator;
import io.wcm.qa.galenium.selectors.SelectorFactory;

import org.openqa.selenium.By;

/**
 * Java version of '${this.elementName()}' from ${spec.filename}.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public class ${className(this)} extends SelectorFromLocator {

  /** Special constant to access ${className(this)} as it is a root element. */
  public static final ${className(this)} SELF = new ${className(this)}();
  <#list this.children as child>
    <@constant child />
  </#list>

  /**
   * Constructor.
   */
  public ${className(this)}() {
    super("${this.elementName()}", <@newLocator this.asRelative().asLocator() />);
    setBy(By.cssSelector("${escapeJava(this.asString())}"));    
  }

  <#list this.children as child>
    <@innerClass child "SELF" />
  </#list>
  
  <#list interaction.methods as method>
     ${method.head} {
       ${method.body}
     }
  </#list>

}
