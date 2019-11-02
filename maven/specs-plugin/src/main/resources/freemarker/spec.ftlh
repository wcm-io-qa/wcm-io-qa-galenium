<#assign classNameForSpec = className(spec) />
package ${specRootPackageName};

import static io.wcm.qa.glnm.galen.reports.GalenReports.handleLayoutReport;
import static io.wcm.qa.glnm.galen.specs.GalenSpecUtil.readSpec;
import static io.wcm.qa.glnm.galen.specs.GalenSpecUtil.asSectionFilter;
import static io.wcm.qa.glnm.galen.specs.GalenSpecUtil.combineSectionFilters;
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
import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.selectors.SelectorFromLocator;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.selectors.base.SelectorFactory;

/**
 * Java version of '${spec.filename}'.
 */
public class ${classNameForSpec} {

  private static final String RELATIVE_PATH = "${spec.relativeFilePath}";

<#if spec.tags??>
  private Collection<String> tags = new ArrayList<>();</#if>

  private void checkSpec() {
    checkSpec((String[])null);
  }

  private void checkSpec(String... includeTags) {
    SectionFilter includeFilter = asSectionFilter(includeTags);
    SectionFilter deviceFilter = asSectionFilter(getTestDevice());
    SectionFilter sectionFilter = combineSectionFilters(deviceFilter, includeFilter);
    LayoutReport layoutReport = GalenValidation.check(RELATIVE_PATH, getPageSpec(sectionFilter), sectionFilter);
    String errorMessage = "errors when checking: " + RELATIVE_PATH;
    String successMessage = "successfully checked: " + RELATIVE_PATH;
    handleLayoutReport(layoutReport, errorMessage, successMessage);
  }

  public PageSpec getPageSpec(SectionFilter sectionFilter) {
     return readSpec(GalenHelperUtil.getBrowser(), GaleniumConfiguration.getGalenSpecPath() + RELATIVE_PATH, sectionFilter);
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
