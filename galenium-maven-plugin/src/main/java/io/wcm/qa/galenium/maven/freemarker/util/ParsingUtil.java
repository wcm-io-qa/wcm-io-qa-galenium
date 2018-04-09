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
package io.wcm.qa.galenium.maven.freemarker.util;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import com.galenframework.page.Page;
import com.galenframework.speclang2.pagespec.SectionFilter;
import com.galenframework.specs.page.Locator;
import com.galenframework.specs.page.PageSpec;

import io.wcm.qa.galenium.exceptions.GaleniumException;
import io.wcm.qa.galenium.maven.mock.MockPage;
import io.wcm.qa.galenium.selectors.NestedSelector;
import io.wcm.qa.galenium.util.GalenHelperUtil;

public final class ParsingUtil {

  private static final GalenSpecFileFilter GALEN_SPEC_FILE_FILTER = new GalenSpecFileFilter();

  private ParsingUtil() {
    // do not instantiate
  }

  public static Collection<NestedSelector> getSelectorsFromSpec(File specFile) {
    PageSpec galenSpec = readSpec(specFile);
    Collection<NestedSelector> selectors = GalenHelperUtil.getObjects(galenSpec);
    return selectors;
  }

  public static File[] getSpecFiles(File fromDir) {
    return fromDir.listFiles(GALEN_SPEC_FILE_FILTER);
  }

  public static PageSpec readSpec(File specFile) {
    if (specFile == null) {
      throw new GaleniumException("cannot read spec from null file.");
    }
    String specPath = specFile.getPath();
    SectionFilter tags = new SectionFilter(emptyList(), emptyList());
    Page page = new MockPage();
    Properties properties = new Properties();
    Map<String, Object> jsVars = emptyMap();
    Map<String, Locator> objects = null;
    return GalenHelperUtil.readSpec(specPath, tags, page, properties, jsVars, objects);
  }

  private static final class GalenSpecFileFilter implements FilenameFilter {

    private static final String FILE_EXTENSION_GSPEC = ".gspec";

    @Override
    public boolean accept(File dir, String name) {
      return name.endsWith(FILE_EXTENSION_GSPEC);
    }
  }

}
