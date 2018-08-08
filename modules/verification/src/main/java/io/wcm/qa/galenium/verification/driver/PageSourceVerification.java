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
package io.wcm.qa.galenium.verification.driver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import io.wcm.qa.galenium.sampling.driver.PageSourceSampler;
import io.wcm.qa.galenium.verification.base.CombiningStringBasedVerification;
import io.wcm.qa.galenium.verification.string.ContainsPatternVerification;
import io.wcm.qa.galenium.verification.string.ContainsStringVerification;
import io.wcm.qa.galenium.verification.string.DoesNotContainPatternVerification;
import io.wcm.qa.galenium.verification.string.DoesNotContainStringVerification;

public class PageSourceVerification extends CombiningStringBasedVerification {

  private Map<Pattern, String> mustNotPatterns = new HashMap<Pattern, String>();
  private Map<String, String> mustNotStrings = new HashMap<String, String>();
  private Map<Pattern, String> mustPatterns = new HashMap<Pattern, String>();
  private Map<String, String> mustStrings = new HashMap<String, String>();

  protected PageSourceVerification(String verificationName) {
    super(verificationName, new PageSourceSampler());
  }

  public void mustContain(Pattern pattern, String message) {
    mustPatterns.put(pattern, message);
  }

  public void mustContain(String string, String message) {
    mustStrings.put(string, message);
  }

  public void mustContainPattern(String regex, String message) {
    mustContain(Pattern.compile(regex), message);
  }

  public void mustNotContain(Pattern pattern, String message) {
    mustNotPatterns.put(pattern, message);
  }

  public void mustNotContain(String string, String message) {
    mustNotStrings.put(string, message);
  }

  public void mustNotContainPattern(String regex, String message) {
    mustNotContain(Pattern.compile(regex), message);
  }

  private void populateMustNotPatterns(String string) {
    for (Entry<Pattern, String> pattern : mustNotPatterns.entrySet()) {
      addCheck(new DoesNotContainPatternVerification(pattern.getValue(), pattern.getKey(), string));
    }
  }

  private void populateMustNotStrings(String string) {
    for (Entry<String, String> pattern : mustNotStrings.entrySet()) {
      addCheck(new DoesNotContainStringVerification(pattern.getValue(), pattern.getKey(), string));
    }
  }

  private void populateMustPatterns(String string) {
    for (Entry<Pattern, String> pattern : mustPatterns.entrySet()) {
      addCheck(new ContainsPatternVerification(pattern.getValue(), pattern.getKey(), string));
    }
  }

  private void populateMustStrings(String string) {
    for (Entry<String, String> pattern : mustStrings.entrySet()) {
      addCheck(new ContainsStringVerification(pattern.getValue(), pattern.getKey(), string));
    }
  }

  @Override
  protected String getSuccessMessageForEmptyCheckMessages() {
    return "(" + getVerificationName() + ") Page source is clean.";
  }

  @Override
  protected void populateChecks(String string) {
    populateMustStrings(string);
    populateMustNotStrings(string);
    populateMustPatterns(string);
    populateMustNotPatterns(string);
  }

}
