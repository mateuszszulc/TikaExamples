package tikainaction.chapter5;

import java.io.File;
import java.io.Reader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;

public class LuceneIndexerExtended {

    private final IndexWriter writer;

    private final Tika tika;

    public LuceneIndexerExtended(IndexWriter writer, Tika tika) {
        this.writer = writer;
        this.tika = tika;
    }

//<start id="lucene_indexer_main"/>
public static void main(String[] args) throws Exception {
    IndexWriter writer = new IndexWriter(
            new SimpleFSDirectory(new File(args[0])),
            new StandardAnalyzer(Version.LUCENE_30),
            MaxFieldLength.UNLIMITED);
    try {
        LuceneIndexer indexer = new LuceneIndexer(new Tika(), writer);
        for (int i = 1; i < args.length; i++) {
            indexer.indexDocument(new File(args[i]));
        }
    } finally {
        writer.close();
    }
}
//<end id="lucene_indexer_main"/>

//<start id="lucene_indexer_reader"/>
public void indexDocument(File file) throws Exception {
    Reader fulltext = tika.parse(file);
    try {
        Document document = new Document();
        document.add(new Field(
                "filename", file.getName(),
                Store.YES, Index.ANALYZED));
        document.add(new Field("fulltext", fulltext));
        writer.addDocument(document);
    } finally {
        fulltext.close();
    }
}
//<end id="lucene_indexer_reader"/>

}
