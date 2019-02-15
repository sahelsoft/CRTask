package baseline;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;
import utils.shared_class.KeywordsExtraction;
import utils.shared_class.TFIDFWithPOSTAG;
import org.apache.lucene.search.BooleanQuery;

import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sahelsoft on 6/6/2018.
 */
public class MainBaselineQueryRetrieval {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/Benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement selectPreparedStatement=null;
    static ResultSet selectResultSet=null;

    public static void main(String[] args) {
        try {
            // this is your print stream, store the reference
            PrintStream err = System.err;

            // now make all writes to the System.err stream silent
            System.setErr(new PrintStream(new OutputStream() {
                public void write(int b) {
                }
            }));

            System.out.println("Baseline Retrieval");

            startWikiConnection();
            CoreNLPSentenceSpliting.loadingCoreNLPModel();
            CoreStopWordDictionary.loadStopWordList();
            TFIDFWithPOSTAG.loadIDFList("en");

            baselineCandidateRetrieval();


            stopWikiConnection();
        } catch(Exception e) {
            System.out.println("Error: " +e.getMessage());
        }
    }


    public static void baselineCandidateRetrieval() {
        try {
            BaselineRetrieval.LoadingIndexSearcher();


            selectPreparedStatement = conn.prepareStatement("select susp_file_name, preprocessed_susp_text from suspdataset", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            List<String> cur_set;
            Map<String, Float> cur_document_top_ten;
            String cur_susp_file_name = null;
            String cur_susp_content = null;
            BooleanQuery cur_bool_query =null;

            while (selectResultSet.next()) {
                cur_susp_file_name = selectResultSet.getString("susp_file_name");
                cur_susp_content = selectResultSet.getString("preprocessed_susp_text");
                cur_set= CoreNLPSentenceSpliting.tokenizeEnglish(cur_susp_content);
                cur_bool_query= BaselineQueryGenerator.generateQueryByKeywordSet(cur_set);
                cur_document_top_ten= BaselineRetrieval.searchByQuery(cur_bool_query,10);
                System.out.println(cur_susp_file_name);
                BaselineRetrieval.displayResult(cur_document_top_ten);
                System.out.println("======================================================================");
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
