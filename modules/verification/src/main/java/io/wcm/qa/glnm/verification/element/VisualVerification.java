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
package io.wcm.qa.glnm.verification.element;

import static io.wcm.qa.glnm.util.GaleniumContext.getTestDevice;

import java.util.Comparator;
import java.util.List;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.generic.SortedDifferences;
import io.wcm.qa.glnm.exceptions.GalenLayoutException;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.galen.GalenHelperUtil;
import io.wcm.qa.glnm.galen.GalenLayoutChecker;
import io.wcm.qa.glnm.imagecomparison.IcValidationListener;
import io.wcm.qa.glnm.imagecomparison.IcsFactory;
import io.wcm.qa.glnm.imagecomparison.ImageComparisonSpecDefinition;
import io.wcm.qa.glnm.selectors.base.Selector;
import io.wcm.qa.glnm.verification.base.VerificationBase;

/**
 * Make sure an element looks like a sample image.
 */
public class VisualVerification extends VerificationBase<Object> {

  private ImageComparisonSpecDefinition specDefinition;

  /**
   * @param selector to identify element
   */
  public VisualVerification(Selector selector) {
    super("Visual(" + selector.elementName() + ")");
    setPreVerification(new VisibilityVerification(selector));
    setSpecDefinition(new ImageComparisonSpecDefinition(selector));
  }

  @Override
  public VisualVerification addDifference(Difference difference) {
    // handle factory
    getSpecDefinition().addDifference(difference);

    // handle self
    super.addDifference(difference);
    return this;
  }

  /**
   * Add an object to ignore during image comparison.
   * @param selectorToIgnore identify element to ignore
   */
  public void addObjectToIgnore(Selector selectorToIgnore) {
    getSpecDefinition().addObjectToIgnore(selectorToIgnore);
  }

  /**
   * If set the scroll position will be taken into account when needed.
   * @param yCorrection vertical scroll position value
   */
  public void correctForSrollPosition(int yCorrection) {
    getSpecDefinition().correctForSrollPosition(yCorrection);
  }

  public String getAllowedError() {
    return getSpecDefinition().getAllowedError();
  }

  public int getAllowedOffset() {
    return getSpecDefinition().getAllowedOffset();
  }

  public String getFilename() {
    return getSpecDefinition().getFilename();
  }

  public String getFoldername() {
    return getSpecDefinition().getFoldername();
  }

  public List<Selector> getObjectsToIgnore() {
    return getSpecDefinition().getObjectsToIgnore();
  }

  public String getSectionName() {
    return getSpecDefinition().getSectionName();
  }

  public ImageComparisonSpecDefinition getSpecDefinition() {
    return specDefinition;
  }

  public boolean isZeroToleranceWarning() {
    return getSpecDefinition().isZeroToleranceWarning();
  }

  /**
   * Percentage of pixels which can have different values without failing verification. Overrides allowed error pixel.
   * @param allowedErrorPercentage tolerance in percent
   */
  public void setAllowedErrorPercent(Double allowedErrorPercentage) {
    getSpecDefinition().setAllowedErrorPercent(allowedErrorPercentage);
  }

  /**
   * Total number of pixels which can have different values without failing verification. Overrides allowed error
   * percentage.
   * @param allowedErrorPixels tolerance in total number of pixels
   */
  public void setAllowedErrorPixel(Integer allowedErrorPixels) {
    getSpecDefinition().setAllowedErrorPixel(allowedErrorPixels);
  }

  /**
   * Allow image to be displaced by a few pixels.
   * @param allowedOffset maximum offset to take into account
   */
  public void setAllowedOffset(int allowedOffset) {
    getSpecDefinition().setAllowedOffset(allowedOffset);
  }

  @Override
  public void setComparator(Comparator<Difference> comparator) {
    this.specDefinition.setComparator(comparator);
  }

  /**
   * Apply positional corrections in form of a {@link CorrectionsRect}.
   * @param corrections to apply when comparing
   */
  public void setCorrections(CorrectionsRect corrections) {
    getSpecDefinition().setCorrections(corrections);
  }

  /**
   * Filename of sample.
   * @param filename new file name
   */
  public void setFilename(String filename) {
    getSpecDefinition().setFilename(filename);
  }

  /**
   * Foldername of sample.
   * @param foldername new folder name
   */
  public void setFoldername(String foldername) {
    getSpecDefinition().setFoldername(foldername);
  }

  /**
   * Ignore a list of objects when doing image comparison.
   * @param objectsToIgnore list to ignore
   */
  public void setObjectsToIgnore(List<Selector> objectsToIgnore) {
    getSpecDefinition().setObjectsToIgnore(objectsToIgnore);
  }

  /**
   * @param sectionName used in reporting
   */
  public void setSectionName(String sectionName) {
    getSpecDefinition().setSectionName(sectionName);
  }

  public void setSpecDefinition(ImageComparisonSpecDefinition def) {
    this.specDefinition = def;
  }

  /**
   * Compare images with no tolerances, but only warn. Allowed pixel percentage and count are ignored along with allowed
   * offset. If this results in a failed verification a warning will be reported and a new sample stored, but the test
   * run will continue.
   * @param zeroToleranceWarning whether to use zero tolerance approach
   */
  public void setZeroToleranceWarning(boolean zeroToleranceWarning) {
    getSpecDefinition().setZeroToleranceWarning(zeroToleranceWarning);
  }

  @Override
  protected void afterVerification() {
    getLogger().debug("done verifying: " + getVerificationName());
  }

  @Override
  protected boolean doVerification() {
    LayoutReport layoutReport;

    PageSpec spec = IcsFactory.getPageSpec(getSpecDefinition());
    TestDevice testDevice = getTestDevice();
    SectionFilter tags = GalenHelperUtil.getSectionFilter(testDevice);
    layoutReport = GalenLayoutChecker.checkLayout(getSpecDefinition().getSectionName(), spec, testDevice, tags, new IcValidationListener());
    try {
      GalenLayoutChecker.handleLayoutReport(layoutReport, getFailureMessage(), getSuccessMessage());
    }
    catch (GalenLayoutException ex) {
      getLogger().debug("image comparison layout", ex);
      return false;
    }
    return true;
  }

  @Override
  protected String getFailureMessage() {
    return getVerificationName() + ": Image comparison failed";
  }

  @Override
  protected String getSuccessMessage() {
    return getVerificationName() + ": Image comparison successful";
  }

  @Override
  protected Object initExpectedValue() {
    throw new GaleniumException("expected value handled in spec factory.");
  }

  @Override
  protected void persistSample(String key, Object newValue) {
    throw new GaleniumException("persistence handled in validation listener.");
  }

  @Override
  protected String sampleValue() {
    throw new GaleniumException("sampling handled by Galen when evalutation spec.");
  }

  @Override
  protected void setDifferences(SortedDifferences differences) {
    // handle factory
    getSpecDefinition().clearDifferences();
    for (Difference difference : differences.getDifferences()) {
      getSpecDefinition().addDifference(difference);
    }

    // handle self
    super.setDifferences(differences);
  }

}
