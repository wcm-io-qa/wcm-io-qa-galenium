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
package io.wcm.qa.galenium.sampling.differences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Holds dimensions of potential differences for samples and supplies them either as file path or property key.
 */
public class MutableDifferences implements Differences {

  private List<Difference> differences = new ArrayList<Difference>();

  /**
   * See {@link ArrayList#add(Object)}
   * @param difference to be appended
   * @return true
   */
  public boolean add(Difference difference) {
    return getDifferences().add(difference);
  }

  /**
   * See {@link ArrayList#addAll(Collection)}
   * @param toBeAppended Collection of differences to be appended
   * @return true
   */
  public boolean addAll(Collection<? extends Difference> toBeAppended) {
    return getDifferences().addAll(toBeAppended);
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.sampling.Differences#asFilePath()
   */
  @Override
  public String asFilePath() {
    return joinTagsWith("/");
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.sampling.Differences#asPropertyKey()
   */
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

  public List<Difference> getDifferences() {
    return differences;
  }

  /* (non-Javadoc)
   * @see io.wcm.qa.galenium.sampling.Differences#iterator()
   */
  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  /**
   * See {@link ArrayList#remove(Object)}
   * @param difference
   * @return true if difference existed and was removed
   */
  public boolean remove(Difference difference) {
    return getDifferences().remove(difference);
  }

  private String joinTagsWith(String separator) {
    StringBuilder joinedTags = new StringBuilder();
    for (Difference difference : this) {
      joinedTags.append(difference.getTag());
      joinedTags.append(separator);
    }
    return joinedTags.toString();
  }

}
