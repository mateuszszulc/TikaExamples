package tikainaction.chapter11;

import java.net.URL;

import org.apache.tika.Tika;
import org.apache.tika.detect.CompositeDetector;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.mime.MimeTypesFactory;

public class CustomMimeInfo {

    public static String customMimeInfo() throws Exception {
//<start id="custom_mime_info"/>
String path = "file:///path/to/prescription-type.xml";
MimeTypes typeDatabase = MimeTypesFactory.create(new URL(path));
Tika tika = new Tika(typeDatabase);
String type = tika.detect("/path/to/prescription.xpd");
//<end id="custom_mime_info"/>
        return type;
    }

    public static String customCompositeDetector()
             throws Exception {
//<start id="custom_composite_detector"/>
String path = "file:///path/to/prescription-type.xml";
MimeTypes typeDatabase = MimeTypesFactory.create(new URL(path));
Tika tika = new Tika(new CompositeDetector(
        typeDatabase,
        new EncryptedPrescriptionDetector()));
String type = tika.detect("/path/to/tmp/prescription.xpd");
//<end id="custom_composite_detector"/>
        return type;
    }

    public static void main(String[] args) throws Exception {
    	System.out.println("customMimeInfo=" + customMimeInfo());
    	System.out.println("customCompositeDetector="+
    	                              customCompositeDetector());
    }

}
