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

import static java.util.Locale.ENGLISH;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.galenframework.parser.SyntaxException;
import com.galenframework.speclang2.specs.SpecReader;
import com.galenframework.specs.Spec;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.CorrectionsRect.Correction;
import com.galenframework.specs.page.CorrectionsRect.Type;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.ObjectSpecs;
import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.galenium.configuration.GaleniumConfiguration;
import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.differences.generic.SortedDifferences;
import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.interaction.Mouse;
import io.wcm.qa.galenium.reporting.GaleniumReportUtil;
import io.wcm.qa.galenium.selectors.base.Selector;
import io.wcm.qa.galenium.util.BrowserUtil;

/**
 * Factory for fileless image comparison specs.
 *
 * @since 1.0.0
 */
public class ImageComparisonSpecFactory {

  private static final String DEFAULT_PAGE_SECTION_NAME = "Image Comparison";
  private static final Correction NO_CORRECTION = new Correction(0, CorrectionsRect.Type.PLUS);

  private String allowedError;
  private int allowedOffset;
  private CorrectionsRect corrections;
  private SortedDifferences differences = new SortedDifferences();
  private String elementName;
  private String filename;
  private String foldername;
  private List<Selector> objectsToIgnore = new ArrayList<Selector>();
  private String sectionName = DEFAULT_PAGE_SECTION_NAME;
  private Selector selector;

  private ValidationListener validationListener = new ImageComparisonValidationListener();
  private boolean zeroToleranceWarning;

