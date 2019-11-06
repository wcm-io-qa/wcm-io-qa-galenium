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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.selectors.base.NestedSelector;

/**
 * Provide access to everything about this Galen spec.
 *
 * @since 4.0.0
 */
abstract class AbstractGalenSpec implements GalenSpec {

  private static final String[] WITHOUT_TAGS = new String[] {};
  private static final Logger LOG = LoggerFactory.getLogger(AbstractGalenSpec.class);
  private GalenPageSpecProvider galenSpecProvider;
  private PageSpec pageSpec;


  protected AbstractGalenSpec(GalenPageSpecProvider provider) {
    galenSpecProvider = provider;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.glnm.galen.specs.GalenSpec#check()
   */
  /** {@inheritDoc} */
  @Override
  public GalenSpecRun check() {
    return check(WITHOUT_TAGS);
  }

  /** {@inheritDoc} */
  @Override
  public GalenSpecRun check(String... tags) {
    LayoutReport report = runWithGalen(GalenSpecUtil.asSectionFilter(tags));
    return createRunFromReport(report);
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.glnm.galen.specs.GalenSpec#getName()
   */
  /** {@inheritDoc} */
  @Override
  public String getName() {
    return getPageSpec().getSections().get(0).getName();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.glnm.galen.specs.GalenSpec#getObjects()
   */
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

  private GalenSpecRun createRunFromReport(LayoutReport report) {
    return new GalenSpecRun(this, report);
  }

  private LayoutReport runWithGalen() {
    SectionFilter defaultIncludeTags = GalenSpecUtil.getDefaultIncludeTags();
    return runWithGalen(defaultIncludeTags);
  }


  private LayoutReport runWithGalen(SectionFilter includeTags) {
    return GalenLayout.check(getName(), getPageSpec(), includeTags, getValidationListener());
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

  protected ValidationListener getValidationListener() {
    if (LOG.isTraceEnabled()) {
      return GalenValidation.getTracingValidationListener();
    }
    return GalenValidation.getNoOpValidationListener();
  }

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
}
