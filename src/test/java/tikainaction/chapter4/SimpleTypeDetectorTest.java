package tikainaction.chapter4;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.Test;

public class SimpleTypeDetectorTest {

    @Test
    public void testSimpleTypeDetector() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        PrintStream out = System.out;
        System.setOut(new PrintStream(buffer));

        SimpleTypeDetector.main(new String[] { "pom.xml" });

        System.setOut(out);

        Assert.assertEquals(
                "pom.xml: application/xml",
                new String(buffer.toByteArray()).trim());
    }

}
