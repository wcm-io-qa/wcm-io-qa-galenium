/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2020 wcm.io
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
package io.wcm.qa.glnm.junit.combinatorial;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

/**
 * Multiplies all sources with each other.
 *
 * @since 5.0.0
 */
@Retention(RUNTIME)
@Target(METHOD)
@TestTemplate
@ExtendWith(CartesianProductProvider.class)
public @interface CartesianProduct {

  /**
   * @see ParameterizedTest#DEFAULT_DISPLAY_NAME
   */
  String DEFAULT_DISPLAY_NAME = "{displayName}[{index}] {arguments}";

  /**
   * @see ParameterizedTest#name()
   * @return name to use in reporting
   */
  String name() default DEFAULT_DISPLAY_NAME;
}
