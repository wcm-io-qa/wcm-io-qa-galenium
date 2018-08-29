package ${interactiveSelectorPackage};

import io.wcm.qa.galenium.interaction.Element;
import io.wcm.qa.galenium.selectors.base.Selector;

/**
 * Interface for interactive selectors.
 *
 * This is GENERATED CODE and should not be modified manually.  
 */
public interface ${interactiveSelectorInterfaceClassName} extends Selector {
  
  <#list interaction.methods as method>
     ${method.headForInterface};
  </#list>

}