  /**
   * <p>Constructor for ImageComparisonSpecFactory.</p>
   *
   * @param selector selector for main object
   */
  public ImageComparisonSpecFactory(Selector selector) {
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
   */
  public ImageComparisonSpecFactory(Selector selector, String elementName) {
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
   */
  public boolean addAll(Collection<? extends Difference> toBeAppended) {
    return getDifferences().addAll(toBeAppended);
  }

  /**
   * <p>addDifference.</p>
   *
   * @param difference appends a difference
   */
  public void addDifference(Difference difference) {
    getDifferences().add(difference);
  }

  /**
   * <p>addObjectToIgnore.</p>
   *
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

  /**
   * <p>correctForSrollPosition.</p>
   *
   * @param yCorrection amount of scrolling
   */
  public void correctForSrollPosition(int yCorrection) {
    Correction top = new Correction(yCorrection, Type.MINUS);
    CorrectionsRect correctionsRect = new CorrectionsRect(NO_CORRECTION, top, NO_CORRECTION, NO_CORRECTION);
    setCorrections(correctionsRect);
  }

  /**
   * <p>Getter for the field <code>allowedError</code>.</p>
   *
   * @return allowed error string
   */
  public String getAllowedError() {
    return allowedError;
  }

  /**
   * <p>Getter for the field <code>allowedOffset</code>.</p>
   *
   * @return offset to analyse
   */
  public int getAllowedOffset() {
    return allowedOffset;
  }

  /**
   * <p>getComparator.</p>
   *
   * @return a {@link java.util.Comparator} object.
   */
  public Comparator<Difference> getComparator() {
    return this.differences.getComparator();
  }

  /**
   * <p>Getter for the field <code>differences</code>.</p>
   *
   * @return a {@link io.wcm.qa.galenium.differences.generic.SortedDifferences} object.
   */
  public SortedDifferences getDifferences() {
    return differences;
  }

  /**
   * <p>Getter for the field <code>elementName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getElementName() {
    return elementName;
  }

  /**
   * <p>Getter for the field <code>filename</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getFilename() {
    return filename;
  }

  /**
   * <p>Getter for the field <code>foldername</code>.</p>
   *
   * @return the set folder name or one constructed from differences
   */
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
   * <p>Getter for the field <code>objectsToIgnore</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Selector> getObjectsToIgnore() {
    return objectsToIgnore;
  }

  /**
   * <p>getPageSpecInstance.</p>
   *
   * @return page spec according to params set on factory
   */
  public PageSpec getPageSpecInstance() {
    // specs
    Spec spec = getSpecForText(getImageComparisonSpecText());
    ObjectSpecs objectSpecs = new ObjectSpecs(getElementName());

    Spec insideViewportSpec = getSpecForText("inside viewport");
    objectSpecs.addSpec(insideViewportSpec);

    objectSpecs.addSpec(spec);
    if (GaleniumConfiguration.isSaveSampledImages()) {
      spec.setOnlyWarn(true);
      insideViewportSpec.setOnlyWarn(true);
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

  /**
   * <p>Getter for the field <code>sectionName</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getSectionName() {
    return sectionName;
  }

  /**
   * <p>Getter for the field <code>selector</code>.</p>
   *
   * @return a {@link io.wcm.qa.galenium.selectors.base.Selector} object.
   */
  public Selector getSelector() {
    return selector;
  }

  /**
   * <p>Getter for the field <code>validationListener</code>.</p>
   *
   * @return a {@link com.galenframework.validation.ValidationListener} object.
   */
  public ValidationListener getValidationListener() {
    return validationListener;
  }

  /**
   * <p>isZeroToleranceWarning.</p>
   *
   * @return a boolean.
   */
  public boolean isZeroToleranceWarning() {
    return zeroToleranceWarning;
  }

  /**
   * <p>setAllowedErrorPercent.</p>
   *
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
   * <p>setAllowedErrorPixel.</p>
   *
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

  /**
   * <p>Setter for the field <code>allowedOffset</code>.</p>
   *
   * @param allowedOffset a int.
   */
  public void setAllowedOffset(int allowedOffset) {
    this.allowedOffset = allowedOffset;
  }

  /**
   * <p>setComparator.</p>
   *
   * @param comparator used to order differences in a consistent manner
   */
  public void setComparator(Comparator<Difference> comparator) {
    this.differences.setComparator(comparator);
  }

  /**
   * <p>Setter for the field <code>corrections</code>.</p>
   *
   * @param corrections a {@link com.galenframework.specs.page.CorrectionsRect} object.
   */
  public void setCorrections(CorrectionsRect corrections) {
    this.corrections = corrections;
  }

  /**
   * <p>Setter for the field <code>differences</code>.</p>
   *
   * @param differences a {@link io.wcm.qa.galenium.differences.generic.SortedDifferences} object.
   */
  public void setDifferences(SortedDifferences differences) {
    this.differences = differences;
  }

  /**
   * <p>Setter for the field <code>elementName</code>.</p>
   *
   * @param elementName a {@link java.lang.String} object.
   */
  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  /**
   * <p>Setter for the field <code>filename</code>.</p>
   *
   * @param filename a {@link java.lang.String} object.
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * <p>Setter for the field <code>foldername</code>.</p>
   *
   * @param foldername a {@link java.lang.String} object.
   */
  public void setFoldername(String foldername) {
    this.foldername = foldername;
  }

  /**
   * <p>Setter for the field <code>objectsToIgnore</code>.</p>
   *
   * @param objectsToIgnore a {@link java.util.List} object.
   */
  public void setObjectsToIgnore(List<Selector> objectsToIgnore) {
    this.objectsToIgnore = objectsToIgnore;
  }

  /**
   * <p>Setter for the field <code>sectionName</code>.</p>
   *
   * @param sectionName a {@link java.lang.String} object.
   */
  public void setSectionName(String sectionName) {
    this.sectionName = sectionName;
  }

  /**
   * <p>Setter for the field <code>selector</code>.</p>
   *
   * @param selector a {@link io.wcm.qa.galenium.selectors.base.Selector} object.
   */
  public void setSelector(Selector selector) {
    this.selector = selector;
  }

  /**
   * <p>Setter for the field <code>validationListener</code>.</p>
   *
   * @param validationListener a {@link com.galenframework.validation.ValidationListener} object.
   */
  public void setValidationListener(ValidationListener validationListener) {
    this.validationListener = validationListener;
  }

  /**
   * <p>Setter for the field <code>zeroToleranceWarning</code>.</p>
   *
   * @param zeroToleranceWarning a boolean.
   */
  public void setZeroToleranceWarning(boolean zeroToleranceWarning) {
    this.zeroToleranceWarning = zeroToleranceWarning;
  }

  private Spec getSpecForText(String specText) {
    try {
      return new SpecReader().read(specText);
    }
    catch (IllegalArgumentException | SyntaxException ex) {
      String msg = "when parsing spec text: '" + specText + "'";
      GaleniumReportUtil.getLogger().error(msg);
      throw new GaleniumException(msg, ex);
    }
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

  protected String getImageComparisonSpecText(String folder, String fileName, String error, int offset) {
    StringBuilder specText = new StringBuilder();

    // boiler plate
    specText.append("image file ");

    // image file
    specText.append(getImageOrDummySamplePath(folder, fileName));

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

  private String getImageOrDummySamplePath(String folder, String fileName) {
    String fullFilePath;

    // folder
    if (StringUtils.isNotBlank(folder)) {
      fullFilePath = FilenameUtils.concat(folder, fileName);
    }
    else {
      // no folder means fileName is all the path info we have
      fullFilePath = fileName;
    }

    createDummyIfSampleDoesNotExist(fullFilePath);

    return fullFilePath;
  }

  private void createDummyIfSampleDoesNotExist(String fullFilePath) {
    if (isExpectedImageSampleMissing(fullFilePath)) {
      GaleniumReportUtil.getLogger().info("Cannot find sample. Substituting dummy for '" + fullFilePath + "'");

      // if image is missing, we'll substitute a dummy to force Galen to at least sample the page
      File targetFile = new File(fullFilePath);

      ImageComparisonUtil.writeDummySample(targetFile);
    }
  }

  private boolean isExpectedImageSampleMissing(String fullFilePath) {
    return !new File(fullFilePath).isFile();
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
