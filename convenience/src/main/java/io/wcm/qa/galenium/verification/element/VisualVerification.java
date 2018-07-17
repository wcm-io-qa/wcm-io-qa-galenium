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
package io.wcm.qa.galenium.verification.element;

import static io.wcm.qa.galenium.util.GaleniumContext.getTestDevice;

import java.util.Comparator;
import java.util.List;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.PageSpec;
import com.galenframework.validation.ValidationListener;

import io.wcm.qa.galenium.device.TestDevice;
import io.wcm.qa.galenium.exceptions.GalenLayoutException;
import io.wcm.qa.galenium.galen.GalenHelperUtil;
import io.wcm.qa.galenium.galen.GalenLayoutChecker;
import io.wcm.qa.galenium.imagecomparison.ImageComparisonSpecFactory;
import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.SortedDifferences;
import io.wcm.qa.galenium.selectors.Selector;

/**
 * Make sure an element looks like a sample image.
 */
public class VisualVerification extends ElementBasedVerification {

  private ImageComparisonSpecFactory specFactory;

  /**
   * @param selector to identify element
   */
  public VisualVerification(Selector selector) {
    super(selector);
    setPreVerification(new VisibilityVerification(getSelector()));
    setSpecFactory(new ImageComparisonSpecFactory(getSelector()));
  }

  @Override
  public void addDifference(Difference difference) {
    // handle factory
    getSpecFactory().addDifference(difference);

    // handle self
    super.addDifference(difference);
  }

  /**
   * Add an object to ignore during image comparison.
   * @param selectorToIgnore identify element to ignore
   */
  public void addObjectToIgnore(Selector selectorToIgnore) {
    getSpecFactory().addObjectToIgnore(selectorToIgnore);
  }

  /**
   * If set the scroll position will be taken into account when needed.
   * @param yCorrection vertical scroll position value
   */
  public void correctForSrollPosition(int yCorrection) {
    getSpecFactory().correctForSrollPosition(yCorrection);
  }

  public String getAllowedError() {
    return getSpecFactory().getAllowedError();
  }

  public int getAllowedOffset() {
    return getSpecFactory().getAllowedOffset();
  }

  @Override
  public Comparator<Difference> getComparator() {
    return this.specFactory.getComparator();
  }

  public String getFilename() {
    return getSpecFactory().getFilename();
  }

  public String getFoldername() {
    return getSpecFactory().getFoldername();
  }

  public List<Selector> getObjectsToIgnore() {
    return getSpecFactory().getObjectsToIgnore();
  }

  public String getSectionName() {
    return getSpecFactory().getSectionName();
  }

  public ImageComparisonSpecFactory getSpecFactory() {
    return specFactory;
  }

  public ValidationListener getValidationListener() {
    return getSpecFactory().getValidationListener();
  }

  public boolean isZeroToleranceWarning() {
    return getSpecFactory().isZeroToleranceWarning();
  }

  /**
   * Percentage of pixels which can have different values without failing verification. Overrides allowed error pixel.
   * @param allowedErrorPercentage tolerance in percent
   */
  public void setAllowedErrorPercent(Double allowedErrorPercentage) {
    getSpecFactory().setAllowedErrorPercent(allowedErrorPercentage);
  }

  /**
   * Total number of pixels which can have different values without failing verification. Overrides allowed error
   * percentage.
   * @param allowedErrorPixels tolerance in total number of pixels
   */
  public void setAllowedErrorPixel(Integer allowedErrorPixels) {
    getSpecFactory().setAllowedErrorPixel(allowedErrorPixels);
  }

  /**
   * Allow image to be displaced by a few pixels.
   * @param allowedOffset maximum offset to take into account
   */
  public void setAllowedOffset(int allowedOffset) {
    getSpecFactory().setAllowedOffset(allowedOffset);
  }

  @Override
  public void setComparator(Comparator<Difference> comparator) {
    this.specFactory.setComparator(comparator);
  }

  /**
   * Apply positional corrections in form of a {@link CorrectionsRect}.
   * @param corrections to apply when comparing
   */
  public void setCorrections(CorrectionsRect corrections) {
    getSpecFactory().setCorrections(corrections);
  }

  /**
   * Filename of sample.
   * @param filename new file name
   */
  public void setFilename(String filename) {
    getSpecFactory().setFilename(filename);
  }

  /**
   * Foldername of sample.
   * @param foldername new folder name
   */
  public void setFoldername(String foldername) {
    getSpecFactory().setFoldername(foldername);
  }

  /**
   * Ignore a list of objects when doing image comparison.
   * @param objectsToIgnore list to ignore
   */
  public void setObjectsToIgnore(List<Selector> objectsToIgnore) {
    getSpecFactory().setObjectsToIgnore(objectsToIgnore);
  }

  /**
   * @param sectionName used in reporting
   */
  public void setSectionName(String sectionName) {
    getSpecFactory().setSectionName(sectionName);
  }

  public void setSpecFactory(ImageComparisonSpecFactory specFactory) {
    this.specFactory = specFactory;
  }

  /**
   * @param validationListener listener to use for this comparison
   */
  public void setValidationListener(ValidationListener validationListener) {
    getSpecFactory().setValidationListener(validationListener);
  }

  /**
   * Compare images with no tolerances, but only warn. Allowed pixel percentage and count are ignored along with allowed
   * offset. If this results in a failed verification a warning will be reported and a new sample stored, but the test
   * run will continue.
   * @param zeroToleranceWarning whether to use zero tolerance approach
   */
  public void setZeroToleranceWarning(boolean zeroToleranceWarning) {
    getSpecFactory().setZeroToleranceWarning(zeroToleranceWarning);
  }

  @Override
  protected void afterVerification() {
    // do nothing, because string based sampling does not apply here
  }

  @Override
  protected Boolean doVerification() {
    LayoutReport layoutReport;
    if (getValidationListener() == null) {
      layoutReport = GalenLayoutChecker.checkLayout(getSpecFactory());
    }
    else {
      PageSpec spec = specFactory.getPageSpecInstance();
      TestDevice testDevice = getTestDevice();
      SectionFilter tags = GalenHelperUtil.getSectionFilter(testDevice);
      layoutReport = GalenLayoutChecker.checkLayout(specFactory.getSectionName(), spec, testDevice, tags, getValidationListener());
    }
    try {
      GalenLayoutChecker.handleLayoutReport(layoutReport, getFailureMessage(), getSuccessMessage());
    }
    catch (GalenLayoutException ex) {
      return false;
    }
    return true;
  }

  @Override
  protected String getFailureMessage() {
    return getElementName() + ": Image comparison failed";
  }

  @Override
  protected String getSuccessMessage() {
    return getElementName() + ": Image comparison successful";
  }

  @Override
  protected String sampleValue() {
    // sampling in visual verification is encapsulated in Galen functionalities
    return null;
  }

  @Override
  protected void setDifferences(SortedDifferences differences) {
    // handle factory
    getSpecFactory().clearDifferences();
    getSpecFactory().addAll(differences.getDifferences());

    // handle self
    super.setDifferences(differences);
  }

}
