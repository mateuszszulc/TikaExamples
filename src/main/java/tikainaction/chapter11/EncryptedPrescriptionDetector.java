package tikainaction.chapter11;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.xml.namespace.QName;

import org.apache.tika.detect.Detector;
import org.apache.tika.detect.XmlRootExtractor;
import org.apache.tika.io.LookaheadInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

//<start id="encrypted_prescription_detector"/>
public class EncryptedPrescriptionDetector implements Detector {

    public MediaType detect(InputStream stream, Metadata metadata)
            throws IOException {
        Key key = Pharmacy.getKey(); //<co id="pharmacy_key"/>
        MediaType type = MediaType.OCTET_STREAM;

        InputStream lookahead = //<co id="lookahead_stream"/>
            new LookaheadInputStream(stream, 1024);
        try {
            Cipher cipher = Cipher.getInstance("RSA"); //<co id="decrypt_stream"/>
            cipher.init(Cipher.DECRYPT_MODE, key);
            InputStream decrypted =
                new CipherInputStream(lookahead, cipher);

            QName name = new XmlRootExtractor() //<co id="xml_root_extractor"/>
                .extractRootElement(decrypted);
            if (name != null
                    && "http://example.com/xpd".
                                       equals(name.getNamespaceURI())
                    && "prescription".equals(name.getLocalPart())) {
                type = //<co id="x_prescription_type"/>
                    MediaType.application("x-prescription");
            }
        } catch (GeneralSecurityException e) {
            // unable to decrypt, fall through
        } finally {
            lookahead.close();
        }
        return type;
    }

}
//<end id="encrypted_prescription_detector"/>