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

import java.util.List;

import io.wcm.qa.glnm.differences.base.Differences;

/**
 * <p>StringListPersistence class.</p>
 *
 * @since 5.0.0
 */
class StringListPersistence implements SamplePersistence<List<String>> {

  private final SampleReader<List<String>> reader;
  private final SampleWriter<List<String>> writer;

  /**
   * <p>Constructor for StringListPersistence.</p>
   *
   * @param clazz a {@link java.lang.Class} object.
   */
  StringListPersistence(Class clazz) {
    reader = new SampleStringListReader(clazz);
    writer = new SampleStringListWriter(clazz);
  }

  /** {@inheritDoc} */
  @Override
  public SampleReader<List<String>> reader() {
    return reader;
  }

  /** {@inheritDoc} */
  @Override
  public SampleWriter<List<String>> writer() {
    return writer;
  }

  private static class SampleStringListReader extends SamplesForClass implements SampleReader<List<String>> {

    protected SampleStringListReader(Class clazz) {
      super(clazz);
    }

    @Override
    public List<String> readSample(Differences differences) {
      return PersistenceUtil.getLinesFromTextFile(getSamplingClass(), differences);
    }

  }

  private static class SampleStringListWriter extends SamplesForClass implements SampleWriter<List<String>> {

    protected SampleStringListWriter(Class clazz) {
      super(clazz);
    }

    @Override
    public void writeSample(Differences differences, List<String> sample) {
      PersistenceUtil.writeLinesToTextFile(getSamplingClass(), differences, sample);
    }

  }

}
