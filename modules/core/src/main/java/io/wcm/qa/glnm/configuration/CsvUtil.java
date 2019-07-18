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

import static io.wcm.qa.glnm.reporting.GaleniumReportUtil.getLogger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Utility methods to parse CSV files.
 */
public final class CsvUtil {

  private static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
  private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withQuote(null).withHeader(new String[] {});

  private CsvUtil() {
    // do not instantiate
  }

  /**
   * Get a parser for CSV file.
   * @param csvFile to get parser for
   * @return parser to access data in CSV file
   */
  public static CSVParser parse(File csvFile) {
    return parse(csvFile, false);
  }

  /**
   * Get a parser for CSV file.
   * @param csvFile to get parser for
   * @param skipHeaderRecord
   * @return parser to access data in CSV file
   */
  public static CSVParser parse(File csvFile, boolean skipHeaderRecord) {
    if (csvFile == null) {
      throw new GaleniumException("error when checking CSV input: file is null");
    }
    if (!csvFile.isFile()) {
      throw new GaleniumException("error when reading CSV file: '" + csvFile.getPath() + "'");
    }
    try {
      CSVFormat format = FORMAT.withSkipHeaderRecord(skipHeaderRecord);
      return CSVParser.parse(csvFile, CHARSET_UTF8, format);
    }
    catch (IOException ex) {
      throw new GaleniumException("error when parsing CSV file: '" + csvFile.getPath() + "'", ex);
    }
  }

  /**
   * Get a parser for CSV file.
   * @param csvFilePath path to file to get parser for
   * @return parser to access data in CSV file
   */
  public static CSVParser parse(String csvFilePath) {
    return parse(new File(csvFilePath));
  }

  /**
   * Populate beans with CSV data.
   * @param csvFile to get input data from
   * @param beanClass type of bean to populate
   * @return collection with one bean per row in CSV
   */
  public static <T> Collection<T> parseToBeans(File csvFile, Class<T> beanClass) {
    Collection<T> result = new ArrayList<>();
    CSVParser parse = parse(csvFile);
    for (CSVRecord csvRecord : parse) {
      Map<String, String> map = csvRecord.toMap();
      getLogger().debug("from csv: " + csvRecord);
      getLogger().debug("from csv: " + ReflectionToStringBuilder.toString(csvRecord));
      getLogger().debug("map from csv: " + ReflectionToStringBuilder.toString(map));
      try {
        T newInstance = beanClass.newInstance();
        BeanUtils.populate(newInstance, map);
        getLogger().debug("populated: " + newInstance);
        result.add(newInstance);
      }
      catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
        throw new GaleniumException("error when constructing bean from CSV: '" + csvFile.getPath() + "'", ex);
      }
    }
    return result;
  }

}
