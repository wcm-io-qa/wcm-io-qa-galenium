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
package io.wcm.qa.galenium.verification.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import io.wcm.qa.galenium.introspection.PageSourceSampler;
import io.wcm.qa.galenium.verification.base.CombiningStringBasedVerification;

public class PageSourceVerification extends CombiningStringBasedVerification {

  private Collection<Pattern> mustNotPatterns = new ArrayList<Pattern>();
  private Collection<String> mustNotStrings = new ArrayList<String>();
  private Collection<Pattern> mustPatterns = new ArrayList<Pattern>();
  private Collection<String> mustStrings = new ArrayList<String>();

  protected PageSourceVerification(String verificationName) {
    super(verificationName, new PageSourceSampler());
  }

  public void mustContain(Pattern pattern) {
    mustPatterns.add(pattern);
  }

  public void mustContain(String string) {
    mustStrings.add(string);
  }

  public void mustContainPattern(String regex) {
    mustContain(Pattern.compile(regex));
  }

  public void mustNotContain(Pattern pattern) {
    mustNotPatterns.add(pattern);
  }

  public void mustNotContain(String string) {
    mustNotStrings.add(string);
  }

  public void mustNotContainPattern(String regex) {
    mustNotContain(Pattern.compile(regex));
  }

  private void populateMustNotPatterns(String string) {
    for (Pattern pattern : mustNotPatterns) {
      addCheck(new DoesNotContainPatternVerification(getVerificationName(), pattern, string));
    }
  }

  private void populateMustNotStrings(String string) {
    for (String pattern : mustNotStrings) {
      addCheck(new DoesNotContainStringVerification(getVerificationName(), pattern, string));
    }
  }

  private void populateMustPatterns(String string) {
    for (Pattern pattern : mustPatterns) {
      addCheck(new ContainsPatternVerification(getVerificationName(), pattern, string));
    }
  }

  private void populateMustStrings(String string) {
    for (String pattern : mustStrings) {
      addCheck(new ContainsStringVerification(getVerificationName(), pattern, string));
    }
  }

  @Override
  protected Boolean doVerification() {
    populateChecks(getActualValue());
    return super.doVerification();
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
