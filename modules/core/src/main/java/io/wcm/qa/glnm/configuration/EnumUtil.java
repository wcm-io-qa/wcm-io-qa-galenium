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
package io.wcm.qa.glnm.configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.wcm.qa.glnm.exceptions.GaleniumException;

/**
 * Utility methods using reflection to construct {@link java.lang.Enum} values from Strings.
 *
 * @since 4.0.0
 */
public final class EnumUtil {

  private EnumUtil() {
    // do not instantiate
  }

  /**
   * <p>toEnumValues.</p>
   *
   * @param headerNames a {@link java.util.List} object.
   * @param columns a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   */
  public static List<List<Enum>> toEnumValues(List<String> headerNames, List<List<String>> columns) {
    List<List<Enum>> result = new ArrayList<List<Enum>>();
    for (int i = 0; i < headerNames.size(); i++) {
      result.add(toEnumValues(headerNames.get(i), columns.get(i)));
    }
    return result;
  }

  /**
   * <p>toEnumValues.</p>
   *
   * @param enumName a {@link java.lang.String} object.
   * @param constantNames a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   */
  public static List<Enum> toEnumValues(String enumName, List<String> constantNames) {

    if (IterableUtils.isEmpty(constantNames)) {
      // no names means no values
      return Collections.emptyList();
    }

    return toEnumValues(getEnumClass(enumName), constantNames);
  }

  private static Class classForName(String name) {
    try {
      return Class.forName(name);
    }
    catch (ClassNotFoundException ex) {
      throw new GaleniumException("When loading Enum class", ex);
    }
  }

  @SuppressWarnings("unchecked")
  private static Class<Enum> ensureEnum(String name, Class<?> candidateClass) {
    if (candidateClass.isEnum()) {
      return (Class<Enum>)candidateClass;
    }
    throw new GaleniumException("CSV header name is not an Enum: '" + name + "'");
  }

  private static Class<Enum> getEnumClass(String name) {
    return ensureEnum(name, classForName(name));
  }

  @SuppressWarnings("unchecked")
  private static Enum toEnumValue(Class<Enum> enumType, String enumConstantName) {
    if (StringUtils.isBlank(enumConstantName)) {
      // no name means no value
      return null;
    }

    Enum enumValue = EnumUtils.getEnumIgnoreCase(enumType, enumConstantName);
    if (enumValue == null) {
      StringBuilder msg = new StringBuilder()
          .append("CSV value '")
          .append(enumConstantName)
          .append("' not in Enum (")
          .append(enumType.getName())
          .append("): [")
          .append(ToStringBuilder.reflectionToString(EnumUtils.getEnumList(enumType), ToStringStyle.SIMPLE_STYLE))
          .append("]");
      throw new GaleniumException(msg.toString());
    }
    return enumValue;
  }

  private static List<Enum> toEnumValues(Class<Enum> enumType, List<String> allValuesInColumn) {
    List<Enum> result = new ArrayList<Enum>();
    for (String csvValue : allValuesInColumn) {
      result.add(toEnumValue(enumType, csvValue));
    }
    return result;
  }

}
