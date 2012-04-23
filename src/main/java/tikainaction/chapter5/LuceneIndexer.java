package tikainaction.chapter5;

//<start id="lucene_indexer"/>
/* */
import java.io.File;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.tika.Tika;

//<co id="indexer_class"/>
public class LuceneIndexer {

    //<co id="facade_instance"/>
    private final Tika tika;

    //<co id="indexwriter_instance"/>
    private final IndexWriter writer;

   //<co id="constructor"/>
    public LuceneIndexer(Tika tika, IndexWriter writer) {
        this.tika = tika;
        this.writer = writer;
    }

    //<co id="index_document"/>
    public void indexDocument(File file) throws Exception {
        Document document = new Document();
        document.add(new Field(
                "filename", file.getName(),
                Store.YES, Index.ANALYZED));
        document.add(new Field(
                "fulltext", tika.parseToString(file),
                Store.NO, Index.ANALYZED));
        writer.addDocument(document);
    }

}
//<end id="lucene_indexer"/>
