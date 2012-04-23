package tikainaction.chapter8;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Parses the output of /bin/ls and counts the number of files and the
 * number of executables using Tika.
 */
public class DirListParser implements Parser {

  private static final long serialVersionUID = 2717930544410610735L;

  private static Set<MediaType> SUPPORTED_TYPES =
      new HashSet<MediaType>(Arrays.asList(MediaType.TEXT_PLAIN));

  /*
   * (non-Javadoc)
   *
   * @see
   * org.apache.tika.parser.Parser#getSupportedTypes(
   * org.apache.tika.parser.ParseContext)
   */
  public Set<MediaType> getSupportedTypes(ParseContext context) {
    return SUPPORTED_TYPES;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.tika.parser.Parser#parse(java.io.InputStream,
   * org.xml.sax.ContentHandler, org.apache.tika.metadata.Metadata)
   */
  public void parse(InputStream is, ContentHandler handler,
                                               Metadata metadata)
               throws IOException, SAXException, TikaException {
    this.parse(is, handler, metadata, new ParseContext());
  }

  /*
   * (non-Javadoc)
   *
   * @see org.apache.tika.parser.Parser#parse(java.io.InputStream,
   * org.xml.sax.ContentHandler, org.apache.tika.metadata.Metadata,
   * org.apache.tika.parser.ParseContext)
   */
  //<start id="parse_method"/>
  public void parse(InputStream is, ContentHandler handler,
      Metadata metadata, ParseContext context)
               throws IOException, SAXException, TikaException {

    List<String> lines = FileUtils.readLines(TikaInputStream.get(is).
    		getFile());
    for (String line : lines) {
      String[] fileToks = line.split("\\s+");
      if (fileToks.length < 8)
        continue;//<co id="ignore_lines"/>
      String filePermissions = fileToks[0];
      String numHardLinks = fileToks[1];
      String fileOwner = fileToks[2]; //<co id="parse_listing_info"/>
      String fileOwnerGroup = fileToks[3];
      String fileSize = fileToks[4];
      StringBuffer lastModDate = new StringBuffer();
      lastModDate.append(fileToks[5]);
      lastModDate.append(" ");
      lastModDate.append(fileToks[6]);
      lastModDate.append(" ");
      lastModDate.append(fileToks[7]);
      StringBuffer fileName = new StringBuffer();
      for (int i = 8; i < fileToks.length; i++) {
        fileName.append(fileToks[i]);
        fileName.append(" ");
      }
      fileName.deleteCharAt(fileName.length() - 1);
      this
          .addMetadata(metadata, filePermissions, numHardLinks,
        		  fileOwner,
              fileOwnerGroup, fileSize, lastModDate.toString(),
              fileName
                  .toString());//<co id="parse_add_met"/>
    }
  }
  //<end id="parse_method"/>

  public static void main(String[] args) throws IOException,
      SAXException, TikaException {
    DirListParser parser = new DirListParser();
    Metadata met = new Metadata();
    parser.parse(System.in, new BodyContentHandler(), met);

    System.out.println("Num files: "+met.getValues("Filename").length);
    System.out.println("Num executables: "+met.get("NumExecutables"));
  }

  //<start id="add_met"/>
  private void addMetadata(Metadata metadata, String filePerms,
      String numHardLinks, String fileOwner, String fileOwnerGroup,
      String fileSize, String lastModDate, String fileName) {
    metadata.add("FilePermissions", filePerms);
    metadata.add("NumHardLinks", numHardLinks); //<co id="scalar_met"/>
    metadata.add("FileOwner", fileOwner);
    metadata.add("FileOwnerGroup", fileOwnerGroup);
    metadata.add("FileSize", fileSize);
    metadata.add("LastModifiedDate", lastModDate);
    metadata.add("Filename", fileName);

    if (filePerms.indexOf("x") != -1 &&
    		filePerms.indexOf("d") == -1) {
      if (metadata.get("NumExecutables") != null) {
        int numExecs = Integer.valueOf(
        		metadata.get("NumExecutables"));
        numExecs++;
        metadata.set("NumExecutables", String.valueOf(numExecs));
      } else {
        metadata.set("NumExecutables", "1");
      }//<co id="counts"/>
    }
  }
  //<end id="add_met"/>

}
