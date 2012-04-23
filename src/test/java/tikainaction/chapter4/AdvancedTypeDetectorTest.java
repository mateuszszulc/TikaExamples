package tikainaction.chapter4;

import junit.framework.Assert;

import org.junit.Test;

public class AdvancedTypeDetectorTest {

    @Test
    public void testDetectWithCustomConfig() throws Exception {
        Assert.assertEquals(
                "application/xml",
                AdvancedTypeDetector.detectWithCustomConfig("pom.xml"));
    }

}
