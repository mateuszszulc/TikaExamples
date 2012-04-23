package tikainaction.chapter2;

//<start id="simple_text_extractor"/>
import java.io.File;
import org.apache.tika.Tika;

public class SimpleTextExtractor {

    public static void main(String[] args) throws Exception {
        // Create a Tika instance with the default configuration
        Tika tika = new Tika();

        // Parse all given files and print out the extracted
        // text content
        for (String file : args) {
            String text = tika.parseToString(new File(file));
            System.out.print(text);
        }
    }

}
//<end id="simple_text_extractor"/>
