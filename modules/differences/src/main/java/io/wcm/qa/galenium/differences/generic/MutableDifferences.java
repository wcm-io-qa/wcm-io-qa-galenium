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
package io.wcm.qa.galenium.differences.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import io.wcm.qa.galenium.differences.base.Difference;
import io.wcm.qa.galenium.differences.base.Differences;

/**
 * Holds dimensions of potential differences for samples and supplies them either as file path or property key.
 */
public class MutableDifferences implements Differences {

  private Collection<Difference> differences = new ArrayList<Difference>();

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
   * @return if differences changed after appending
   */
  public boolean addAll(Collection<? extends Difference> toBeAppended) {
    return getDifferences().addAll(toBeAppended);
  }

  /**
   * @param toBeAppended Collection of differences to be appended
   * @return if differences changed after appending
   */
  public boolean addAll(Differences toBeAppended) {
    return CollectionUtils.addAll(getDifferences(), toBeAppended);
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

  public Collection<Difference> getDifferences() {
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
    return joinNamesWith(getDifferences(), separator);
  }

  protected String joinNamesWith(Collection<Difference> collection, String separator) {
    List<String> list = new ArrayList<String>();
    for (Difference difference : collection) {
      list.add(difference.getName());
    }
    return StringUtils.join(list, separator).toLowerCase();
  }

  protected String joinTagsWith(Collection<Difference> collection, String separator) {
    List<String> list = new ArrayList<String>();
    for (Difference difference : collection) {
      list.add(getCleanTag(difference, separator));
    }
    return StringUtils.join(list, separator).toLowerCase();
  }

  protected String getCleanTag(Difference difference, String separator) {
    String tag = difference.getTag();
    tag = tag.toLowerCase();
    return tag.replaceAll("[^\\w\\d_]+", "_");
  }

  protected String joinTagsWith(String separator) {
    return joinTagsWith(getDifferences(), separator);
  }

}