/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.qa.glnm.galen.specs;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.interaction.Browser;
import io.wcm.qa.glnm.reporting.GaleniumReportUtil;
import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Provide access to everything about this Galen spec.
 *
 * @since 4.0.0
 */
public abstract class AbstractGalenSpec implements GalenSpec {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractGalenSpec.class);
  private static final String[] WITHOUT_TAGS = new String[] {};
  private GalenPageSpecProvider galenSpecProvider;
  private String name;
  private PageSpec pageSpec;
  private String uuid;


  protected AbstractGalenSpec(GalenPageSpecProvider provider) {
    galenSpecProvider = provider;
  }

  /** {@inheritDoc} */
  @Override
  public GalenSpecRun check() {
    return check(WITHOUT_TAGS);
  }

  /** {@inheritDoc} */
  @Override
  public GalenSpecRun check(String... tags) {
    String runName = getRunName(tags);
    if (LOG.isInfoEnabled()) {
      LOG.info("checking '" + runName + "'");
    }
    setUuid(GaleniumReportUtil.startStep(runName));
    LayoutReport report = GalenLayout.check(
        runName,
        getPageSpec(),
        GalenSpecUtil.getSectionFilter(tags),
        getValidationListener());
    GalenSpecRun specRun = createRunFromReport(report);
    GaleniumReportUtil.stopStep();
    return specRun;
  }

  /** {@inheritDoc} */
  @Override
  public String getName() {
    if (name == null) {
      name = initSpecName();
    }
    return name;
  }

  /** {@inheritDoc} */
  @Override
  public Collection<NestedSelector> getObjects() {
    return GalenSpecUtil.getObjects(getPageSpec());
  }

  /**
   * <p>Getter for the field <code>pageSpec</code>.</p>
   *
   * @return a {@link com.galenframework.specs.page.PageSpec} object.
   */
  public PageSpec getPageSpec() {
    if (pageSpec == null) {
      pageSpec = initPageSpec();
    }
    return pageSpec;
  }

  /**
   * <p>Setter for the field <code>name</code>.</p>
   *
   * @param name a {@link java.lang.String} object.
   */
  public void setName(String name) {
    this.name = name;
  }

  private GalenSpecRun createRunFromReport(LayoutReport report) {
    GalenSpec spec = this;
    return GalenSpecUtil.createRun(spec, report);
  }

  protected String getRunName(String... tags) {
    SectionFilter sectionFilter = GalenSpecUtil.getSectionFilter(tags);
    StringBuilder runName = new StringBuilder();
    runName.append(getName());
    String currentUrl = Browser.getCurrentUrl();
    if (StringUtils.isNotBlank(currentUrl)) {
      runName.append(" (");
      runName.append(currentUrl);
      runName.append(")");
    }
    if (CollectionUtils.isNotEmpty(sectionFilter.getIncludedTags())) {
      runName.append(" with [");
      runName.append(StringUtils.join(sectionFilter.getIncludedTags(), ", "));
      runName.append("]");
    }
    if (CollectionUtils.isNotEmpty(sectionFilter.getExcludedTags())) {
      runName.append(" without [");
      runName.append(StringUtils.join(sectionFilter.getExcludedTags(), ", "));
      runName.append("]");
    }
    String string = runName.toString();
    return string;
  }

  private String initSpecName() {
    StringBuilder specName = new StringBuilder();
    List<PageSection> sections = getPageSpec().getSections();
    if (CollectionUtils.isNotEmpty(sections)) {
      specName.append(sections.get(0).getName());
    }
    else {
      specName.append(getGalenSpecProvider().toString());
    }
    return specName.toString();
  }

  /**
   * <p>
   * Getter for the field <code>galenSpecProvider</code>.
   * </p>
   *
   * @return a {@link io.wcm.qa.glnm.galen.specs.GalenPageSpecProvider} object.
   * @since 4.0.0
   */
  protected GalenPageSpecProvider getGalenSpecProvider() {
    return galenSpecProvider;
  }

  protected abstract ValidationListener getValidationListener();

  protected PageSpec initPageSpec() {
    return getGalenSpecProvider().getPageSpec();
  }

  /**
   * <p>
   * Setter for the field <code>galenSpecProvider</code>.
   * </p>
   *
   * @param galenSpecProvider a {@link io.wcm.qa.glnm.galen.specs.GalenPageSpecProvider} object.
   * @since 4.0.0
   */
  protected void setGalenSpecProvider(GalenPageSpecProvider galenSpecProvider) {
    this.galenSpecProvider = galenSpecProvider;
  }

  /**
   * <p>Setter for the field <code>uuid</code>.</p>
   *
   * @param uuid a {@link java.lang.String} object.
   */
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  protected String getUuid() {
    return uuid;
  }

}
