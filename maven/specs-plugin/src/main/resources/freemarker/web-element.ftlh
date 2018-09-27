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
    List<WebElement> elements = findElements(${selectorClassName(selector)}.${constantName(child)}.asRelative().asBy());
    List<${gweClassName(child)}> galeniumElements = new java.util.ArrayList<${gweClassName(child)}>();
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

import io.wcm.qa.galenium.selectors.base.Selector;
import io.wcm.qa.galenium.selectors.base.SelectorFactory;
import io.wcm.qa.galenium.interaction.webelement.GaleniumWebElementBase;

import java.util.List;

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
    List<WebElement> elements = findElements(${selectorClassName(this)}.${constantName(child)}.asRelative().asBy());
    List<${gweClassName(child)}> galeniumElements = new java.util.ArrayList<${gweClassName(child)}>();
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

}
