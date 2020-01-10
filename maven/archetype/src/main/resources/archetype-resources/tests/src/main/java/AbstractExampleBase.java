package ${package};

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.testcase.AbstractBrowserBasedTest;

/**
 * Abstract base class for common functionality needed by multiple tests.
 */
public class AbstractExampleBase extends AbstractBrowserBasedTest {
    /**
     * @param testDevice test device to use for test
     */
    public AbstractExampleBase(TestDevice testDevice) {
        super(testDevice);
    }
}
