<#assign classNameForSpec = className(spec) />
package ${specPackageName};

import static io.wcm.qa.galenium.util.GalenLayoutChecker.checkLayout;
import static io.wcm.qa.galenium.util.GalenLayoutChecker.handleLayoutReport;
import static io.wcm.qa.galenium.util.GalenHelperUtil.readSpec;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.PageSpec;

import java.io.File;
import java.util.Collection;
import java.util.ArrayList;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.selectors.SelectorFromLocator;
import io.wcm.qa.galenium.selectors.SelectorFactory;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

/**
 * Java version of '${spec.filename}'. 
 */
public class ${classNameForSpec} {

  private static final String RELATIVE_PATH = "${spec.relativeFilePath}";
  private PageSpec pageSpec;

<#if spec.tags??>
  private Collection<String> tags = new ArrayList<>();</#if>

  private void checkSpec() {
    LayoutReport layoutReport = checkLayout(RELATIVE_PATH, getPageSpec());
    String errorMessage = "errors when checking: " + RELATIVE_PATH;
    String successMessage = "successfully checked: " + RELATIVE_PATH;
    handleLayoutReport(layoutReport, errorMessage, successMessage);
  }

  public PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = readSpec(GaleniumConfiguration.getGalenSpecPath() + RELATIVE_PATH);
    }
    return pageSpec;
  }

<#if spec.tags??>
  <#list spec.tags as tag>
    public ${classNameForSpec} with${className(tag)}() {
      tags.add("${tag}");
      return this;
    }
  </#list>
</#if>

  public static void check() {
    new ${classNameForSpec}().checkSpec();
  }

}
