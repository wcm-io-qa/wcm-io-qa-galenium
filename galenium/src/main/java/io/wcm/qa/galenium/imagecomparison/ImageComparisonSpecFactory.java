/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.galenium.imagecomparison;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.galenframework.speclang2.specs.SpecReader;
import com.galenframework.specs.Spec;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.CorrectionsRect.Correction;
import com.galenframework.specs.page.CorrectionsRect.Type;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.ObjectSpecs;
import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

/**
 * Factory for fileless image comparison specs.
 */
public class ImageComparisonSpecFactory {

  private static final String DEFAULT_PAGE_SECTION_NAME = "Image Comparison";
  private static final Correction NO_CORRECTION = new Correction(0, CorrectionsRect.Type.PLUS);

  private String allowedError;
  private int allowedOffset;
  private CorrectionsRect corrections;
  private String elementName;
  private String filename;
  private String foldername;
  private List<Selector> objectsToIgnore = new ArrayList<Selector>();
  private String sectionName = DEFAULT_PAGE_SECTION_NAME;
  private Selector selector;

  private boolean zeroToleranceWarning;

  /**
   * @param selector selector for main object
   */
  public ImageComparisonSpecFactory(Selector selector) {
    this(selector, selector.elementName());
  }

  /**
   * @param selector selector for main object
   * @param elementName object name to use
   */
  public ImageComparisonSpecFactory(Selector selector, String elementName) {
    setSelector(selector);
    setElementName(elementName);
    setFilename(elementName + ".png");
  }

  /**
   * @param selectorToIgnore the area of this object will be ignored in image comparison
   */
  public void addObjectToIgnore(Selector selectorToIgnore) {
    getObjectsToIgnore().add(selectorToIgnore);
  }

  /**
   * @param yCorrection amount of scrolling
   */
  public void correctForSrollPosition(int yCorrection) {
    Correction top = new Correction(yCorrection, Type.MINUS);
    CorrectionsRect correctionsRect = new CorrectionsRect(NO_CORRECTION, top, NO_CORRECTION, NO_CORRECTION);
    setCorrections(correctionsRect);
  }

  /**
   * @return allowed error string
   */
  public String getAllowedError() {
    return allowedError;
  }

  /**
   * @return offset to analyse
   */
  public int getAllowedOffset() {
    return allowedOffset;
  }

  public String getElementName() {
    return elementName;
  }

  public String getFilename() {
    return filename;
  }

  public String getFoldername() {
    return foldername;
  }

  public List<Selector> getObjectsToIgnore() {
    return objectsToIgnore;
  }

  /**
   * @return page spec according to params set on factory
   */
  public PageSpec getPageSpecInstance() {
    // specs
    Spec spec = getSpecForText(getImageComparisonSpecText());
    ObjectSpecs objectSpecs = new ObjectSpecs(getElementName());

    objectSpecs.addSpec(spec);
    if (GaleniumConfiguration.isSaveSampledImages()) {
      spec.setOnlyWarn(true);
    }
    else if (isZeroToleranceWarning()) {
      Spec zeroToleranceSpec = getSpecForText(getZeroToleranceImageComparisonSpecText());
      zeroToleranceSpec.setOnlyWarn(true);
      objectSpecs.addSpec(zeroToleranceSpec);
    }

    // page section
    PageSection pageSection = new PageSection(getSectionName());
    pageSection.addObjects(objectSpecs);

    // page spec
    PageSpec pageSpec = new PageSpec();
    pageSpec.addObject(getElementName(), getLocator());
    if (!getObjectsToIgnore().isEmpty()) {
      for (Selector objectToIgnore : objectsToIgnore) {
        Locator asLocator = objectToIgnore.asLocator();
        if (hasCorrections()) {
          asLocator.withCorrections(getCorrections());
        }
        pageSpec.addObject(objectToIgnore.elementName(), asLocator);
      }
    }
    pageSpec.addSection(pageSection);

    return pageSpec;
  }

  public String getSectionName() {
    return sectionName;
  }

  public Selector getSelector() {
    return selector;
  }

  public boolean isZeroToleranceWarning() {
    return zeroToleranceWarning;
  }

  /**
   * @param allowedErrorPercentage zero or negative will result in zero tolerance
   */
  public void setAllowedErrorPercent(Double allowedErrorPercentage) {
    if (allowedErrorPercentage > 0) {
      this.allowedError = allowedErrorPercentage + "%";
    }
    else {
      this.allowedError = StringUtils.EMPTY;
    }
  }

  /**
   * @param allowedErrorPixels zero or negative will result in zero tolerance
   */
  public void setAllowedErrorPixel(Integer allowedErrorPixels) {
    if (allowedErrorPixels > 0) {
      this.allowedError = allowedErrorPixels + "px";
    }
    else {
      this.allowedError = StringUtils.EMPTY;
    }
  }

  public void setAllowedOffset(int allowedOffset) {
    this.allowedOffset = allowedOffset;
  }

  public void setCorrections(CorrectionsRect corrections) {
    this.corrections = corrections;
  }

  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void setFoldername(String foldername) {
    this.foldername = foldername;
  }

  public void setObjectsToIgnore(List<Selector> objectsToIgnore) {
    this.objectsToIgnore = objectsToIgnore;
  }

  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }

  public void setSelector(Selector selector) {
    this.selector = selector;
  }

  public void setZeroToleranceWarning(boolean zeroToleranceWarning) {
    this.zeroToleranceWarning = zeroToleranceWarning;
  }

  private Spec getSpecForText(String specText) {
    return new SpecReader().read(specText);
  }

  private boolean hasCorrections() {
    return getCorrections() != null;
  }

  protected CorrectionsRect getCorrections() {
    return corrections;
  }

  protected String getImageComparisonSpecText() {
    String error;
    int offset;
    if (GaleniumConfiguration.isSaveSampledImages()) {
      error = StringUtils.EMPTY;
      offset = 0;
    }
    else {
      error = getAllowedError();
      offset = getAllowedOffset();
    }
    return getImageComparisonSpecText(getFoldername(), getFilename(), error, offset);
  }

  protected String getImageComparisonSpecText(String folder, String file, String error, int offset) {
    StringBuilder specText = new StringBuilder();
    specText.append("image file ");

    // folder
    if (StringUtils.isNotBlank(folder)) {
      if (StringUtils.endsWith(folder, "/")) {
        specText.append(folder);
      }
      else {
        specText.append(folder);
        specText.append("/");
      }
    }

    // image file
    specText.append(file);

    // tolerance
    if (StringUtils.isNotBlank(error)) {
      specText.append(", error ");
      specText.append(error);
    }
    if (offset > 0) {
      specText.append(", analyze-offset ");
      specText.append(offset);
    }
    if (!getObjectsToIgnore().isEmpty()) {
      List<Selector> objects = getObjectsToIgnore();
      specText.append(", ignore-objects ");
      if (objects.size() == 1) {
        specText.append(objects.get(0));
      }
      else {
        specText.append("[");
        Collection<String> elementNames = new HashSet<String>();

        for (Selector object : objects) {
          elementNames.add(object.elementName());
        }

        specText.append(StringUtils.join(elementNames, ", "));
        specText.append("]");
      }
    }

    return specText.toString();
  }

  protected Locator getLocator() {
    Locator locator = getSelector().asLocator();
    if (hasCorrections()) {
      locator.withCorrections(getCorrections());
    }
    return locator;
  }

  protected String getZeroToleranceImageComparisonSpecText() {
    return getImageComparisonSpecText(getFoldername(), getFilename(), "", 0);
  }

}
