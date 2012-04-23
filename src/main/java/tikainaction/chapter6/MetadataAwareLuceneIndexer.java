package tikainaction.chapter6;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.tika.Tika;
import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.Property;

/**
 * Builds on the LuceneIndexer from Chapter 5 and adds indexing of
 * Metadata.
 */
public class MetadataAwareLuceneIndexer {

  private Tika tika;

  private IndexWriter writer;

  public MetadataAwareLuceneIndexer(IndexWriter writer, Tika tika) {
    this.writer = writer;
    this.tika = tika;
  }

  // <start id="lucene_met_indexer"/>
  public void indexContentSpecificMet(File file) throws Exception {
    Metadata met = new Metadata();
    InputStream is = new FileInputStream(file);
    try {
      tika.parse(is, met); //<co id="tika_facade"/>
      Document document = new Document();
      for (String key : met.names()) {
        String[] values = met.getValues(key);
        for (String val : values) {
          document.add(new Field(key, val, Store.YES, Index.ANALYZED));
        }
        writer.addDocument(document);           //<co id="contribute_met"/>
      }
    } finally {
      is.close();
    }
  }

  //<end id="lucene_met_indexer"/>

  // <start id="lucene_met_dc_indexer"/>
  public void indexWithDublinCore(File file) throws Exception {
    Metadata met = new Metadata();
    met.add(Metadata.CREATOR, "Manning");
    met.add(Metadata.CREATOR, "Tika in Action");
    met.set(Metadata.DATE, new Date());
    met.set(Metadata.FORMAT, tika.detect(file));
    met.set(DublinCore.SOURCE, file.toURL().toString());
    met.add(Metadata.SUBJECT, "File");
    met.add(Metadata.SUBJECT, "Indexing");
    met.add(Metadata.SUBJECT, "Metadata");
    met.set(Property.externalClosedChoise(Metadata.RIGHTS,
        "public", "private"), "public");
    InputStream is = new FileInputStream(file);
    try {
      tika.parse(is, met);
      Document document = new Document();
      for (String key : met.names()) {
        String[] values = met.getValues(key);
        for (String val : values) {
          document.add(new Field(key, val, Store.YES, Index.ANALYZED));
        }
        writer.addDocument(document);
      }
    } finally {
      is.close();
    }
  }
  //<end id="lucene_met_dc_indexer"/>

}
