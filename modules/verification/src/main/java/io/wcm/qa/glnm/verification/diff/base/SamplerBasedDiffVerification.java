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
package io.wcm.qa.glnm.verification.diff.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import com.github.difflib.DiffUtils;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.ChangeDelta;
import com.github.difflib.patch.Chunk;
import com.github.difflib.patch.DeleteDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.InsertDelta;
import com.github.difflib.patch.Patch;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRow.Tag;
import com.github.difflib.text.DiffRowGenerator;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.verification.base.SamplerBasedVerification;

/**
 * @param <S> type of sampler
 * @param <I> type of input sample
 * @param <O> type of input sample collection
 */
public abstract class SamplerBasedDiffVerification<S extends Sampler<O>, I, O extends List<I>> extends SamplerBasedVerification<S, O> {

  private Patch<I> diffResult;
  private int maxLinesInFailureMessage = 20;

  protected SamplerBasedDiffVerification(String verificationName, S sampler) {
    super(verificationName, sampler);
  }

  /**
   * Writes formatted diff to {@link StringBuilder}.
   * @param builder to write to
   * @param maxLines to limit number of diff lines written
   */
  public void appendDiffTable(StringBuilder builder, int maxLines) {
    try {
      DiffRowGenerator generator = DiffRowGenerator.create().build();
      List<String> original = asStringList(getExpectedValue());
      Patch<String> patch = asStringPatch(getDiffResult());
      List<DiffRow> diffRows = generator.generateDiffRows(original, patch);
      builder.append("<table><thead><tr><th/><th/><th>Removed</th><th>Added</th></tr></thead><tbody>");
      int outputLineCounter = 0;
      for (DiffRow diffRow : diffRows) {
        if (outputLineCounter >= maxLines) {
          break;
        }
        Tag tag = diffRow.getTag();
        if (tag != Tag.EQUAL) {
          builder.append("<tr><td/><td/>");

          builder.append("<td style=\"color:red !important;\">");
          if (tag == Tag.CHANGE || tag == Tag.DELETE) {
            builder.append(diffRow.getOldLine());
          }
          builder.append("</td>").append("<td style=\"color:green !important;\">");
          if (tag == Tag.CHANGE || tag == Tag.INSERT) {
            builder.append(diffRow.getNewLine());
          }
          builder.append("</td>");
          builder.append("</tr>");
          outputLineCounter++;
        }
      }
      builder.append("</tbody></table>");
    }
    catch (DiffException ex) {
      throw new GaleniumException("Problems creating DiffRows.", ex);
    }
  }

  public Patch<I> getDiffResult() {
    return diffResult;
  }

  public int getMaxLinesInFailureMessage() {
    return maxLinesInFailureMessage;
  }

  public void setMaxLinesInFailureMessage(int maxLinesInFailureMessage) {
    this.maxLinesInFailureMessage = maxLinesInFailureMessage;
  }

  private Chunk<String> asStringChunk(Chunk<I> inputChunk) {
    return new Chunk<String>(inputChunk.getPosition(), asStringList(inputChunk.getLines()));
  }

  private AbstractDelta<String> asStringDelta(AbstractDelta<I> delta) {
    Chunk<String> source = asStringChunk(delta.getSource());
    Chunk<String> target = asStringChunk(delta.getTarget());
    DeltaType type = delta.getType();
    if (type == DeltaType.DELETE) {
      return new DeleteDelta<String>(source, target);
    }
    else if (type == DeltaType.INSERT) {
      return new InsertDelta<String>(source, target);
    }
    else if (type == DeltaType.CHANGE) {
      return new ChangeDelta<String>(source, target);
    }
    else {
      throw new GaleniumException("Delta stringification failure.");
    }
  }

  private int getDiffCount() {
    if (getDiffResult() == null) {
      return -1;
    }
    List<AbstractDelta<I>> deltas = getDiffResult().getDeltas();
    if (deltas == null) {
      return -2;
    }
    return deltas.size();
  }

  private String valueToString(I singleValue) {
    if (singleValue == null) {
      return "null";
    }
    return singleValue.toString();
  }

  @Override
  protected void afterVerification() {
    if (getLogger().isTraceEnabled()) {
      getLogger().trace("looking for '" + getValueForLogging(getExpectedValue()) + "'");
      getLogger().trace("(" + getVerificationName() + ") found " + getDiffCount() + " differences.");
      getLogger().trace("verified: " + isVerified());
    }
    if (!isVerified()) {
      persistSample(getExpectedKey(), getCachedValue());
    }
    if (getLogger().isTraceEnabled()) {
      getLogger().trace("done verifying (" + toString() + ")");
    }
  }

  protected List<String> asStringList(List<I> values) {
    List<String> stringList = new ArrayList<String>();
    for (I singleValue : values) {
      stringList.add(valueToString(singleValue));
    }
    return stringList;
  }

  protected Patch<String> asStringPatch(Patch<I> patch) {
    Patch<String> stringPatch = new Patch<String>();
    for (AbstractDelta<I> delta : patch.getDeltas()) {
      stringPatch.addDelta(asStringDelta(delta));
    }
    return stringPatch;
  }

  protected void diff(O expectedValue, O actualValue) {
    Patch<I> diff;
    try {
      diff = DiffUtils.diff(expectedValue, actualValue, getDiffEqualizer());
      setDiffResult(diff);
    }
    catch (DiffException ex) {
      throw new GaleniumException("Malfunctioning diff.", ex);
    }
  }

  /**
   * @return equalizer used when diffing
   */
  protected BiPredicate<I, I> getDiffEqualizer() {
    return null;
  }

  @Override
  protected boolean doVerification() {
    O expectedValue = getExpectedValue();
    O actualValue = getActualValue();
    diff(expectedValue, actualValue);
    return expectedValue.size() == actualValue.size() && getDiffCount() == 0;
  }

  @Override
  protected String getFailureMessage() {
    StringBuilder msg = new StringBuilder()
        .append("(")
        .append(getVerificationName())
        .append(") found ")
        .append(getDiffCount())
        .append(" differences in ")
        .append(getCachedValue().size())
        .append(" rows:");
    if (getDiffCount() > 0) {
      appendDiffTable(msg, getMaxLinesInFailureMessage());
    }
    return msg.toString();
  }

  @Override
  protected String getSuccessMessage() {
    StringBuilder stringBuilder = new StringBuilder()
        .append("(")
        .append(getVerificationName())
        .append(") found no differences in ")
        .append(getCachedValue().size())
        .append(" rows.");
    return stringBuilder.toString();
  }

  protected void setDiffResult(Patch<I> diffResult) {
    this.diffResult = diffResult;
  }

}
