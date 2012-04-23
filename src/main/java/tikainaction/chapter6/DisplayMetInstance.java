package tikainaction.chapter6;

import java.io.IOException;
import java.net.URL;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

/**
 * Grabs a PDF file from a URL and prints its {@link Metadata}
 */
// <start id="met_getter"/>
public class DisplayMetInstance {

  public static Metadata getMet(URL url) throws IOException,
      SAXException, TikaException {
    Metadata met = new Metadata();
    PDFParser parser = new PDFParser();
    parser.parse(url.openStream(), new BodyContentHandler(), met,
        new ParseContext()); //<co id="pop_met"/>
    return met;
  }

  public static void main(String[] args) throws Exception {
    Metadata met = DisplayMetInstance.getMet(new URL(args[0]));
    System.out.println(met); //<co id="print_met"/>
  }

}
//<end id="met_getter"/>
