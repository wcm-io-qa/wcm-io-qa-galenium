package io.wcm.qa.galenium.listeners;

import java.util.Collection;

import org.testng.ITestContext;
import org.testng.TestListenerAdapter;

import io.wcm.qa.galenium.persistence.util.SampleManager;

/**
 * Handles persisting text samples at the end of test run.
 */
public class SamplePersistenceListener extends TestListenerAdapter {

  @Override
  public void onFinish(ITestContext testContext) {
    Collection<SampleManager> managers = SampleManager.getAllInstances().values();

    System.out.println("generating samples gathered from " + managers.size() + " managers");
    for (SampleManager sampleManager : managers) {
      sampleManager.persistAllExpected();
    }
  }

}

