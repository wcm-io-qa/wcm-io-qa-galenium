package ${package};

import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import io.wcm.qa.galenium.providers.TestDeviceProvider;
import io.wcm.qa.galenium.device.TestDevice;

/**
 * Sample Test for Galenium.
 */
public class SampleGaleniumIT extends AbstractExampleBase
{

    @Factory(dataProviderClass = TestDeviceProvider.class, dataProvider = TestDeviceProvider.GALENIUM_TEST_DEVICES_ALL)
    public SampleGaleniumIT(TestDevice testDevice){
        super(testDevice);
    }

    @Test
    public void testIt() {
        // TODO: implement
    }
}
