package tikainaction.chapter11;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Collections;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

//<start id="encrypted_prescription_parser"/> 
public class EncryptedPrescriptionParser
        extends AbstractParser { //<co id="abstract_parser"/>

    public void parse( //<co id="implement_parse"/>
            InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context)
            throws IOException, SAXException, TikaException {
        try {
            Key key = Pharmacy.getKey(); //<co id="xpd_parse_decrypt"/>
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            InputStream decrypted =
                new CipherInputStream(stream, cipher);

            new PrescriptionParser().parse( //<co id="delegate_to_xpd"/>
                    decrypted, handler, metadata, context);
        } catch (GeneralSecurityException e) {
            throw new TikaException(
                    "Unable to decrypt a digital prescription", e);
        }
    }

    public Set<MediaType> getSupportedTypes( //<co id="xpd_encrypted_types"/>
            ParseContext context) {
        return Collections.singleton(
                MediaType.application("x-prescription"));
    }

}
//<end id="encrypted_prescription_parser"/> 