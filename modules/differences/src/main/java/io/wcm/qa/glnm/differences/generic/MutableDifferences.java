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
import java.util.Iterator;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.base.Differences;
import io.wcm.qa.glnm.differences.util.DifferenceUtil;

/**
 * Holds dimensions of potential differences for samples and supplies them either as file path or property key.
 */
public class MutableDifferences implements Differences {

  private Collection<Difference> differences = new ArrayList<Difference>();

  /**
   * See {@link ArrayList#add(Object)}
   * @param difference to be appended
   * @return true if adding changed anything
   */
  public boolean add(Difference difference) {
    if (difference == null) {
      throw new IllegalArgumentException("cannot add null to MutableDifferences.");
    }
    return getDifferences().add(difference);
  }

  /**
   * See {@link ArrayList#addAll(Collection)}
   * @param toBeAppended Collection of differences to be appended
   * @return if differences changed after appending
   */
  public boolean addAll(Collection<? extends Difference> toBeAppended) {
    boolean changed = false;
    for (Difference difference : toBeAppended) {
      if (add(difference)) {
        changed = true;
      }
    }
    return changed;
  }

  /**
   * @param toBeAppended Collection of differences to be appended
   * @return if differences changed after appending
   */
  public boolean addAll(Iterable<? extends Difference> toBeAppended) {
    boolean changed = false;
    for (Difference difference : toBeAppended) {
      if (add(difference)) {
        changed = true;
      }
    }
    return changed;
  }

  @Override
  public String asFilePath() {
    return joinTagsWith("/");
  }

  @Override
  public String asPropertyKey() {
    return joinTagsWith(".");
  }

  /**
   * See {@link ArrayList#clear()}
   */
  public void clear() {
    getDifferences().clear();
  }

  public Collection<Difference> getDifferences() {
    return differences;
  }

  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  /**
   * See {@link ArrayList#remove(Object)}
   * @param difference to be removed
   * @return true if difference existed and was removed
   */
  public boolean remove(Difference difference) {
    return getDifferences().remove(difference);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("differences: [");
    stringBuilder.append(joinNamesWith("]|["));
    stringBuilder.append("], asPropertyKey: '");
    stringBuilder.append(asPropertyKey());
    stringBuilder.append("', asFilePath: '");
    stringBuilder.append(asFilePath());
    stringBuilder.append("'");
    return stringBuilder.toString();
  }

  private String joinNamesWith(String separator) {
    return DifferenceUtil.joinNamesWith(getDifferences(), separator);
  }

  protected String joinTagsWith(String separator) {
    return DifferenceUtil.joinTagsWith(getDifferences(), separator);
  }

}
