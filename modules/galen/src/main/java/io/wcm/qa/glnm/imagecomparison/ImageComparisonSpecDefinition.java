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
package io.wcm.qa.glnm.imagecomparison;

import static java.util.Locale.ENGLISH;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.CorrectionsRect.Correction;
import com.galenframework.specs.page.CorrectionsRect.Type;
import com.galenframework.specs.page.Locator;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.generic.SortedDifferences;
import io.wcm.qa.glnm.interaction.Mouse;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.util.BrowserUtil;

/**
 * Factory for fileless image comparison specs.
 */
public class ImageComparisonSpecDefinition implements IcsDefinition {

  private static final String DEFAULT_PAGE_SECTION_NAME = "Image Comparison";
  private static final Correction NO_CORRECTION = new Correction(0, CorrectionsRect.Type.PLUS);

  private String allowedError;
  private int allowedOffset;
  private CorrectionsRect corrections;
  private boolean cropIfOutside = true;
  private SortedDifferences differences = new SortedDifferences();
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
  public ImageComparisonSpecDefinition(Selector selector) {
    this(selector, selector.elementName());
    if (BrowserUtil.isChrome()) {
      correctForSrollPosition(Mouse.getVerticalScrollPosition().intValue());
    }
  }

  /**
   * @param selector selector for main object
   * @param elementName object name to use
   */
  public ImageComparisonSpecDefinition(Selector selector, String elementName) {
    setSelector(selector);
    setElementName(elementName);
    setFilename(elementName.toLowerCase(ENGLISH) + ".png");
    setSectionName(DEFAULT_PAGE_SECTION_NAME + " for " + getElementName());
  }

  /**
   * @param difference appends a difference
   */
  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  /**
   * @param selectorToIgnore the area of this object will be ignored in image comparison
   */
  public void addObjectToIgnore(Selector selectorToIgnore) {
    getObjectsToIgnore().add(selectorToIgnore);
  }

  /**
   * Removes all differences added to this factory.
   */
  public void clearDifferences() {
    getDifferences().clear();
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#correctForSrollPosition(int)
   */
  @Override
  public void correctForSrollPosition(int yCorrection) {
    Correction top = new Correction(yCorrection, Type.MINUS);
    CorrectionsRect correctionsRect = new CorrectionsRect(NO_CORRECTION, top, NO_CORRECTION, NO_CORRECTION);
    setCorrections(correctionsRect);
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getAllowedError()
   */
  @Override
  public String getAllowedError() {
    return allowedError;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getAllowedOffset()
   */
  @Override
  public int getAllowedOffset() {
    return allowedOffset;
  }

  @Override
  public CorrectionsRect getCorrections() {
    return corrections;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getDifferences()
   */
  public SortedDifferences getDifferences() {
    return differences;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getElementName()
   */
  @Override
  public String getElementName() {
    return elementName;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getFilename()
   */
  @Override
  public String getFilename() {
    return filename;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getFoldername()
   */
  @Override
  public String getFoldername() {
    if (foldername != null) {
      return foldername;
    }
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(GaleniumConfiguration.getExpectedImagesDirectory());
    stringBuilder.append("/");
    stringBuilder.append(getDifferences().asFilePath());
    return stringBuilder.toString();
  }

  @Override
  public Locator getLocator() {
    Locator locator = getSelector().asLocator();
    if (hasCorrections()) {
      locator.withCorrections(getCorrections());
    }
    return locator;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getObjectsToIgnore()
   */
  @Override
  public List<Selector> getObjectsToIgnore() {
    return objectsToIgnore;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getSectionName()
   */
  @Override
  public String getSectionName() {
    return sectionName;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getSelector()
   */
  @Override
  public Selector getSelector() {
    return selector;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#getValidationListener()
   */
  public ValidationListener getValidationListener() {
    return new IcValidationListener();
  }

  @Override
  public boolean isCropIfOutside() {
    return cropIfOutside;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.imagecomparison.IcsDefinition#isZeroToleranceWarning()
   */
  @Override
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

  /**
   * @param comparator used to order differences in a consistent manner
   */
  public void setComparator(Comparator<Difference> comparator) {
    this.differences.setComparator(comparator);
  }

  public void setCorrections(CorrectionsRect corrections) {
    this.corrections = corrections;
  }

  public void setCropIfOutside(boolean cropIfOutside) {
    this.cropIfOutside = cropIfOutside;
  }

  public void setDifferences(SortedDifferences differences) {
    this.differences = differences;
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

  private boolean hasCorrections() {
    return getCorrections() != null;
  }

}
