package tikainaction.chapter4;

import java.io.InputStream;
import java.util.Arrays;

import org.apache.tika.Tika;
import org.apache.tika.detect.CompositeDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypesFactory;

public class AdvancedTypeDetector {

    public static String detectWithCustomConfig(String name)
                                                    throws Exception {
//<start id="custom_type_config"/>
String config = "/org/apache/tika/mime/tika-mimetypes.xml";
Tika tika = new Tika(MimeTypesFactory.create(config));
//<end id="custom_type_config"/>

        return tika.detect(name);
    }

    public static String detectWithCustomDetector(String name)
                                                    throws Exception {
        //<start id="custom_type_detector"/>
        // Create a Tika instance with a specific type configuration
        String config = "/org/apache/tika/mime/tika-mimetypes.xml";
        Detector detector = MimeTypesFactory.create(config);

        Detector custom = new Detector() {
            public MediaType detect(InputStream input,
                                    Metadata metadata) {
                String type = metadata.get("my-custom-type-override");
                if (type != null) {
                    return MediaType.parse(type);
                } else {
                    return MediaType.OCTET_STREAM;
                }
            }
        };

        Tika tika = new Tika(new CompositeDetector(custom, detector));
        //<end id="custom_type_detector"/>

             return tika.detect(name);
    }

}
