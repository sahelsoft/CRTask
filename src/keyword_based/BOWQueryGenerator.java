package keyword_based;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TermQuery;

import java.util.ArrayList;

/**
 * Created by Sahelsoft on 6/7/2018.
 */
public class BOWQueryGenerator {


    public static BooleanQuery generateQueryByKeywordSet(ArrayList<String> curKeywordSet) {
        BooleanQuery totalBooleanQuery=null;
        BooleanQuery.Builder booleanBuilder=new BooleanQuery.Builder();
        for (String keyword: curKeywordSet) {
            booleanBuilder.add(new TermQuery(new Term("Content",keyword)), BooleanClause.Occur.SHOULD);
        }
        totalBooleanQuery=booleanBuilder.build();
        return  totalBooleanQuery;
    }

    public static BooleanQuery generateQueryByCollocationSet(ArrayList<String> curCollocationSet) {
        BooleanQuery totalBooleanQuery=null;
        BooleanQuery.Builder booleanBuilder=new BooleanQuery.Builder();
        PhraseQuery.Builder phraseBuilder;
        PhraseQuery phraseQuery;
        for (String collocation: curCollocationSet) {
            phraseBuilder = new PhraseQuery.Builder();
            String[] terms=collocation.split(" ");
            for (String term: terms) {
                phraseBuilder.add(new Term("Content",term));
            }
            booleanBuilder.add(phraseBuilder.build(), BooleanClause.Occur.SHOULD);
        }
        totalBooleanQuery=booleanBuilder.build();
        return  totalBooleanQuery;
    }

}
