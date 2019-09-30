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
package io.wcm.qa.glnm.selectors;

import com.galenframework.specs.page.CorrectionsRect;
import com.galenframework.specs.page.CorrectionsRect.Correction;
import com.galenframework.specs.page.Locator;

/**
 * Convenience wrapper to add corrections without modifying original locator.
 */
public class LocatorCorrectionsWrapper extends Locator {

  private CorrectionsRect additionalCorrections;

  /**
   * @param locator locator to delegate everything except additionalCorrections to
   * @param corrections additional corrections to use on this locator
   */
  public LocatorCorrectionsWrapper(Locator locator, CorrectionsRect corrections) {
    super(locator.getLocatorType(), locator.getLocatorValue(), locator.getIndex());
    setParent(locator.getParent());
    setAdditionalCorrections(corrections);
  }

  public CorrectionsRect getAdditionalCorrections() {
    return additionalCorrections;
  }

  @Override
  public CorrectionsRect getCorrections() {
    return combinedCorrections(super.getCorrections(), getAdditionalCorrections());
  }

  public void setAdditionalCorrections(CorrectionsRect additionalCorrections) {
    this.additionalCorrections = additionalCorrections;
  }

  private Correction combinedCorrection(Correction c1, Correction c2) {
    return new CombinedCorrection(c1, c2);
  }

  private CorrectionsRect combinedCorrections(CorrectionsRect cr1, CorrectionsRect cr2) {
    if (cr1 == null) {
      return cr2;
    }
    if (cr2 == null) {
      return cr1;
    }
    Correction left = combinedCorrection(cr1.getLeft(), cr2.getLeft());
    Correction top = combinedCorrection(cr1.getTop(), cr2.getTop());
    Correction width = combinedCorrection(cr1.getWidth(), cr2.getWidth());
    Correction height = combinedCorrection(cr1.getHeight(), cr2.getHeight());
    return new CorrectionsRect(left, top, width, height);
  }

  /**
   * Combines two corrections into one.
   */
  private class CombinedCorrection extends Correction {

    private Correction additionalCorrection;

    /**
     * @param original applied first
     * @param additional applied to result of first
     */
    CombinedCorrection(Correction original, Correction additional) {
      super(original.getValue(), original.getType());
      additionalCorrection = additional;
    }

    @Override
    public int correct(int oldValue) {
      int correctedValue = super.correct(oldValue);
      return additionalCorrection.correct(correctedValue);
    }
  }
}
