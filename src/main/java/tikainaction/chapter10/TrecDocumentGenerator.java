package tikainaction.chapter10;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;

/**
 * 
 * Generates document summaries for corpus analysis in the Open Relevance
 * project.
 * 
 */
public class TrecDocumentGenerator {

  //<start id="orp_tika_integration"/>
  public TrecDocument summarize(File file) throws FileNotFoundException,
      IOException, TikaException {
    Tika tika = new Tika(); //<co id="get_tika"/>
    Metadata met = new Metadata();
    
    String contents = tika.parseToString(new FileInputStream(file), met); 
    //<co id="parse_contents_and_met"/>
        
    return new TrecDocument(met.get(Metadata.RESOURCE_NAME_KEY), 
    		contents, met
        .getDate(Metadata.DATE)); //<co id="build_trec_orp_doc"/>
    
  }

  //<end id="orp_tika_integration"/>

  // copied from
  // http://svn.apache.org/repos/asf/lucene/openrelevance/trunk/src/java/org/
  // apache/orp/util/TrecDocument.java
  // since the ORP jars aren't published anywhere
  class TrecDocument {
    private CharSequence docname;
    private CharSequence body;
    private Date date;

    public TrecDocument(CharSequence docname, CharSequence body, Date date) {
      this.docname = docname;
      this.body = body;
      this.date = date;
    }

    public TrecDocument() {
    }

    /**
     * @return the docname
     */
    public CharSequence getDocname() {
      return docname;
    }

    /**
     * @param docname
     *          the docname to set
     */
    public void setDocname(CharSequence docname) {
      this.docname = docname;
    }

    /**
     * @return the body
     */
    public CharSequence getBody() {
      return body;
    }

    /**
     * @param body
     *          the body to set
     */
    public void setBody(CharSequence body) {
      this.body = body;
    }

    /**
     * @return the date
     */
    public Date getDate() {
      return date;
    }

    /**
     * @param date
     *          the date to set
     */
    public void setDate(Date date) {
      this.date = date;
    }
  }

}
