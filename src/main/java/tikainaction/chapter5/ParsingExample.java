package tikainaction.chapter5;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.CompositeParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.apache.tika.parser.html.HtmlMapper;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.html.IdentityHtmlMapper;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParsingExample {

    public static String parseToStringExample() throws Exception {
//<start id="parse_to_string"/>
File document = new File("example.doc");
String content = new Tika().parseToString(document);
System.out.print(content);
//<end id="parse_to_string"/>
        return content;
    }

    public static void parseToReaderExample() throws Exception {
//<start id="parse_to_reader"/>
File document = new File("example.doc");
Reader reader = new Tika().parse(document);
try {
    char[] buffer = new char[1000];
    int n = reader.read(buffer);
    while (n != -1) {
        System.out.append(CharBuffer.wrap(buffer, 0, n));
        n = reader.read(buffer);
    }
} finally {
    reader.close();
}
//<end id="parse_to_reader"/>
    }


    public static void parseFileInputStream(String filename) throws Exception {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
//<start id="parse_file_input_stream"/>
InputStream stream = new FileInputStream(new File(filename));
try {
    parser.parse(stream, handler, metadata, context);
} finally {
    stream.close();
}
//<end id="parse_file_input_stream"/>
    }

    public static void parseURLStream(String address) throws Exception {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
//<start id="parse_url_stream"/>
InputStream stream =
    new GZIPInputStream(new URL(address).openStream());
try {
    parser.parse(stream, handler, metadata, context);
} finally {
    stream.close();
}
//<end id="parse_url_stream"/>
    }

    public static void parseTikaInputStream(String filename) throws Exception {
        Parser parser = new AutoDetectParser();
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
//<start id="parse_tika_input_stream"/>
InputStream stream = TikaInputStream.get(new File(filename));
try {
    parser.parse(stream, handler, metadata, context);
} finally {
    stream.close();
}
//<end id="parse_tika_input_stream"/>
    }

    public static File tikaInputStreamGetFile(String filename) throws Exception {
        InputStream stream = TikaInputStream.get(new File(filename));
        try {
//<start id="tika_input_stream_get_file"/>
TikaInputStream tikaInputStream = TikaInputStream.get(stream);
File file = tikaInputStream.getFile();
//<end id="tika_input_stream_get_file"/>
            return file;
        } finally {
            stream.close();
        }
    }

    public static void useHtmlParser() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
//<start id="html_parser"/>
Parser parser = new HtmlParser();
parser.parse(stream, handler, metadata, context);
//<end id="html_parser"/>
    }

    public static void useCompositeParser() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        ContentHandler handler = new DefaultHandler();
        ParseContext context = new ParseContext();
//<start id="composite_parser"/>
Map<MediaType, Parser> parsersByType = new HashMap<MediaType, Parser>();
parsersByType.put(MediaType.parse("text/html"), new HtmlParser());
parsersByType.put(MediaType.parse("application/xml"), new XMLParser());

CompositeParser parser = new CompositeParser();
parser.setParsers(parsersByType);
parser.setFallback(new TXTParser());

Metadata metadata = new Metadata();
metadata.set(Metadata.CONTENT_TYPE, "text/html");
parser.parse(stream, handler, metadata, context);
//<end id="composite_parser"/>
    }

    public static void useAutoDetectParser() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
//<start id="auto_detect_parser"/>
Parser parser = new AutoDetectParser();
parser.parse(stream, handler, metadata, context);
//<end id="auto_detect_parser"/>
    }

    public static void testTeeContentHandler(String filename) throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        Parser parser = new AutoDetectParser();
//<start id="tee_content_handler"/>
LinkContentHandler linkCollector = new LinkContentHandler();
OutputStream output = new FileOutputStream(new File(filename));
try {
    ContentHandler handler = new TeeContentHandler(
            new BodyContentHandler(output), linkCollector);
    parser.parse(stream, handler, metadata, context);
} finally {
    output.close();
}
//<end id="tee_content_handler"/>
    }

    public static void testLocale() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
//<start id="locale"/>
ParseContext context = new ParseContext();
context.set(Locale.class, Locale.ENGLISH);
parser.parse(stream, handler, metadata, context);
//<end id="locale"/>
    }

    public static void testHtmlMapper() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
//<start id="html_mapper"/>
ParseContext context = new ParseContext();
context.set(HtmlMapper.class, new IdentityHtmlMapper());
parser.parse(stream, handler, metadata, context);
//<end id="html_mapper"/>
    }

    public static void testCompositeDocument() throws Exception {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();
//<start id="composite_document"/>
ParseContext context = new ParseContext();
context.set(Parser.class, new ParserDecorator(parser) {
    @Override
    public void parse(
            InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context)
            throws IOException, SAXException, TikaException {
        // custom processing of the component document
    }
});
parser.parse(stream, handler, metadata, context);
//<end id="composite_document"/>
    }

}
