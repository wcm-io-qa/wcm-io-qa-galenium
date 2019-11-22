/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
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
package io.wcm.qa.glnm.verification.persistence;

import io.wcm.qa.glnm.differences.base.Differences;

/**
 * <p>IntegerPersistence class.</p>
 *
 * @since 4.0.0
 */
public class IntegerPersistence implements SamplePersistence<Integer> {

  private SampleReader<Integer> reader;

  private SampleWriter<Integer> writer;

  /**
   * <p>Constructor for IntegerPersistence.</p>
   *
   * @param clazz a {@link java.lang.Class} object.
   */
  public IntegerPersistence(Class clazz) {
    reader = new IntegerReader(clazz);
    writer = new IntegerWriter(clazz);
  }
  /** {@inheritDoc} */
  @Override
  public SampleReader<Integer> reader() {
    return reader;
  }

  /** {@inheritDoc} */
  @Override
  public SampleWriter<Integer> writer() {
    return writer;
  }

  private static final class IntegerReader extends SamplePropertiesReaderBase<Integer> {

    private IntegerReader(Class clazz) {
      super(clazz);
    }

    @Override
    public Integer readSample(Differences differences) {
      return Integer.parseInt(readSampleAsString(differences));
    }
  }

  private static final class IntegerWriter extends SamplePropertiesWriterBase<Integer> {

    private IntegerWriter(Class clazz) {
      super(clazz);
    }

    @Override
    public void writeSample(Differences differences, Integer sample) {
      writeSampleAsString(differences, Integer.toString(sample));
    }
  }

}
