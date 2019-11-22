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
 * <p>BooleanPersistence class.</p>
 *
 * @since 4.0.0
 */
class BooleanPersistence implements SamplePersistence<Boolean> {

  private SampleReader<Boolean> reader;

  private SampleWriter<Boolean> writer;

  /**
   * <p>Constructor for BooleanPersistence.</p>
   *
   * @param clazz a {@link java.lang.Class} object.
   */
  BooleanPersistence(Class clazz) {
    reader = new BooleanReader(clazz);
    writer = new BooleanWriter(clazz);
  }

  /** {@inheritDoc} */
  @Override
  public SampleReader<Boolean> reader() {
    return reader;
  }

  /** {@inheritDoc} */
  @Override
  public SampleWriter<Boolean> writer() {
    return writer;
  }

  private static final class BooleanReader extends SamplePropertiesReaderBase<Boolean> {

    private BooleanReader(Class clazz) {
      super(clazz);
    }

    @Override
    public Boolean readSample(Differences differences) {
      return Boolean.valueOf(readSampleAsString(differences));
    }
  }

  private final class BooleanWriter extends SamplePropertiesWriterBase<Boolean> {

    private BooleanWriter(Class clazz) {
      super(clazz);
    }

    @Override
    public void writeSample(Differences differences, Boolean sample) {
      writeSampleAsString(differences, Boolean.toString(sample));
    }
  }

}
