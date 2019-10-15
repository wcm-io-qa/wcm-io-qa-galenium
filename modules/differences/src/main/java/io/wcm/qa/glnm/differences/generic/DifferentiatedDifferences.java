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
package io.wcm.qa.glnm.differences.generic;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.util.DifferenceUtil;

/**
 * Differences are sorted and a cut off value determines which part goes into the folder name and which part into the
 * filename.
 *
 * @since 1.0.0
 */
public class DifferentiatedDifferences extends SortedDifferences {

  private static final Logger LOG = LoggerFactory.getLogger(DifferentiatedDifferences.class);

  private int cutoff = 2;

  /** {@inheritDoc} */
  @Override
  public String asFilePath() {
    Collection<Difference> differences = getDifferences();
    int differencesTotalCount = differences.size();
    int pivotIndex = differencesTotalCount - getCutoff();
    if (pivotIndex < 0 || pivotIndex > differencesTotalCount) {
      LOG.debug("could not differentiate because of illegal cutoff: " + this);
      return super.asFilePath();
    }

    ArrayList<Difference> differencesAsList = new ArrayList<Difference>();
    differencesAsList.addAll(differences);

    String folderPart = DifferenceUtil.joinTagsWith(differencesAsList.subList(0, pivotIndex), "/");
    String filePart = DifferenceUtil.joinTagsWith(differencesAsList.subList(pivotIndex, differencesTotalCount), "/");

    return folderPart + "/" + filePart;
  }

  /**
   * <p>Getter for the field <code>cutoff</code>.</p>
   *
   * @return a int.
   * @since 2.0.0
   */
  public int getCutoff() {
    return cutoff;
  }

  /**
   * <p>Setter for the field <code>cutoff</code>.</p>
   *
   * @param cutoff a int.
   * @since 2.0.0
   */
  public void setCutoff(int cutoff) {
    this.cutoff = cutoff;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return super.toString() + " cutoff: " + getCutoff();
  }

}
