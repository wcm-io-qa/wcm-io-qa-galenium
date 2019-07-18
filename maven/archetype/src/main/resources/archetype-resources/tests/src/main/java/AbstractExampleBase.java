package ${package};

import io.wcm.qa.glnm.device.TestDevice;
import io.wcm.qa.glnm.testcase.AbstractGaleniumBase;

/**
 * Abstract base class for common functionality needed by multiple tests.
 */
public class AbstractExampleBase extends AbstractGaleniumBase {
    /**
     * @param testDevice test device to use for test
     */
    public AbstractExampleBase(TestDevice testDevice) {
        super(testDevice);
    }
}
