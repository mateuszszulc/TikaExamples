package tikainaction.chapter8;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Demonstrates Tika and its ability to sense symlinks.
 */
public class RollbackSoftware {

  public static void main(String[] args) throws Exception {
    RollbackSoftware r = new RollbackSoftware();
    r.rollback(new File(args[0]));
  }

  //<start id="rollback_func"/>
  public void rollback(File deployArea) throws IOException,
       SAXException, TikaException {
    LinkContentHandler handler = new LinkContentHandler();
    Metadata met = new Metadata();
    DeploymentAreaParser parser = new DeploymentAreaParser();
    parser.parse(IOUtils.toInputStream(deployArea.getAbsolutePath()),
    		handler,
        met); //<co id="parse_and_extract"/>
    List<Link> links = handler.getLinks();
    if (links.size() < 2)
     throw new IOException("Must have installed at least 2 versions!");
    Collections.sort(links, new Comparator<Link>() { //<co id="arrange_by_latest_version"/>
      public int compare(Link o1, Link o2) {
        return o1.getText().compareTo(o2.getText());
      }
    });

    this.updateVersion(links.get(links.size() - 2).getText()); //<co id="rollback"/>

  }
  //<end id="rollback_func"/>

  private void updateVersion(String version) {
    System.out.println("Rolling back to version: [" + version + "]");
  }

  class DeploymentAreaParser implements Parser {

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.tika.parser.Parser#getSupportedTypes(
     * org.apache.tika.parser.ParseContext)
     */
    public Set<MediaType> getSupportedTypes(ParseContext context) {
      return Collections.unmodifiableSet(new HashSet<MediaType>(Arrays
          .asList(MediaType.TEXT_PLAIN)));
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
      parse(is, handler, metadata, new ParseContext());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.tika.parser.Parser#parse(java.io.InputStream,
     * org.xml.sax.ContentHandler, org.apache.tika.metadata.Metadata,
     * org.apache.tika.parser.ParseContext)
     */

    //<start id="parse_func"/>
    public void parse(InputStream is, ContentHandler handler,
        Metadata metadata, ParseContext context) throws IOException,
        SAXException, TikaException {

      File deployArea = new File(IOUtils.toString(is));//<co id="unravel_deploy_area"/>
      File[] versions = deployArea.listFiles(new
    		  FileFilter() {

		public boolean accept(File pathname) {
			return !pathname.getName().startsWith("current");
		}
	  });

      XHTMLContentHandler xhtml = new
        XHTMLContentHandler(handler, metadata);
      xhtml.startDocument();

      for (File v : versions) {//<co id="iterate_files"/>
        if(isSymlink(v)) continue;
        xhtml.startElement("a", "href", v.toURL().toExternalForm());
        xhtml.characters(v.getName());//<co id="extract_and_ignore"/>
        xhtml.endElement("a");
      }

    }
    //<end id="parse_func"/>

  }

  private boolean isSymlink(File f) throws IOException {
    return !f.getAbsolutePath().equals(f.getCanonicalPath());
  }

}
