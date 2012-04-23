package tikainaction.chapter6;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.jackrabbit.util.ISO8601;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.tika.metadata.DublinCore;
import org.apache.tika.metadata.Metadata;

/**
 *
 * Builds on top of the LuceneIndexer and the Metadata discussions in
 * Chapter 6 to output an RSS (or RDF) feed of files crawled by the
 * LuceneIndexer within the last N minutes.
 */
public class RecentFiles {

  private IndexReader reader;

  private SimpleDateFormat rssDateFormat = new SimpleDateFormat(
      "E, dd MMM yyyy HH:mm:ss z");

//<start id="gen_rss"/>
  public String generateRSS(File indexFile)
      throws CorruptIndexException, IOException {
    StringBuffer output = new StringBuffer();
    output.append(getRSSHeaders());
    try {
      reader = IndexReader.open(new SimpleFSDirectory(indexFile));
      IndexSearcher searcher = new IndexSearcher(reader);
      GregorianCalendar gc = new java.util.GregorianCalendar();
      gc.setTime(new Date());
      String nowDateTime = ISO8601.format(gc);
      gc.add(java.util.GregorianCalendar.MINUTE, -5);
      String fiveMinsAgo = ISO8601.format(gc);
      TermRangeQuery query = new TermRangeQuery(
		  Metadata.DATE.toString(),
          fiveMinsAgo, nowDateTime, true, true);
      TopScoreDocCollector collector =
    	  TopScoreDocCollector.create(20, true);
      searcher.search(query, collector);
      ScoreDoc[] hits = collector.topDocs().scoreDocs;
      for (int i = 0; i < hits.length; i++) {
        Document doc = searcher.doc(hits[i].doc);
        output.append(getRSSItem(doc));
      }

    } finally {
      reader.close();
    }

    output.append(getRSSFooters());
    return output.toString();
  }
//<end id="gen_rss"/>

//<start id="use_met_rss"/>
  public String getRSSItem(Document doc) {
    StringBuffer output = new StringBuffer();
    output.append("<item>");
    output.append(emitTag("guid", doc.get(DublinCore.SOURCE),
        "isPermalink", "true"));
    output.append(emitTag("title", doc.get(Metadata.TITLE), null,
    		null));
    output.append(emitTag("link", doc.get(DublinCore.SOURCE), null,
    		null));
    output.append(emitTag("author", doc.get(Metadata.CREATOR), null,
    		null));
    for (String topic : doc.getValues(Metadata.SUBJECT)) {
      output.append(emitTag("category", topic, null, null));
    }
    output.append(emitTag("pubDate", rssDateFormat.format(ISO8601.
    		parse(doc
        .get(Metadata.DATE.toString()))), null, null));
    output.append(emitTag("description", doc.get(Metadata.TITLE), null,
    		null));
    output.append("</item>");
    return output.toString();
  }
//<end id="use_met_rss"/>

  public String getRSSHeaders() {
    StringBuffer output = new StringBuffer();
    output.append("<?xml version=\"1.0\" encoding=\"utf-8\">");
    output.append("<rss version=\"2.0\">");
    output.append("  <channel>");
    output.append("     <title>Tika in Action: Recent Files Feed." +
    		"</title>");
    output.append("     <description>Chapter 6 Examples demonstrating "
        + "use of Tika Metadata for RSS.</description>");
    output.append("     <link>tikainaction.rss</link>");
    output.append("     <lastBuildDate>"
        + rssDateFormat.format(new Date()) + "</lastBuildDate>");
    output
        .append("     <generator>Manning Publications: Tika in Action" +
        		"</generator>");
    output.append("     <copyright>All Rights Reserved</copyright>");
    return output.toString();
  }

  public String getRSSFooters() {
    StringBuffer output = new StringBuffer();
    output.append("   </channel>");
    return output.toString();
  }

  private String emitTag(String tagName, String value,
      String attributeName, String attributeValue) {
    StringBuffer output = new StringBuffer();
    output.append("<");
    output.append(tagName);
    if (attributeName != null) {
      output.append(" ");
      output.append(attributeName);
      output.append("=\"");
      output.append(attributeValue);
      output.append("\"");
    }
    output.append(">");
    output.append(value);
    output.append("</");
    output.append(tagName);
    output.append(">");
    return output.toString();
  }

}
