package tikainaction.chapter7;

//<start id="language_detecting_parser"/>
import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.ProfilingHandler;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.DelegatingParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.TeeContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class LanguageDetectingParser extends DelegatingParser {

    public void parse( //<co id="ldp_override"/>
            InputStream stream, ContentHandler handler,
            final Metadata metadata, ParseContext context)
            throws SAXException, IOException, TikaException {
        ProfilingHandler profiler = //<co id="ldp_profiler"/>
            new ProfilingHandler();
        ContentHandler tee =
            new TeeContentHandler(handler, profiler);

        super.parse(stream, tee, metadata, context); //<co id="ldp_super"/>

        LanguageIdentifier identifier = //<co id="ldp_language"/>
            profiler.getLanguage();
        if (identifier.isReasonablyCertain()) { //<co id="ldp_certain"/>
            metadata.set(
                    Metadata.LANGUAGE, //<co id="ldp_metadata"/>
                    identifier.getLanguage());
        }
    }

    
}
//<end id="language_detecting_parser"/>
