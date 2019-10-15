package ${interactiveSelectorPackage};

import io.wcm.qa.glnm.interaction.Element;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Interface for interactive selectors.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public interface ${interactiveSelectorInterfaceClassName} extends Selector {
  
  <#list elementInteraction.methods as method>
     ${method.headForInterface};
  </#list>
  
  <#list formElementInteraction.methods as method>
     ${method.headForInterface};
  </#list>

}
