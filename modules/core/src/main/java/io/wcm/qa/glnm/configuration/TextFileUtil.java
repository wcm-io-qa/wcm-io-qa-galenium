/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2018 wcm.io
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
package io.wcm.qa.glnm.configuration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Utility methods for extracting string values from text files with one value per line.
 */
public final class TextFileUtil {

  private TextFileUtil() {
    // do not instantiate
  }

  /**
   * Read lines from file to String collection.
   * @param file to read from
   * @return list of strings with one string per line
   */
  public static Collection<String> parse(File file) {
    try {
      return FileUtils.readLines(file);
    }
    catch (IOException ex) {
      throw new GaleniumException("when trying to parse text file: " + ex);
    }
  }

  /**
   * Read lines from file to String collection.
   * @param filePath to read from
   * @return list of strings with one string per line
   */
  public static Collection<String> parse(final String filePath) {
    return parse(new File(filePath));
  }

}
