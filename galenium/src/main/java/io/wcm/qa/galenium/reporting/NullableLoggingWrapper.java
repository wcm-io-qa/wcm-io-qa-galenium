/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
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
package io.wcm.qa.galenium.reporting;

/**
 * Wraps a {@link GaleniumLogging} object that is potentially null.
 */
public class NullableLoggingWrapper implements GaleniumLogging {

  private GaleniumLogging delegate;

  /**
   * @param logging delegate object to log to, maybe null
   */
  public NullableLoggingWrapper(GaleniumLogging logging) {
    delegate = logging;
  }

  @Override
  public void assignCategory(String category) {
    if (delegate != null) {
      delegate.assignCategory(category);
    }
  }

  @Override
  public void debugAndIgnoreException(Throwable ex) {
    if (delegate != null) {
      delegate.debugAndIgnoreException(ex);
    }
  }

  @Override
  public void debugException(Throwable ex) {
    if (delegate != null) {
      delegate.debugException(ex);
    }
  }

  @Override
  public void debugFailed(String msg) {
    if (delegate != null) {
      delegate.debugFailed(msg);
    }
  }

  @Override
  public void debugInfo(String msg) {
    if (delegate != null) {
      delegate.debugInfo(msg);
    }
  }

  @Override
  public void debugPassed(String msg) {
    if (delegate != null) {
      delegate.debugPassed(msg);
    }
  }

  @Override
  public void debugSkip(String msg) {
    if (delegate != null) {
      delegate.debugSkip(msg);
    }
  }

  @Override
  public void debugUnknown(String msg) {
    if (delegate != null) {
      delegate.debugUnknown(msg);
    }
  }

  @Override
  public void debugWarning(String msg) {
    if (delegate != null) {
      delegate.debugWarning(msg);
    }
  }

  @Override
  public void reportException(Throwable ex) {
    if (delegate != null) {
      delegate.reportException(ex);
    }
  }

  @Override
  public void reportFailed(String msg) {
    if (delegate != null) {
      delegate.reportFailed(msg);
    }
  }

  @Override
  public void reportFatal(String msg) {
    if (delegate != null) {
      delegate.reportFatal(msg);
    }
  }

  @Override
  public void reportFatalException(Throwable ex) {
    if (delegate != null) {
      delegate.reportFatalException(ex);
    }
  }

  @Override
  public void reportInfo(String msg) {
    if (delegate != null) {
      delegate.reportInfo(msg);
    }
  }

  @Override
  public void reportPassed(String msg) {
    if (delegate != null) {
      delegate.reportPassed(msg);
    }
  }

  @Override
  public void reportSkip(String msg) {
    if (delegate != null) {
      delegate.reportSkip(msg);
    }
  }

  @Override
  public void reportWarning(String msg) {
    if (delegate != null) {
      delegate.reportWarning(msg);
    }
  }

  @Override
  public boolean isDebugging() {
    if (delegate != null) {
      return delegate.isDebugging();
    }
    return false;
  }
}
