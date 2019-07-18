<#macro innerClass selector>
  /**
   * Galenium webelement version of '${selector.elementName()}' from ${spec.filename}. 
   */
  public static class ${className(selector)}Gwe extends GaleniumWebElementBase {

    /**
     * Constructor.
     */
    public ${className(selector)}Gwe(WebElement element) {
      super(element);
    }

    <#list selector.children as child>
      <@innerClass child />
    </#list>

    <#list selector.children as child>


  public ${gweClassName(child)} get${className(child)}() {
    WebElement element = findElement(${selectorClassName(selector)}.${constantName(child)}.asRelative().asBy());
    return new ${gweClassName(child)}(element);
  }

  public ${gweClassName(child)} getNth${className(child)}(int index) {
    return get${className(child)}s().get(index);
  }

  public java.util.List<${gweClassName(child)}> get${className(child)}s() {
    java.util.List<WebElement> elements = findElements(${selectorClassName(selector)}.${constantName(child)}.asRelative().asBy());
    java.util.List<${gweClassName(child)}> galeniumElements = new java.util.ArrayList<${gweClassName(child)}>();
    for (WebElement webElement : elements) {
      galeniumElements.add(new ${gweClassName(child)}(webElement));
    }
    return galeniumElements;
  }
    </#list>

    
  }
</#macro>
package ${packageName(spec)};

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;

import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;
import io.wcm.qa.glnm.interaction.webelement.GaleniumWebElementBase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Galenium webelement version of '${this.elementName()}' from ${spec.filename}.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public class ${className(this)}Gwe extends GaleniumWebElementBase {

  /**
   * Constructor.
   */
  public ${className(this)}Gwe(WebElement element) {
    super(element);
  }

  <#list this.children as child>
    <@innerClass child />
  </#list>
  
  <#list this.children as child>

  public ${gweClassName(child)} get${className(child)}() {
    WebElement element = findElement(${selectorClassName(this)}.${constantName(child)}.asRelative().asBy());
    return new ${gweClassName(child)}(element);
  }

  public ${gweClassName(child)} getNth${className(child)}(int index) {
    return get${className(child)}s().get(index);
  }

  public java.util.List<${gweClassName(child)}> get${className(child)}s() {
    java.util.List<WebElement> elements = findElements(${selectorClassName(this)}.${constantName(child)}.asRelative().asBy());
    java.util.List<${gweClassName(child)}> galeniumElements = new java.util.ArrayList<${gweClassName(child)}>();
    for (WebElement webElement : elements) {
      galeniumElements.add(new ${gweClassName(child)}(webElement));
    }
    return galeniumElements;
  }
  </#list>

  public static ${gweClassName(this)} instance() {
    WebElement initialElement = ${selectorClassName(this)}.SELF.find();
    return new ${gweClassName(this)}(initialElement);
  } 

  public static ${gweClassName(this)} instance(int index) {
    WebElement initialElement = ${selectorClassName(this)}.SELF.findNth(index);
    return new ${gweClassName(this)}(initialElement);
  } 

  public static java.util.List<${gweClassName(this)}> instances() {
    java.util.List<WebElement> initialElements = ${selectorClassName(this)}.SELF.findAll();
    java.util.List<${gweClassName(this)}> galeniumElements = new java.util.ArrayList<${gweClassName(this)}>();
    for (WebElement webElement : initialElements) {
      galeniumElements.add(new ${gweClassName(this)}(webElement));
    }
    return galeniumElements;
  } 

}
