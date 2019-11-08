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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.net.URL;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.galenframework.specs.page.PageSection;
import com.galenframework.specs.page.PageSpec;


public class GalenSpecParsingProviderTest {

  @Test
  public void testSuccessfulParsing() {
    URL resource = getClass().getClassLoader().getResource("test.gspec");
    assertThat(resource, is(notNullValue()));
    GalenSpecParsingProvider specProvider = new GalenSpecParsingProvider(resource.getPath());

    PageSpec pageSpec = specProvider.getPageSpec();
    assertThat(pageSpec, hasProperty("sections", hasSize(2)));

    List<PageSection> sections = pageSpec.getSections();
    assertThat(sections.get(0), allOf(
            isCalled("Header section"),
            hasProperty("sections", Matchers.hasSize(2))));
    assertThat(sections.get(1), allOf(
            isCalled("Navigation section"),
            hasProperty("sections", Matchers.is(Matchers.empty()))));
  }

  private static Matcher<PageSection> isCalled(String name) {
    return Matchers.hasProperty("name", Matchers.is(name));
  }
}
