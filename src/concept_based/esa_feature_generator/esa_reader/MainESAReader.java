package concept_based.esa_feature_generator.esa_reader;

import utils.shared_class.KeywordsExtraction;
import org.apache.lucene.search.BooleanQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ZohrehSadat on 6/21/2018.
 */
public class MainESAReader {
    static int B=40;
    public static void ESAReaderLoader() throws IOException {
        ESAFeatureSetRetrieval.LoadingIndexSearcher();
    }

    public static Map<String,Float> extractESAConcepts(String text, String language) throws Exception {
        ArrayList<String> keywords = KeywordsExtraction.getKeywords(text, language);
        BooleanQuery curBoolQuery= ESAFeatureSetRetrieval.generateQueryByKeywordSet(keywords);
        Map<String,Float> curSegmentConcepts=ESAFeatureSetRetrieval.searchQueryInFeatureSet(curBoolQuery,B);

        return curSegmentConcepts;
    }

    public static String convertConceptsToText(Map<String,Float> Concepts) {
        String docConcepts="";
        for (Map.Entry entry: Concepts.entrySet()) {
            docConcepts=docConcepts+" "+entry.getKey();
        }
        return docConcepts;
    }
}
