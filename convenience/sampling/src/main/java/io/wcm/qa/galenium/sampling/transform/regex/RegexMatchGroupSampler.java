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
package io.wcm.qa.galenium.sampling.transform.regex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.wcm.qa.galenium.sampling.Sampler;
import io.wcm.qa.galenium.sampling.transform.base.RegexGroupBasedSampler;

public class RegexMatchGroupSampler<S extends Sampler<String>> extends RegexGroupBasedSampler<S> {

  public RegexMatchGroupSampler(S inputSampler, String regex, int flags) {
    super(inputSampler, regex, flags);
  }

  public RegexMatchGroupSampler(S inputSampler, Pattern pattern) {
    super(inputSampler, pattern);
  }

  public RegexMatchGroupSampler(S inputSampler, String regex) {
    super(inputSampler, regex);
  }

  @Override
  protected Collection<String> transform(Matcher matcher) {
    Collection<String> results = new ArrayList<>();
    if (matcher.matches()) {
      addAllGroups(results, matcher);
    }
    return results;
  }


}
