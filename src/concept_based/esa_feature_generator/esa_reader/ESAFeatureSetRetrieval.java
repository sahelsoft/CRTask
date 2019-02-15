package concept_based.esa_feature_generator.esa_reader;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by SahelSoft on 6/21/2018.
 */
public class ESAFeatureSetRetrieval {
    private static String INDEX_DIR=null;
    private static IndexSearcher indexSearcher=null;

    public static BooleanQuery generateQueryByKeywordSet(ArrayList<String> curKeywordSet) {
        BooleanQuery totalBooleanQuery=null;
        BooleanQuery.Builder booleanBuilder=new BooleanQuery.Builder();
        for (String keyword: curKeywordSet) {
            booleanBuilder.add(new TermQuery(new Term("Content",keyword)), BooleanClause.Occur.SHOULD);
        }
        totalBooleanQuery=booleanBuilder.build();
        return  totalBooleanQuery;
    }

    public static void LoadingIndexSearcher() throws IOException {
        INDEX_DIR = System.getProperty("user.dir")+"\\Other\\ESAIndexDir";
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(reader);
    }

    public static Map<String,Float> searchQueryInFeatureSet(BooleanQuery query, int numberOfHits) throws Exception {
        TopDocs hits = indexSearcher.search(query, numberOfHits);

        Map<String,Float> foundDocs=new HashMap<String,Float>();
        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = indexSearcher.doc(sd.doc);
            foundDocs.put(String.format(doc.get("indexID")),sd.score);
        }
        return foundDocs;
    }

    public static void displayResult(Map<String,Float> foundDocs) throws IOException {
        for (Map.Entry<String, Float> entry : foundDocs.entrySet()) {
            System.out.println(String.format(entry.getKey() + ":" + entry.getValue()));
        }
    }

    public static Map<String, Float> copyTopSourceDocToTopDestDoc(Map<String,Float> sourceMap, Map<String,Float> destMap) {
        for(Map.Entry entry : sourceMap.entrySet()) {
            if (!destMap.containsKey(entry.getKey())) {
                destMap.put(entry.getKey().toString(), (float) entry.getValue());
            } else {
                if (destMap.get(entry.getKey()) < (float) entry.getValue()) {
                    destMap.replace(entry.getKey().toString(), (float) entry.getValue());
                }
            }
        }
        return destMap;
    }

}
