/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.galenium.maven.freemarker.pojo;

import static io.wcm.qa.galenium.reporting.GaleniumReportUtil.getLogger;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.galenframework.specs.page.Locator;

import io.wcm.qa.galenium.maven.freemarker.util.FormatUtil;
import io.wcm.qa.galenium.selectors.NestedSelector;

class SelectorPojo {

  private Collection<SelectorPojo> children = new ArrayList<>();
  private SelectorPojo parent;
  private NestedSelector selector;
  private SpecPojo spec;

  public SelectorPojo(SpecPojo specPojo, NestedSelector selector) {
    setSelector(selector);
    setSpec(specPojo);
  }

  public void addChild(SelectorPojo child) {
    children.add(child);
  }

  public String getAbsoluteElementName() {
    return getSelector().elementName();
  }

  public Collection<SelectorPojo> getChildren() {
    return children;
  }

  public String getClassName() {
    return FormatUtil.getClassName(getSelector());
  }

  public String getConstantName() {
    return FormatUtil.getConstantName(getSelector());
  }

  public String getCss() {
    return getSelector().asString();
  }

  public String getElementName() {

    if (hasParent()) {
      // child selectors return relative name
      String thisName = getAbsoluteElementName();
      String parentName = getParent().getAbsoluteElementName();
      String cleanName = StringUtils.removeStart(thisName, parentName + ".");
      getLogger().info("this: '" + thisName + "' parent: '" + parentName + "' clean: '" + cleanName + "'");
      return cleanName;
    }

    return getAbsoluteElementName();
  }

  public Locator getLocator() {
    return getSelector().asLocator();
  }

  public String getPackageName() {
    return getSpec().getPackageName();
  }

  public SelectorPojo getParent() {
    return parent;
  }

  public NestedSelector getSelector() {
    return selector;
  }

  public SpecPojo getSpec() {
    return spec;
  }

  public boolean hasChildren() {
    return !children.isEmpty();
  }

  public boolean hasParent() {
    return getParent() != null;
  }

  public void setParent(SelectorPojo parent) {
    this.parent = parent;
    if (parent != null) {
      parent.addChild(this);
    }
  }

  private void setSelector(NestedSelector selector) {
    this.selector = selector;
  }

  private void setSpec(SpecPojo spec) {
    this.spec = spec;
  }

}
