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
package io.wcm.qa.galenium.differences.difference;

import io.wcm.qa.galenium.differences.base.DifferenceBase;

/**
 * Integer based difference for use with index or count values.
 *
 * @since 1.0.0
 */
public class IntegerDifference extends DifferenceBase {

  private int index;

  /**
   * Constructor.
   *
   * @since 2.0.0
   */
  public IntegerDifference() {
    this(0);
  }

  /**
   * <p>Constructor for IntegerDifference.</p>
   *
   * @param index index to use
   * @since 2.0.0
   */
  public IntegerDifference(int index) {
    setIndex(index);
  }

  /**
   * Decrement or count down.
   *
   * @since 2.0.0
   */
  public void decrement() {
    setIndex(getIndex() - 1);
  }

  /**
   * <p>Getter for the field <code>index</code>.</p>
   *
   * @return a int.
   * @since 2.0.0
   */
  public int getIndex() {
    return index;
  }

  /**
   * Increment aka increase index by one.
   *
   * @since 2.0.0
   */
  public void increment() {
    setIndex(getIndex() + 1);
  }

  /**
   * <p>Setter for the field <code>index</code>.</p>
   *
   * @param index a int.
   * @since 2.0.0
   */
  public void setIndex(int index) {
    this.index = index;
  }

  @Override
  protected String getRawTag() {
    return Integer.toString(getIndex());
  }

}
