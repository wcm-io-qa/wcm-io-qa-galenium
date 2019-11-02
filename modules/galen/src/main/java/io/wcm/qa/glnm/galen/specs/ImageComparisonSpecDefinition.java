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
package io.wcm.qa.glnm.galen.specs;

import static java.util.Locale.ENGLISH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.galenframework.validation.ValidationListener;

import io.wcm.qa.glnm.configuration.GaleniumConfiguration;
import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.generic.SortedDifferences;
import io.wcm.qa.glnm.galen.specs.page.GalenCorrection;
import io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect;
import io.wcm.qa.glnm.interaction.Mouse;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.util.BrowserUtil;

/**
 * Factory for fileless image comparison specs.
 *
 * @since 1.0.0
 */
public class ImageComparisonSpecDefinition implements IcsDefinition {

  private static final String DEFAULT_PAGE_SECTION_NAME = "Image Comparison";

  private String allowedError;
  private int allowedOffset;
  private GalenCorrectionRect corrections;
  private boolean cropIfOutside;
  private SortedDifferences differences = new SortedDifferences();
  private String elementName;
  private String filename;
  private String foldername;
  private List<Selector> objectsToIgnore = new ArrayList<Selector>();
  private String sectionName = DEFAULT_PAGE_SECTION_NAME;
  private Selector selector;
  private boolean zeroToleranceWarning;

  /**
   * <p>Constructor for ImageComparisonSpecFactory.</p>
   *
   * @param selector selector for main object
   * @since 2.0.0
   */
  public ImageComparisonSpecDefinition(Selector selector) {
    this(selector, selector.elementName());
    if (BrowserUtil.isChrome()) {
      correctForSrollPosition(Mouse.getVerticalScrollPosition().intValue());
    }
  }

  /**
   * <p>Constructor for ImageComparisonSpecFactory.</p>
   *
   * @param selector selector for main object
   * @param elementName object name to use
   * @since 2.0.0
   */
  public ImageComparisonSpecDefinition(Selector selector, String elementName) {
    setSelector(selector);
    setElementName(elementName);
    setFilename(elementName.toLowerCase(ENGLISH) + ".png");
    setSectionName(DEFAULT_PAGE_SECTION_NAME + " for " + getElementName());
  }

  /**
   * <p>addAll.</p>
   *
   * @param toBeAppended differences to be appended
   * @return true if this list changed as a result of the call
   * @since 2.0.0
   */
  public boolean addAll(Collection<? extends Difference> toBeAppended) {
    return getDifferences().addAll(toBeAppended);
  }

  /**
   * <p>addDifference.</p>
   *
   * @param difference appends a difference
   * @since 2.0.0
   */
  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  /**
   * <p>addObjectToIgnore.</p>
   *
   * @param selectorToIgnore the area of this object will be ignored in image comparison
   * @since 2.0.0
   */
  public void addObjectToIgnore(Selector selectorToIgnore) {
    getObjectsToIgnore().add(selectorToIgnore);
  }

  /**
   * Removes all differences added to this factory.
   *
   * @since 2.0.0
   */
  public void clearDifferences() {
    getDifferences().clear();
  }

