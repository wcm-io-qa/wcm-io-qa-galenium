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
package io.wcm.qa.glnm.imagecomparison;

import java.util.List;

import com.galenframework.specs.Spec;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.ObjectSpecs;
import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Factory class to get image comparing Galen specs.
 */
public final class IcsFactory {


  private IcsFactory() {
  }

  /**
   * @param def parameters for spec generation
   * @return a parsed Galen page spec
   */
  public static PageSpec getPageSpec(IcsDefinition def) {
    // specs
    Spec spec = IcUtil.getSpecForText(IcUtil.getImageComparisonSpecText(def));
    ObjectSpecs objectSpecs = new ObjectSpecs(def.getElementName());

    Spec insideViewportSpec = IcUtil.getSpecForText("inside viewport");
    objectSpecs.addSpec(insideViewportSpec);

    objectSpecs.addSpec(spec);
    if (GaleniumConfiguration.isSamplingVerificationIgnore()) {
      spec.setOnlyWarn(true);
      insideViewportSpec.setOnlyWarn(true);
    }
    if (def.isZeroToleranceWarning()) {
      Spec zeroToleranceSpec = IcUtil.getSpecForText(IcUtil.getZeroToleranceImageComparisonSpecText(def));
      zeroToleranceSpec.setOnlyWarn(true);
      objectSpecs.addSpec(zeroToleranceSpec);
    }

    // page section
    PageSection pageSection = new PageSection(def.getSectionName());
    pageSection.addObjects(objectSpecs);

    // page spec
    PageSpec pageSpec = new PageSpec();
    pageSpec.addObject(def.getElementName(), def.getSelector().asLocator());
    List<Selector> objectsToIgnore = def.getObjectsToIgnore();
    if (!objectsToIgnore.isEmpty()) {
      CorrectionsRect corrections = def.getCorrections();
      for (Selector objectToIgnore : objectsToIgnore) {
        Locator asLocator = objectToIgnore.asLocator();
        if (corrections != null) {
          asLocator.withCorrections(corrections);
        }
        pageSpec.addObject(objectToIgnore.elementName(), asLocator);
      }
    }
    pageSpec.addSection(pageSection);

    return pageSpec;
  }

}
