<#assign classNameForSpec = className(spec) />
package ${specRootPackageName};

import static io.wcm.qa.glnm.galen.reports.GalenReports.handleGalenSpecRun;

import java.io.File;
import java.util.Collection;
import java.util.ArrayList;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.galen.util.GalenHelperUtil;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.selectors.SelectorFromLocator;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;

/**
 * Java version of '${spec.filename}'.
 */
public class ${classNameForSpec} {

  private static final String RELATIVE_PATH = "${spec.relativeFilePath}";

  private void checkSpec() {
    checkSpec((String[])null);
  }

  private void checkSpec(String... includeTags) {
    GalenSpecRun specRun = GalenValidation.check(RELATIVE_PATH, includeTags);
    String errorMessage = "errors when checking: " + RELATIVE_PATH;
    String successMessage = "successfully checked: " + RELATIVE_PATH;
    handleGalenSpecRun(specRun, errorMessage, successMessage);
  }

<#if spec.tags??>
  <#list spec.tags as tag>
    public static void check${classNameFromString(tag)}() {
      check("${tag}");
    }
  </#list>
</#if>

  public static void check() {
    instance().checkSpec();
  }

  public static void check(String... tags) {
    instance().checkSpec(tags);
  }

  private static ${classNameForSpec} instance() {
    return new ${classNameForSpec}();
  }

}
