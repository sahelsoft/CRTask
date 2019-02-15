package baseline;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Sahelsoft on 6/7/2018.
 */
public class BaselineRetrieval {
    private static String INDEX_DIR=null;
    private static IndexSearcher indexSearcher=null;

    public static void LoadingIndexSearcher() throws IOException {
        INDEX_DIR = System.getProperty("user.dir")+ "\\Other\\indexDir\\BaselineIndexDir";
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(reader);
    }

    public static Map<String,Float> searchByQuery(BooleanQuery query, int numberOfHits) throws Exception    {
        TopDocs hits = indexSearcher.search(query, numberOfHits);

        Map<String,Float> foundDocs=new HashMap<String,Float>();
        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = indexSearcher.doc(sd.doc);
            foundDocs.put(String.format(doc.get("SrcFileName")),sd.score);
        }
        return foundDocs;
    }

    public static void displayResult(Map<String,Float> foundDocs) throws IOException {
        for (Map.Entry<String, Float> entry : foundDocs.entrySet()) {
            System.out.println(String.format(entry.getKey() + ":" + entry.getValue()));
        }
    }

}
