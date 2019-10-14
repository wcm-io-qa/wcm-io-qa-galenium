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
package io.wcm.qa.galenium.verification.driver;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import difflib.Chunk;
import difflib.Delta;
import difflib.Delta.TYPE;
import difflib.DiffUtils;
import difflib.Patch;
import io.wcm.qa.galenium.sampling.driver.PageSourceSampler;
import io.wcm.qa.galenium.verification.string.base.StringSamplerBasedVerification;

/**
 * Does a straightforward comparison of page source.
 *
 * @since 2.0.0
 */
public class SamplingPageSourceVerification extends StringSamplerBasedVerification {

  private static final String COLOR_ADDED = "green";
  private static final String COLOR_REMOVED = "red";
  private Patch<String> diffResult;

  /**
   * <p>Constructor for SamplingPageSourceVerification.</p>
   *
   * @param verificationName name to use for reporting and logging
   */
  public SamplingPageSourceVerification(String verificationName) {
    super(verificationName, new PageSourceSampler());
  }

  @Override
  protected boolean doVerification() {
    List<String> original = Arrays.asList(StringUtils.split(getExpectedValue(), '\n'));
    List<String> revised = Arrays.asList(StringUtils.split(getActualValue(), '\n'));
    diffResult = DiffUtils.diff(original, revised);
    return diffResult.getDeltas().isEmpty();
  }

  @Override
  protected String getFailureMessage() {
    List<Delta<String>> deltas = diffResult.getDeltas();
    StringBuffer msg = new StringBuffer()
        .append("(")
        .append(getVerificationName())
        .append(") found ")
        .append(deltas.size())
        .append(" differences: <br/>");
    for (Delta<String> delta : deltas) {
      TYPE type = delta.getType();
      if (type == TYPE.DELETE || type == TYPE.CHANGE) {
        appendChunk(msg, COLOR_REMOVED, delta.getOriginal());
      }
      if (type == TYPE.INSERT || type == TYPE.CHANGE) {
        appendChunk(msg, COLOR_ADDED, delta.getRevised());
      }
    }
    return msg.toString();
  }

  private void appendChunk(StringBuffer msg, String prefix, Chunk<String> chunk) {
    List<String> lines = chunk.getLines();
    for (String line : lines) {
      msg
          .append("<p style=\"color:")
          .append(prefix)
          .append("\">")
          .append(getValueForLogging(line))
          .append("</p>");
    }
  }

  @Override
  protected String getSuccessMessage() {
    StringBuffer msg = new StringBuffer()
        .append("(")
        .append(getVerificationName())
        .append(") found no differences.");
    return msg.toString();
  }

}
