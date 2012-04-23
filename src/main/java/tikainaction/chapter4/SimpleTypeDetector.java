package tikainaction.chapter4;

//<start id="simple_type_detector"/>
import java.io.File;

import org.apache.tika.Tika;

public class SimpleTypeDetector {

  public static void main(String[] args) throws Exception {
      Tika tika = new Tika();

      for (String file : args) {
          String type = tika.detect(new File(file));
          System.out.println(file + ": " + type);
      }
  }

}
//<end id="simple_type_detector"/>
