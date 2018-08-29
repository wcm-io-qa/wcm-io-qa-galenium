package ${interactiveSelectorPackage};

import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.selectors.SelectorFromLocator;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Interface for interactive selectors.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public abstract class ${interactiveSelectorBaseClassName} extends SelectorFromLocator implements ${interactiveSelectorInterfaceClassName} {
  
  protected ${interactiveSelectorBaseClassName}(String elementName, Locator locator) {
    super(elementName, locator);
  }

  <#list interaction.methods as method>
     ${method.head} {
       ${method.body}
     }
  </#list>

}