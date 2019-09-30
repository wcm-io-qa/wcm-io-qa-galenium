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
package io.wcm.qa.glnm.sampling.jsoup.base;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.sampling.Sampler;
import io.wcm.qa.glnm.sampling.transform.base.TransformationBasedSampler;

/**
 * Base class to help implementing extracting an {@link Element} from a {@link Document}.
 * @param <JDS> {@link Document} sampler
 * @param <D> Jsoup {@link Document}
 */
public abstract class JsoupElementBasedSampler<JDS extends Sampler<D>, D extends Document, O>
    extends TransformationBasedSampler<JDS, D, O> {

  /**
   * @param inputSampler {@link Document} sampler
   */
  public JsoupElementBasedSampler(JDS inputSampler) {
    super(inputSampler);
  }

  /**
   * Override to extract specific element.
   * @param inputSample document to extract element from
   * @return extracted element
   */
  protected Element extractElement(Document inputSample) {
    // just return document per default
    return inputSample;
  }

  protected abstract O extractValueFromElement(Element extractedElement);

  @Override
  protected O transform(D inputSample) {
    Element extractedElement = extractElement(inputSample);
    if (extractedElement != null) {
      return extractValueFromElement(extractedElement);
    }
    throw new GaleniumException("null Element passed to extraction.");
  }

}
