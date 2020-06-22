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
package io.wcm.qa.glnm.hamcrest;

import java.util.concurrent.ExecutionException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.wcm.qa.glnm.exceptions.GaleniumException;


/**
 * <p>Abstract TypeSafeWrappingMatcher class.</p>
 *
 * @since 5.0.0
 */
public abstract class TypeSafeWrappingMatcher<T, M> extends TypeSafeMatcher<T> {

  private Matcher<M> internalMatcher;
  private CacheLoader<T, M> loader = new CacheLoader<T, M>() {
    @Override
    public M load(T key) throws Exception {
      return map(key);
    };
  };
  private LoadingCache<T, M> mappedItems = CacheBuilder.newBuilder().build(loader);

  protected TypeSafeWrappingMatcher(Matcher<M> matcher) {
    setInternalMatcher(matcher);
  }

  /** {@inheritDoc} */
  @Override
  public void describeTo(Description description) {
    getInternalMatcher().describeTo(description);
  }

  private M mapped(T item) {
    try {
      return mappedItems.get(item);
    }
    catch (ExecutionException ex) {
      throw new GaleniumException("when getting mapped value", ex);
    }
  }

  @Override
  protected void describeMismatchSafely(T item, Description mismatchDescription) {
    getInternalMatcher().describeMismatch(mapped(item), mismatchDescription);
  }

  protected Matcher<M> getInternalMatcher() {
    return internalMatcher;
  }

  protected abstract M map(T item);

  @Override
  protected boolean matchesSafely(T item) {
    mappedItems.refresh(item);
    return getInternalMatcher().matches(mapped(item));
  }

  protected void setInternalMatcher(Matcher<M> internalMatcher) {
    this.internalMatcher = internalMatcher;
  }

}
