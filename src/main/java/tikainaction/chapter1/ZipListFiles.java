package tikainaction.chapter1;

//JDK imports
import java.io.IOException;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 *
 * Example code listing from Chapter 1. Lists a zip file's entries
 * using JDK's standard APIs.
 *
 */
public class ZipListFiles {
	public static void main(String[] args) throws Exception {
		if(args.length > 0) {
			for	(String file : args) {
				System.out.println("Files in " + file + " file:");
				listZipEntries(file);
			}
		}
	}

  //<start id="list_zip_file"/>
  public static void listZipEntries(String path) throws IOException {
      ZipFile zip = new ZipFile(path);
      for (ZipEntry entry : Collections.list(zip.entries())) {
          System.out.println(entry.getName());
      }
  }
  //<end id="list_zip_file"/>

}