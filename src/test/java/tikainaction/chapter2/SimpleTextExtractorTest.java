package tikainaction.chapter2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class SimpleTextExtractorTest {

    @Test
    public void testSimpleTextExtractor() throws Exception {
        String message =
            "Hello, World! This is simple UTF-8 text content written"
            + " in English to test autodetection of the character"
            + " encoding of the input stream.";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        PrintStream out = System.out;
        System.setOut(new PrintStream(buffer));

        File file = new File("target", "test.txt");
        FileUtils.writeStringToFile(file, message);
        SimpleTextExtractor.main(new String[] { file.getPath() });
        file.delete();

        System.setOut(out);

        Assert.assertEquals(message, new String(buffer.toByteArray()).trim());
    }

}
