<#macro innerClass selector parentConstantName>
  /**
   * Java version of '${selector.elementName()}' from ${spec.filename}. 
   */
  public static class ${className(selector)} extends ${interactiveSelectorBaseClassName} {

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

    <#list selector.children as child>
      <@innerClass child constantName(selector) />
    </#list>

    <#list selector.children as child>

  public WebElement get${className(child)}() {
    return  ${constantName(child)}.find();
  }

  public WebElement getNth${className(child)}(int index) {
    return ${constantName(child)}.findNth(index);
  }

  public java.util.List<WebElement> get${className(child)}s() {
    return ${constantName(child)}.findAll();
  }
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
   new com.galenframework.specs.page.CorrectionsRect.Correction(${correction.value}, com.galenframework.specs.page.CorrectionsRect.Type.${correction.type})
</#macro>
package ${packageName(spec)};

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;

import ${interactiveSelectorPackage}.${interactiveSelectorInterfaceClassName};
import ${interactiveSelectorPackage}.${interactiveSelectorBaseClassName};

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Java version of '${this.elementName()}' from ${spec.filename}.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public class ${className(this)} extends ${interactiveSelectorBaseClassName} {

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
  
  <#list this.children as child>

  public WebElement get${className(child)}() {
    return  ${constantName(child)}.find();
  }

  public WebElement getNth${className(child)}(int index) {
    return ${constantName(child)}.findNth(index);
  }

  public java.util.List<WebElement> get${className(child)}s() {
    return ${constantName(child)}.findAll();
  }
  </#list>


}
