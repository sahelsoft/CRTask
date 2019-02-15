package keyword_based;


import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;
import utils.shared_class.KeywordsExtraction;
import utils.shared_class.TFIDFWithPOSTAG;
import org.apache.lucene.search.BooleanQuery;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sahelsoft on 6/6/2018.
 */
public class MainBOWQueryRetrieval {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement selectPreparedStatement=null;
    static ResultSet selectResultSet=null;

    static String curSuspFileName = null;
    static String curSuspSegmentedContent = null;
    static BooleanQuery curBoolQuery =null;

    public static void main(String[] args) {
        try {
            // this is your print stream, store the reference
            PrintStream err = System.err;

            // now make all writes to the System.err stream silent
            System.setErr(new PrintStream(new OutputStream() {
                public void write(int b) {
                }
            }));

            System.out.println("BOW Retrieval");

            startWikiConnection();
            CoreNLPSentenceSpliting.loadingCoreNLPModel();
            CoreStopWordDictionary.loadStopWordList();
            TFIDFWithPOSTAG.loadIDFList("en");
            retrieveQueryOfEachSegment();
            stopWikiConnection();
        } catch(Exception e) {
            System.out.println("Error: "+curSuspFileName+"\t"+e.getMessage());
        }
    }


    public static void retrieveQueryOfEachSegment() {
        try {
            BOWRetrieval.LoadingIndexSearcher();

            selectPreparedStatement = conn.prepareStatement("select susp_file_name, segmented_susp_text from suspdataset", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            ArrayList<String> curKeywordSet=null;
            Map<String,Float> curDocumentTop=null;
            int Y=1;
            while (selectResultSet.next()) {
                System.out.println("======================================================================");
                curSuspFileName = selectResultSet.getString("susp_file_name");
                curSuspSegmentedContent = selectResultSet.getString("segmented_susp_text");
                System.out.println("Cheking : "+ curSuspFileName);
                curDocumentTop=new HashMap<String,Float>();
                String[] segmentContents=curSuspSegmentedContent.split("@@@@@@@@@@@@");
                for (String content: segmentContents) {
                    curKeywordSet= KeywordsExtraction.getKeywords(content,"en");
                    curBoolQuery=BOWQueryGenerator.generateQueryByKeywordSet(curKeywordSet);
                    Map<String, Float> curSegmentTop=BOWRetrieval.searchByQuery(curBoolQuery,Y);
                    curDocumentTop= BOWRetrieval.copyTopSourceDocToTopDestDoc(curSegmentTop,curDocumentTop);
                }
                Map<String, Float> curDocumentTopTen= BOWRetrieval.extrcingTopTenDoc(curDocumentTop,10);
                BOWRetrieval.displayResult(curDocumentTopTen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startWikiConnection() throws SQLException, ClassNotFoundException {
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void stopWikiConnection() {
        try {
            if (conn != null)
                conn.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
}
