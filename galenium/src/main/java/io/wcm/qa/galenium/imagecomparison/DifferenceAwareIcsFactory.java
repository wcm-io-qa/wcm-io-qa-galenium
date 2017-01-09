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

import io.wcm.qa.galenium.sampling.differences.Difference;
import io.wcm.qa.galenium.sampling.differences.Differences;
import io.wcm.qa.galenium.sampling.differences.MutableDifferences;
import io.wcm.qa.galenium.selectors.Selector;
import io.wcm.qa.galenium.util.GaleniumConfiguration;

import java.util.Collection;

/**
 * Image comparison spec factory using {@link Differences}.
 */
public class DifferenceAwareIcsFactory extends ImageComparisonSpecFactory {

  private MutableDifferences differences = new MutableDifferences();

  /**
   * @param selector selects element to use in image comparison
   */
  public DifferenceAwareIcsFactory(Selector selector) {
    super(selector);
  }

  /**
   * @param selector selects element to use in image comparison
   * @param elementName used in spec and image filename
   */
  public DifferenceAwareIcsFactory(Selector selector, String elementName) {
    super(selector, elementName);
  }

  /**
   * @param toBeAppended differences to be appended
   * @return true if this list changed as a result of the call
   */
  public boolean addAll(Collection<? extends Difference> toBeAppended) {
    return this.differences.addAll(toBeAppended);
  }

  /**
   * @param difference appends a difference
   */
  public void addDifference(Difference difference) {
    differences.add(difference);
  }

  @Override
  public String getFoldername() {
    return GaleniumConfiguration.getGalenSpecPath() + "/" + differences.asFilePath();
  }
}
