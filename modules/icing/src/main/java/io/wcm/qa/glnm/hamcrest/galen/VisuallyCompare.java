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
package io.wcm.qa.glnm.hamcrest.galen;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.wcm.qa.glnm.differences.base.Difference;
import io.wcm.qa.glnm.differences.generic.MutableDifferences;
import io.wcm.qa.glnm.exceptions.GaleniumException;
import io.wcm.qa.glnm.galen.specs.GalenSpecRun;
import io.wcm.qa.glnm.galen.specs.imagecomparison.ImageComparisonSpecDefinition;
import io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect;
import io.wcm.qa.glnm.galen.validation.GalenValidation;
import io.wcm.qa.glnm.hamcrest.baseline.DifferentiatingMatcher;
import io.wcm.qa.glnm.hamcrest.selector.SelectorMatcher;
import io.wcm.qa.glnm.selectors.base.Selector;

/**
 * Matcher doing visual comparison on element defined by Selector.
 *
 * @since 5.0.0
 */
public class VisuallyCompare
    extends SelectorMatcher<GalenSpecRun>
    implements DifferentiatingMatcher<Selector> {

  private final ImageComparisonSpecDefinition specDefinition = new ImageComparisonSpecDefinition();

  private final LoadingCache<Selector, GalenSpecRun> visualComparisons = CacheBuilder.newBuilder().build(
      new CacheLoader<Selector, GalenSpecRun>() {

        @Override
        public GalenSpecRun load(Selector key) throws Exception {
          ImageComparisonSpecDefinition def = new ImageComparisonSpecDefinition(specDefinition);
          def.setSelector(key);
          return GalenValidation.imageComparison(def);
        }
      });

  protected VisuallyCompare() {
    super(GalenSpecRunMatcher.successfulRun());
  }

  /** {@inheritDoc} */
  @Override
  public void add(Difference difference) {
    specDefinition.addDifference(difference);
  }

  /** {@inheritDoc} */
  @Override
  public String getKey() {
    return getDifferences().getKey();
  }

  /** {@inheritDoc} */
  @Override
  public Iterator<Difference> iterator() {
    return getDifferences().iterator();
  }

  /** {@inheritDoc} */
  @Override
  public void prepend(Difference difference) {
    MutableDifferences newDifferences = new MutableDifferences();
    newDifferences.add(difference);
    newDifferences.addAll(getDifferences());
    specDefinition.setDifferences(newDifferences);
  }

  /**
   * <p>whileIgnoring.</p>
   *
   * @param ignore a {@link io.wcm.qa.glnm.selectors.base.Selector} object.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare whileIgnoring(Selector... ignore) {
    if (ignore == null) {
      return this;
    }
    for (Selector selectorToIgnore : ignore) {
      specDefinition.addObjectToIgnore(selectorToIgnore);
    }
    return this;
  }

  /**
   * <p>withAllowedErrorPercent.</p>
   *
   * @param error a double.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withAllowedErrorPercent(double error) {
    specDefinition.setAllowedErrorPercent(error);
    return this;
  }

  /**
   * <p>withAllowedErrorPixel.</p>
   *
   * @param error a int.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withAllowedErrorPixel(int error) {
    specDefinition.setAllowedErrorPixel(error);
    return this;
  }

  /**
   * <p>withAllowedOffset.</p>
   *
   * @param offset a int.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withAllowedOffset(int offset) {
    specDefinition.setAllowedOffset(offset);
    return this;
  }

  /**
   * <p>withCorrections.</p>
   *
   * @param corrections a {@link io.wcm.qa.glnm.galen.specs.page.GalenCorrectionRect} object.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withCorrections(GalenCorrectionRect corrections) {
    specDefinition.setCorrections(corrections);
    return this;
  }

  /**
   * <p>withCropIfOutside.</p>
   *
   * @param crop a boolean.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withCropIfOutside(boolean crop) {
    specDefinition.setCropIfOutside(crop);
    return this;
  }

  /**
   * <p>withDifferences.</p>
   *
   * @param differences a {@link java.util.Collection} object.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withDifferences(Collection<Difference> differences) {
    specDefinition.addAll(differences);
    return this;
  }

  /**
   * <p>withElementName.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withElementName(String name) {
    specDefinition.setElementName(name);
    return this;
  }

  /**
   * <p>withSectionName.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withSectionName(String name) {
    specDefinition.setSectionName(name);
    return this;
  }

  /**
   * <p>withZeroToleranceWarning.</p>
   *
   * @param warning a boolean.
   * @return a {@link io.wcm.qa.glnm.hamcrest.galen.VisuallyCompare} object.
   */
  public VisuallyCompare withZeroToleranceWarning(boolean warning) {
    specDefinition.setZeroToleranceWarning(warning);
    return this;
  }

  protected MutableDifferences getDifferences() {
    return specDefinition.getDifferences();
  }

  @Override
  protected GalenSpecRun map(Selector item) {
    try {
      return visualComparisons.get(item);
    }
    catch (ExecutionException ex) {
      throw new GaleniumException("when executing visual comparison", ex);
    }
  }

  /**
   * <p>
   * visuallyCompare.
   * </p>
   *
   * @return visual comparison matcher
   * @since 5.0.0
   */
  public static VisuallyCompare visuallyCompare() {
    return new VisuallyCompare();
  }
}
