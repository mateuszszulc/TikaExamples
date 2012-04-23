package tikainaction.chapter11;

import java.util.Collections;
import java.util.Set;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.ElementMetadataHandler;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.sax.TeeContentHandler;
import org.xml.sax.ContentHandler;

//<start id="prescription_parser"/> 
public class PrescriptionParser extends XMLParser {

    @Override //<co id="xpd_override"/>
    protected ContentHandler getContentHandler(
            ContentHandler handler, Metadata metadata, 
            ParseContext context) {
        String xpd = "http://example.com/2011/xpd";

        ContentHandler doctor = new ElementMetadataHandler( //<co id="xpd_emh"/>
                xpd, "doctor", metadata, "xpd:doctor");
        ContentHandler patient = new ElementMetadataHandler(
                xpd, "patient", metadata, "xpd:patient");

        return new TeeContentHandler( //<co id="xpd_tee_handler"/>
                super.getContentHandler(handler, metadata, context),
                doctor, patient);
    }

    @Override //<co id="xpd_supported_types"/>
    public Set<MediaType> getSupportedTypes(
            ParseContext context) {
        return Collections.singleton(
                MediaType.application("x-prescription+xml"));
    }

}
//<end id="prescription_parser"/> 