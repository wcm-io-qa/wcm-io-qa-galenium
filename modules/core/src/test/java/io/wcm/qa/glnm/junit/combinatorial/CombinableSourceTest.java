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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.junit.sources.CsvSourceXY;

class CombinableSourceTest {

  private static final Logger LOG = LoggerFactory.getLogger(CombinableSourceTest.class);

  @CartesianProduct
  @CsvSourceXY
  @CombinableSource(CombinableSourceDummy.class)
  void testExtensionInjection(String string) {
    assertThat(string, is(notNullValue()));
  }

  static class CombinableSourceDummy implements CombinableProvider {

    @Override
    public List<Combinable> combinables() {
      return Lists.newArrayList(
          new Combinable<Extension>(new CombinableExtensionDummy(1)),
          new Combinable<Extension>(new CombinableExtensionDummy(2)),
          new Combinable<Extension>(new CombinableExtensionDummy(3))
          );
    }

    @Override
    public Class providedType() {
      return Extension.class;
    }

    private static final class CombinableExtensionDummy
        implements BeforeEachCallback, AfterEachCallback {

      private int value;

      CombinableExtensionDummy(int i) {
        value = i;
      }

      @Override
      public void afterEach(ExtensionContext context) throws Exception {
        Store store = getStore(context);
        String testId = store.get("test_id", String.class);
        LOG.debug(testId);
        if (StringUtils.isBlank(testId)) {
          throw new GaleniumException("no test id found.");
        }
      }

      @Override
      public void beforeEach(ExtensionContext context) throws Exception {
        Store store = getStore(context);
        store.put("test_id", value + " -> " + context.getUniqueId());
      }

      private Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(this));
      }
    }

  }
}
