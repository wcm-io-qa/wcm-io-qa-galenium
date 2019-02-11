package ${interactiveSelectorPackage};

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.selectors.IndexedSelector;

/**
 * Interface for interactive selectors.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public interface ${interactiveSelectorInterfaceClassName} extends IndexedSelector {
  
  <#list elementInteraction.methods as method>
     ${method.headForInterface};
  </#list>
  
  <#list formElementInteraction.methods as method>
     ${method.headForInterface};
  </#list>

}
