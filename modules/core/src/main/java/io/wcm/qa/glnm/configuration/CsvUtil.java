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
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Utility methods to parse CSV files.
 *
 * @since 1.0.0
 */
public final class CsvUtil {

  private static final Charset CHARSET_UTF8 = Charset.forName("utf-8");
  private static final CSVFormat FORMAT = CSVFormat.DEFAULT.withQuote(null).withHeader(new String[] {});

  private static final Logger LOG = LoggerFactory.getLogger(CsvUtil.class);

  private CsvUtil() {
    // do not instantiate
  }

  /**
   * Get a parser for CSV file.
   *
   * @param csvFile to get parser for
   * @return parser to access data in CSV file
   * @since 3.0.0
   */
  public static CSVParser parse(File csvFile) {
    return parse(csvFile, false);
  }

  /**
   * Get a parser for CSV file.
   *
   * @param csvFile to get parser for
   * @param skipHeaderRecord whether to skip header record while parsing
   * @return parser to access data in CSV file
   * @since 3.0.0
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
   *
   * @param csvFilePath path to file to get parser for
   * @return parser to access data in CSV file
   * @since 3.0.0
   */
  public static CSVParser parse(String csvFilePath) {
    return parse(new File(csvFilePath));
  }

  /**
   * <p>parseToEnums.</p>
   *
   * @param parser a {@link org.apache.commons.csv.CSVParser} object.
   * @return a {@link java.util.List} object.
   */
  public static List<List<Enum>> parseToEnums(CSVParser parser) {
    List<String> headerNames = getHeaderNames(parser);
    List<List<String>> columns = parseToColumns(parser);
    return EnumUtil.toEnumValues(headerNames, columns);
  }

  /**
   * <p>parseToColumns.</p>
   *
   * @param parser a {@link org.apache.commons.csv.CSVParser} object.
   * @return a {@link java.util.List} object.
   */
  public static List<List<String>> parseToColumns(CSVParser parser) {
    List<CSVRecord> records = fetchRecords(parser);
    List<List<String>> columns = new ArrayList<List<String>>();
    for (CSVRecord csvRecord : records) {
      for (int i = 0; i < csvRecord.size(); i++) {
        addToColumn(columns, i, csvRecord.get(i));
      }
    }
    return columns;
  }


  private static List<CSVRecord> fetchRecords(CSVParser parser) {
    return fetchRecords(parser, true);
  }

  private static void addToColumn(List<List<String>> columns, int index, String value) {
    List<String> list = columns.get(index);
    if (list == null) {
      list = new ArrayList<String>();
      columns.set(index, list);
    }
    list.add(value);
  }

  private static List<CSVRecord> fetchRecords(CSVParser parser, boolean failOnEmpty) {
    List<CSVRecord> records;
    try {
      records = parser.getRecords();
    }
    catch (IOException ex) {
      throw new GaleniumException("When fetching records.", ex);
    }
    if (failOnEmpty && records.isEmpty()) {
      throw new GaleniumException("CSV parser returned no records.");
    }
    return records;
  }

  private static List<String> getHeaderNames(CSVParser parser) {
    List<String> headerNames = parser.getHeaderNames();
    if (IterableUtils.isEmpty(headerNames)) {
      throw new GaleniumException("No header names in CSV");
    }
    return headerNames;
  }
  /**
   * Populate beans with CSV data.
   *
   * @param csvFile to get input data from
   * @param beanClass type of bean to populate
   * @param <T> type of bean as generic to create typed collection
   * @return collection with one bean per row in CSV
   * @since 3.0.0
   */
  public static <T> Collection<T> parseToBeans(File csvFile, Class<T> beanClass) {
    Collection<T> result = new ArrayList<>();
    CSVParser parse = parse(csvFile);
    for (CSVRecord csvRecord : parse) {
      Map<String, String> map = csvRecord.toMap();
      LOG.debug("from csv: " + csvRecord);
      LOG.debug("from csv: " + ReflectionToStringBuilder.toString(csvRecord));
      LOG.debug("map from csv: " + ReflectionToStringBuilder.toString(map));
      try {
        T newInstance = beanClass.newInstance();
        BeanUtils.populate(newInstance, map);
        LOG.debug("populated: " + newInstance);
        result.add(newInstance);
      }
      catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
        throw new GaleniumException("error when constructing bean from CSV: '" + csvFile.getPath() + "'", ex);
      }
    }
    return result;
  }

}
