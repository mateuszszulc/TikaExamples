package tikainaction.chapter7;

import java.io.IOException;

import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.LanguageProfile;
import org.apache.tika.language.ProfilingHandler;
import org.apache.tika.language.ProfilingWriter;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;

public class Language {

    public static void languageDetection() throws IOException {
//<start id="language_detection"/> 
LanguageProfile profile = new LanguageProfile( //<co id="udhr-sv"/>
        "Alla människor är födda fria och"
        + " lika i värde och rättigheter.");

LanguageIdentifier identifier = //<co id="lang-sv"/>
    new LanguageIdentifier(profile);
System.out.println(identifier.getLanguage());
//<end id="language_detection"/>
    }

    public static void languageDetectionWithWriter() throws IOException {
//<start id="language_detection_with_writer"/> 
ProfilingWriter writer = new ProfilingWriter();
writer.append("Minden emberi lény"); //<co id="udhr-hu"/>
writer.append(" szabadon születik és");
writer.append(" egyenlő méltósága és");
writer.append(" joga van.");

LanguageIdentifier identifier = //<co id="lang-hu"/>
    writer.getLanguage();
System.out.println(identifier.getLanguage());
//<end id="language_detection_with_writer"/>
    }


    public static void languageDetectionWithHandler() throws Exception {
//<start id="language_detection_with_handler"/> 
ProfilingHandler handler = new ProfilingHandler();
new AutoDetectParser().parse(
        System.in, handler,
        new Metadata(), new ParseContext());

LanguageIdentifier identifier =  handler.getLanguage();
System.out.println(identifier.getLanguage());
//<end id="language_detection_with_handler"/>
    }
}
