<#assign classNameForSpec = className(spec) />
package ${specRootPackageName};

import static io.wcm.qa.glnm.galen.GalenHelperUtil.readSpec;
import static io.wcm.qa.glnm.galen.GalenHelperUtil.getSectionFilter;
import static io.wcm.qa.glnm.galen.GalenHelperUtil.getCombinedSectionFilter;
import static io.wcm.qa.glnm.galen.GalenLayoutChecker.checkLayout;
import static io.wcm.qa.glnm.galen.GalenLayoutChecker.handleLayoutReport;
import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.PageSpec;

import java.io.File;
import java.util.Collection;
import java.util.ArrayList;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.galen.GalenHelperUtil;
import io.wcm.qa.glnm.selectors.SelectorFromLocator;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;

/**
 * Java version of '${spec.filename}'. 
 */
public class ${classNameForSpec} {

  private static final String RELATIVE_PATH = "${spec.relativeFilePath}";
  private PageSpec pageSpec;

<#if spec.tags??>
  private Collection<String> tags = new ArrayList<>();</#if>

  private void checkSpec() {
    checkSpec((String[])null);
  }

  private void checkSpec(String... includeTags) {
    SectionFilter includeFilter = getSectionFilter(includeTags);
    SectionFilter deviceFilter = getSectionFilter(getTestDevice());
    SectionFilter sectionFilter = getCombinedSectionFilter(deviceFilter, includeFilter);
    LayoutReport layoutReport = checkLayout(RELATIVE_PATH, getPageSpec(sectionFilter), sectionFilter);
    String errorMessage = "errors when checking: " + RELATIVE_PATH;
    String successMessage = "successfully checked: " + RELATIVE_PATH;
    handleLayoutReport(layoutReport, errorMessage, successMessage);
  }

  public PageSpec getPageSpec(SectionFilter sectionFilter) {
     return readSpec(GalenHelperUtil.getBrowser(), GaleniumConfiguration.getGalenSpecPath() + RELATIVE_PATH, sectionFilter);
  }

<#if spec.tags??>
  <#list spec.tags as tag>
    public ${classNameForSpec} with${classNameFromString(tag)}() {
      tags.add("${tag}");
      return this;
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
