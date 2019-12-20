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

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import io.wcm.qa.glnm.exceptions.GaleniumException;


public class EnumUtilTest {

  private static final String NAME_CONSTANT_TWO = "CONSTANT_TWO";
  private static final String NAME_CONSTANT_ONE = "CONSTANT_ONE";
  private static final String NAME_TEST_ENUM = "io.wcm.qa.glnm.configuration.TestEnum";
  private static final String NAME_ILLEGAL_ENUM = "NOT_AN_ENUM_NAME";

  @Test
  public void testEnumValues() {
    List<Enum> enumValues = EnumUtil.toEnumValues(NAME_TEST_ENUM, asList(NAME_CONSTANT_ONE, NAME_CONSTANT_TWO));
    assertThat(enumValues, hasSize(2));
    assertThat(enumValues, contains(TestEnum.CONSTANT_ONE, TestEnum.CONSTANT_TWO));
  }

  @Test
  public void testEnumValuesReverse() {
    List<Enum> enumValues = EnumUtil.toEnumValues(NAME_TEST_ENUM, asList(NAME_CONSTANT_TWO, NAME_CONSTANT_ONE));
    assertThat(enumValues, hasSize(2));
    assertThat(enumValues, contains(TestEnum.CONSTANT_TWO, TestEnum.CONSTANT_ONE));
  }

  @Test
  public void testEnumValuesWithNull() {
    List<Enum> enumValues = EnumUtil.toEnumValues(NAME_TEST_ENUM, asList(NAME_CONSTANT_TWO, null, NAME_CONSTANT_ONE, " ", ""));
    assertThat(enumValues, hasSize(5));
    assertThat(enumValues, contains(TestEnum.CONSTANT_TWO, null, TestEnum.CONSTANT_ONE, null, null));
  }

  @Test
  public void testIllegalEnumName() {
    assertThrows(
        GaleniumException.class,
        new Executable() {
          @Override
          public void execute() throws Throwable {
            EnumUtil.toEnumValues(NAME_ILLEGAL_ENUM, asList(NAME_CONSTANT_TWO));
          }
        },
        "Illegal name should throw exception");
  }

  @Test
  public void testNullList() {
    List<Enum> enumValues = EnumUtil.toEnumValues(NAME_TEST_ENUM, null);
    assertThat(enumValues, hasSize(0));
  }

}
