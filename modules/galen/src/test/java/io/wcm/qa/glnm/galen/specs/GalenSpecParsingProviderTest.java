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
package io.wcm.qa.glnm.galen.specs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.glnm.exceptions.GaleniumException;


public class GalenSpecParsingProviderTest {

  private static final String TEST_BROKEN_GSPEC = "test-broken.gspec";
  private static final String TEST_GSPEC = "test.gspec";
  private static final String TEST_NON_EXISTENT_GSPEC = "test-non-existent.gspec";

  @Test
  public void testParsingErrorBrokenSpec() {
    assertThrows(GaleniumException.class, new Executable() {

      @Override
      public void execute() throws Throwable {
        GalenSpecParsingProvider specProvider = new GalenSpecParsingProvider(TEST_BROKEN_GSPEC);
        // getting the page spec will throw the exception
        specProvider.getPageSpec();
      }
    });
  }

  @Test
  public void testParsingErrorWrongPath() {
    assertThrows(GaleniumException.class, new Executable() {

      @Override
      public void execute() throws Throwable {
        GalenParsing.fromPath(TEST_NON_EXISTENT_GSPEC);
      }
    });
  }

  @Test
  public void testSuccessfulParsing() {
    GalenSpecParsingProvider specProvider = new GalenSpecParsingProvider(TEST_GSPEC);

    PageSpec pageSpec = specProvider.getPageSpec();
    assertThat(pageSpec, hasProperty("sections", hasSize(2)));

    List<PageSection> sections = pageSpec.getSections();
    assertThat(sections.get(0),
        both(
            isNamed("Header section"))
                .and(
                    hasProperty("sections", Matchers.hasSize(2))));
    assertThat(sections.get(1),
        both(
            isNamed("Navigation section"))
                .and(
                    hasProperty("sections", is(empty()))));
  }

  private static Matcher<PageSection> isNamed(String name) {
    return Matchers.hasProperty("name", Matchers.is(name));
  }
}