  /** {@inheritDoc} */
  @Override
  public void correctForSrollPosition(int yCorrection) {
    GalenCorrection top = GalenCorrection.adjust(-yCorrection);
    setCorrections(new GalenCorrectionRect().withTop(top));
  }

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>allowedError</code>.</p>
   */
  @Override
  public String getAllowedError() {
    return allowedError;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>allowedOffset</code>.</p>
   */
  @Override
  public int getAllowedOffset() {
    return allowedOffset;
  }

  /**
   * <p>getComparator.</p>
   *
   * @return a {@link java.util.Comparator} object.
   * @since 2.0.0
   */
  public Comparator<Difference> getComparator() {
    return this.differences.getComparator();
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Getter for the field <code>differences</code>.
   * </p>
   */
  @Override
  public GalenCorrectionRect getCorrections() {
    return corrections;
  }

  /**
   * <p>Getter for the field <code>differences</code>.</p>
   *
   * @return a  {@link io.wcm.qa.glnm.differences.generic.SortedDifferences} object.
   * @since 4.0.0
   */
  public SortedDifferences getDifferences() {
    return differences;
  }
  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>elementName</code>.</p>
   */
  @Override
  public String getElementName() {
    return elementName;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>filename</code>.</p>
   */
  @Override
  public String getFilename() {
    return filename;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>foldername</code>.</p>
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

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>objectsToIgnore</code>.</p>
   */
  @Override
  public List<Selector> getObjectsToIgnore() {
    return objectsToIgnore;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>sectionName</code>.</p>
   */
  @Override
  public String getSectionName() {
    return sectionName;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Getter for the field <code>selector</code>.</p>
   */
  @Override
  public Selector getSelector() {
    return selector;
  }

  /**
   * <p>Getter for the field <code>validationListener</code>.</p>
   *
   * @return a {@link com.galenframework.validation.ValidationListener} object.
   * @since 2.0.0
   */
  public ValidationListener getValidationListener() {
    return new IcValidationListener();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCropIfOutside() {
    return cropIfOutside;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isZeroToleranceWarning() {
    return zeroToleranceWarning;
  }

  /**
   * <p>setAllowedErrorPercent.</p>
   *
   * @param allowedErrorPercentage zero or negative will result in zero tolerance
   * @since 2.0.0
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
   * <p>setAllowedErrorPixel.</p>
   *
   * @param allowedErrorPixels zero or negative will result in zero tolerance
   * @since 2.0.0
   */
  public void setAllowedErrorPixel(Integer allowedErrorPixels) {
    if (allowedErrorPixels > 0) {
      this.allowedError = allowedErrorPixels + "px";
    }
    else {
      this.allowedError = StringUtils.EMPTY;
    }
  }

  /**
   * <p>Setter for the field <code>allowedOffset</code>.</p>
   *
   * @param allowedOffset a int.
   * @since 2.0.0
   */
  public void setAllowedOffset(int allowedOffset) {
    this.allowedOffset = allowedOffset;
  }

  /**
   * <p>setComparator.</p>
   *
   * @param comparator used to order differences in a consistent manner
   * @since 2.0.0
   */
  public void setComparator(Comparator<Difference> comparator) {
    this.differences.setComparator(comparator);
  }

  /**
   * <p>Setter for the field <code>corrections</code>.</p>
   *
   * @param corrections a {@link com.galenframework.specs.page.CorrectionsRect} object.
   * @since 2.0.0
   */
  public void setCorrections(GalenCorrectionRect corrections) {
    this.corrections = corrections;
  }

  /**
   * <p>Setter for the field <code>cropIfOutside</code>.</p>
   *
   * @param cropIfOutside a boolean.
   * @since 4.0.0
   */
  public void setCropIfOutside(boolean cropIfOutside) {
    this.cropIfOutside = cropIfOutside;
  }

  /**
   * <p>Setter for the field <code>differences</code>.</p>
   *
   * @param differences a  {@link io.wcm.qa.glnm.differences.generic.SortedDifferences} object.
   * @since 2.0.0
   */
  public void setDifferences(SortedDifferences differences) {
    this.differences = differences;
  }

  /**
   * <p>Setter for the field <code>elementName</code>.</p>
   *
   * @param elementName a {@link java.lang.String} object.
   * @since 2.0.0
   */
  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  /**
   * <p>Setter for the field <code>filename</code>.</p>
   *
   * @param filename a {@link java.lang.String} object.
   * @since 2.0.0
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * <p>Setter for the field <code>foldername</code>.</p>
   *
   * @param foldername a {@link java.lang.String} object.
   * @since 2.0.0
   */
  public void setFoldername(String foldername) {
    this.foldername = foldername;
  }

  /**
   * <p>Setter for the field <code>objectsToIgnore</code>.</p>
   *
   * @param objectsToIgnore a {@link java.util.List} object.
   * @since 2.0.0
   */
  public void setObjectsToIgnore(List<Selector> objectsToIgnore) {
    this.objectsToIgnore = objectsToIgnore;
  }

  /**
   * <p>Setter for the field <code>sectionName</code>.</p>
   *
   * @param sectionName a {@link java.lang.String} object.
   * @since 2.0.0
   */
  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }

  /**
   * <p>Setter for the field <code>selector</code>.</p>
   *
   * @param selector a  {@link io.wcm.qa.glnm.selectors.base.Selector} object.
   * @since 2.0.0
   */
  public void setSelector(Selector selector) {
    this.selector = selector;
  }

  /**
   * <p>Setter for the field <code>zeroToleranceWarning</code>.</p>
   *
   * @param zeroToleranceWarning a boolean.
   * @since 2.0.0
   */
  public void setZeroToleranceWarning(boolean zeroToleranceWarning) {
    this.zeroToleranceWarning = zeroToleranceWarning;
  }

}
