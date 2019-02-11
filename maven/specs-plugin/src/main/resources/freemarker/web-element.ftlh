<#macro innerClass selector>
  /**
   * Galenium webelement version of '${selector.elementName()}' from ${spec.filename}. 
   */
  public static class ${className(selector)}Gwe extends GaleniumWebElementBase {

    /**
     * Constructor.
     */
    public ${className(selector)}Gwe(WebElement element, IndexedSelector selector) {
      super(element, selector);
    }

    <#list selector.children as child>
      <@innerClass child />
    </#list>

    <#list selector.children as child>


  public ${gweClassName(child)} get${className(child)}() {
    InteractiveSelector original = ${selectorClassName(selector)}.${constantName(child)};
    WebElement element = findElement(SelectorFactory.relativeFromAbsolute(original).asBy());
    return new ${gweClassName(child)}(element, cloneSelector(original, 0));
  }

  public ${gweClassName(child)} getNth${className(child)}(int index) {
    return get${className(child)}s().get(index);
  }

  public java.util.List<${gweClassName(child)}> get${className(child)}s() {
    InteractiveSelector original = ${selectorClassName(selector)}.${constantName(child)};
    java.util.List<WebElement> elements = findElements(SelectorFactory.relativeFromAbsolute(original).asBy());
    java.util.List<${gweClassName(child)}> galeniumElements = new java.util.ArrayList<${gweClassName(child)}>();
    int index = 0;
    for (WebElement webElement : elements) {
      galeniumElements.add(new ${gweClassName(child)}(webElement, cloneSelector(original, index++)));
    }
    return galeniumElements;
  }
    </#list>

  }
</#macro>
package ${packageName(spec)};

import org.openqa.selenium.WebElement;

import io.wcm.qa.galenium.example.selectors.InteractiveSelector;
import io.wcm.qa.galenium.interaction.webelement.GaleniumWebElementBase;
import io.wcm.qa.galenium.selectors.IndexedSelector;
import io.wcm.qa.galenium.selectors.util.SelectorFactory;

/**
 * Galenium webelement version of '${this.elementName()}' from ${spec.filename}.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public class ${className(this)}Gwe extends GaleniumWebElementBase {

  /**
   * Constructor.
   */
  public ${className(this)}Gwe(WebElement element, IndexedSelector selector) {
    super(element, selector);
  }

  <#list this.children as child>
    <@innerClass child />
  </#list>

  <#list this.children as child>

  public ${gweClassName(child)} get${className(child)}() {
    return getNth${className(child)}(0);
  }

  public ${gweClassName(child)} getNth${className(child)}(int index) {
    return get${className(child)}s().get(index);
  }

  public java.util.List<${gweClassName(child)}> get${className(child)}s() {
    InteractiveSelector original = ${selectorClassName(this)}.${constantName(child)};
    java.util.List<WebElement> elements = findElements(SelectorFactory.relativeFromAbsolute(original).asBy());
    java.util.List<${gweClassName(child)}> galeniumElements = new java.util.ArrayList<${gweClassName(child)}>();
    int index = 0;
    for (WebElement webElement : elements) {
      galeniumElements.add(new ${gweClassName(child)}(webElement, cloneSelector(original, index++)));
    }
    return galeniumElements;
  }
  </#list>

  private static IndexedSelector cloneSelector(InteractiveSelector original, int index) {
    return SelectorFactory.indexedFromSelector(SelectorFactory.fromSelector(original));
  }

  public static ${gweClassName(this)} instance() {
    InteractiveSelector original = ${selectorClassName(this)}.SELF;
    WebElement initialElement = original.find();
    return new ${gweClassName(this)}(initialElement, cloneSelector(original, 0));
  }

  public static ${gweClassName(this)} instance(int index) {
    InteractiveSelector original = ${selectorClassName(this)}.SELF;
    WebElement initialElement = original.findNth(index);
    return new ${gweClassName(this)}(initialElement, cloneSelector(original, index));
  }

  public static java.util.List<${gweClassName(this)}> instances() {
    InteractiveSelector original = ${selectorClassName(this)}.SELF;
    java.util.List<WebElement> initialElements = original.findAll();
    java.util.List<${gweClassName(this)}> galeniumElements = new java.util.ArrayList<${gweClassName(this)}>();
    int index = 0;
    for (WebElement webElement : initialElements) {
      galeniumElements.add(new ${gweClassName(this)}(webElement, cloneSelector(original, index++)));
    }
    return galeniumElements;
  } 

}
