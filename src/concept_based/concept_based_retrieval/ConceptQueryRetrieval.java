package concept_based.concept_based_retrieval;

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
 * Created by SahelSoft on 6/22/2018.
 */
public class ConceptQueryRetrieval {
    private static String INDEX_DIR=null;
    private static IndexSearcher indexSearcher=null;

    public static BooleanQuery generateQueryByConceptSet(Map<String,Float> curConceptSet) {
        BooleanQuery totalBooleanQuery=null;
        BooleanQuery.Builder booleanBuilder=new BooleanQuery.Builder();
        for (Map.Entry entry: curConceptSet.entrySet()) {
            booleanBuilder.add(new TermQuery(new Term("Concepts",entry.getKey().toString())), BooleanClause.Occur.SHOULD);
        }
        totalBooleanQuery=booleanBuilder.build();
        return  totalBooleanQuery;
    }

    public static void LoadingIndexSearcher() throws IOException {
        INDEX_DIR = System.getProperty("user.dir")+"\\Other\\SegESABasedIndexDir";
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        indexSearcher = new IndexSearcher(reader);
    }

    public static Map<String,Float> searchByConceptBasedQuery(BooleanQuery query, int numberOfHits) throws Exception    {
        TopDocs hits = indexSearcher.search(query, numberOfHits);

        Map<String,Float> foundDocs=new HashMap<String,Float>();
        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = indexSearcher.doc(sd.doc);
            foundDocs.put(String.format(doc.get("SrcFileSegName")),sd.score);
        }
        return foundDocs;
    }

    public static Map<String, Float> extractTopOfEachSegmByJoiningSrcSegm(Map<String,Float> curSegmnetTop) {
        Map<String, Float> selectedSrc=new HashMap<String, Float>();
        for (Map.Entry entry:curSegmnetTop.entrySet()) {
            String[] tempKey=entry.getKey().toString().split("_");
            Float tempValue= (Float) entry.getValue();
            if (selectedSrc.containsKey(tempKey[0])) {
                selectedSrc.replace(tempKey[0],selectedSrc.get(tempKey[0])+tempValue);
            } else {
                selectedSrc.put(tempKey[0],tempValue);
            }
        }
        return selectedSrc;
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

    public static <K, V extends Comparable<? super V>> Map<K, V> extractingTopDocs(Map<K, V> unsortMap, int numberOfTop) {
        List<Map.Entry<K, V>> list =
                new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        int counter=0;
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
            if (++counter>=numberOfTop) break;
        }
        return result;
    }

    public static void displayResult(Map<String,Float> foundDocs) throws IOException {
        for (Map.Entry<String, Float> entry : foundDocs.entrySet()) {
            System.out.println(String.format(entry.getKey() + ":" + entry.getValue()));
        }
    }
}
