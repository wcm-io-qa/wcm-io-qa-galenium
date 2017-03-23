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
package io.wcm.qa.galenium.verification;

import java.util.List;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.specs.page.CorrectionsRect;

import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.MutableDifferences;
import io.wcm.qa.galenium.sampling.images.DifferenceAwareIcsFactory;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GalenLayoutChecker;

public class VisualVerification extends ElementBasedVerification {

  private DifferenceAwareIcsFactory specFactory;

  public VisualVerification(Selector selector) {
    super(selector);
    setPreVerification(new VisibilityVerification(getSelector()));
    setSpecFactory(new DifferenceAwareIcsFactory(getSelector()));
  }

  @Override
  public void addDifference(Difference difference) {
    // handle factory
    getSpecFactory().addDifference(difference);

    // handle self
    super.addDifference(difference);
  }

  public void addObjectToIgnore(Selector selectorToIgnore) {
    getSpecFactory().addObjectToIgnore(selectorToIgnore);
  }

  public void correctForSrollPosition(int yCorrection) {
    getSpecFactory().correctForSrollPosition(yCorrection);
  }

  public String getAllowedError() {
    return getSpecFactory().getAllowedError();
  }

  public int getAllowedOffset() {
    return getSpecFactory().getAllowedOffset();
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

  public DifferenceAwareIcsFactory getSpecFactory() {
    return specFactory;
  }

  public boolean isZeroToleranceWarning() {
    return getSpecFactory().isZeroToleranceWarning();
  }

  public void setAllowedErrorPercent(Double allowedErrorPercentage) {
    getSpecFactory().setAllowedErrorPercent(allowedErrorPercentage);
  }

  public void setAllowedErrorPixel(Integer allowedErrorPixels) {
    getSpecFactory().setAllowedErrorPixel(allowedErrorPixels);
  }

  public void setAllowedOffset(int allowedOffset) {
    getSpecFactory().setAllowedOffset(allowedOffset);
  }

  public void setCorrections(CorrectionsRect corrections) {
    getSpecFactory().setCorrections(corrections);
  }

  public void setFilename(String filename) {
    getSpecFactory().setFilename(filename);
  }

  public void setFoldername(String foldername) {
    getSpecFactory().setFoldername(foldername);
  }

  public void setObjectsToIgnore(List<Selector> objectsToIgnore) {
    getSpecFactory().setObjectsToIgnore(objectsToIgnore);
  }

  public void setRelativeImagePath(String relativeImagePath) {
    this.specFactory.setRelativeImagePath(relativeImagePath);
  }

  public void setSectionName(String sectionName) {
    getSpecFactory().setSectionName(sectionName);
  }

  public void setSpecFactory(DifferenceAwareIcsFactory specFactory) {
    this.specFactory = specFactory;
  }

  public void setZeroToleranceWarning(boolean zeroToleranceWarning) {
    getSpecFactory().setZeroToleranceWarning(zeroToleranceWarning);
  }

  @Override
  protected Boolean doVerification() {
    LayoutReport layoutReport = GalenLayoutChecker.checkLayout(getSpecFactory());
    try {
      GalenLayoutChecker.handleLayoutReport(layoutReport, getFailureMessage(), getSuccessMessage());
    }
    catch (Exception ex) {
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
  protected void setDifferences(MutableDifferences differences) {
    // handle factory
    getSpecFactory().clearDifferences();
    getSpecFactory().addAll(differences.getDifferences());

    // handle self
    super.setDifferences(differences);
  }

}
