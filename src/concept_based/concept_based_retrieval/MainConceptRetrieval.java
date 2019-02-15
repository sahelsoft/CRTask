package concept_based.concept_based_retrieval;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;
import utils.shared_class.TFIDFWithPOSTAG;
import concept_based.esa_feature_generator.esa_reader.MainESAReader;
import org.apache.lucene.search.BooleanQuery;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SahelSoft on 6/22/2018.
 */
public class MainConceptRetrieval {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement selectPreparedStatement = null;
    static ResultSet selectResultSet = null;
    static int Y=4;

    static String curSuspFileName = null;
    static String curSuspSegmentedContent = null;
    static BooleanQuery curBoolQuery = null;


    public static void main(String[] args) {
        try {
            PrintStream err = System.err;

            System.setErr(new PrintStream(new OutputStream() {
                public void write(int b) {
                }
            }));

            System.out.println("Concept Based Retrieval");

            startWikiConnection();
            CoreNLPSentenceSpliting.loadingCoreNLPModel();
            CoreStopWordDictionary.loadStopWordList();
            TFIDFWithPOSTAG.loadIDFList("en");
            MainESAReader.ESAReaderLoader();

            FirstTest_ConceptQueryPerEachSegment();

            stopWikiConnection();
        } catch (Exception e) {
            System.out.println("Error: " + curSuspFileName + "\t" + e.getMessage());
        }
    }

    public static void FirstTest_ConceptQueryPerEachSegment() {
        try {
            ConceptQueryRetrieval.LoadingIndexSearcher();
            selectPreparedStatement = conn.prepareStatement("select susp_file_name, segmented_susp_text from suspdataset");
            selectResultSet = selectPreparedStatement.executeQuery();
            Map<String, Float> curSegmentTops = null;
            Map<String, Float> curDocumentTops = null;

            while (selectResultSet.next()) {
                curSuspFileName = selectResultSet.getString("susp_file_name");
                curSuspSegmentedContent = selectResultSet.getString("segmented_susp_text");
                System.out.println("Cheking:" + curSuspFileName);
                Map<String, Float> curSegmentConceptSet = null;
                String[] segmentContents = curSuspSegmentedContent.split("@@@@@@@@@@@@");
                curDocumentTops = new HashMap<String, Float>();
                for (String segContent : segmentContents) {
                    curSegmentConceptSet = MainESAReader.extractESAConcepts(segContent, "en");
                    BooleanQuery curBooleanQuery = ConceptQueryRetrieval.generateQueryByConceptSet(curSegmentConceptSet);
                    curSegmentTops = ConceptQueryRetrieval.searchByConceptBasedQuery(curBooleanQuery, Y);
                    curSegmentTops = ConceptQueryRetrieval.extractTopOfEachSegmByJoiningSrcSegm(curSegmentTops);
                    curDocumentTops = ConceptQueryRetrieval.copyTopSourceDocToTopDestDoc(curSegmentTops, curDocumentTops);
                }
                curDocumentTops = ConceptQueryRetrieval.extractingTopDocs(curDocumentTops, 10);
                ConceptQueryRetrieval.displayResult(curDocumentTops);
                System.out.println("======================================================================");
            }
            selectPreparedStatement.close();
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
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
