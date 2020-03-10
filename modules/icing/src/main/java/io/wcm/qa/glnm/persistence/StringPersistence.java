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
package io.wcm.qa.glnm.persistence;

import io.wcm.qa.glnm.differences.base.Differences;

/**
 * <p>StringPersistence class.</p>
 *
 * @since 5.0.0
 */
class StringPersistence implements SamplePersistence<String> {


  private final SampleReader<String> reader;

  private final SampleWriter<String> writer;

  /**
   * <p>Constructor for StringPersistence.</p>
   *
   * @param clazz a {@link java.lang.Class} object.
   */
  StringPersistence(Class clazz) {
    reader = new SampleStringReader(clazz);
    writer = new SampleStringWriter(clazz);
  }
  /** {@inheritDoc} */
  @Override
  public SampleReader<String> reader() {
    return reader;
  }

  /** {@inheritDoc} */
  @Override
  public SampleWriter<String> writer() {
    return writer;
  }

  private static class SampleStringReader extends SamplePropertiesReaderBase<String> {

    protected SampleStringReader(Class clazz) {
      super(clazz);
    }

    @Override
    public String readSample(Differences differences) {
      return readSampleAsString(differences);
    }


  }

  private static class SampleStringWriter extends SamplePropertiesWriterBase<String> {

    protected SampleStringWriter(Class clazz) {
      super(clazz);
    }

    @Override
    public void writeSample(Differences differences, String sample) {
      writeSampleAsString(differences, sample);
    }

  }

}
