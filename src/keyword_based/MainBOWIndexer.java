package keyword_based;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;

import java.sql.*;
import java.util.List;

public class MainBOWIndexer {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement selectPreparedStatement = null;
    static ResultSet selectResultSet = null;

    static String curSrcFileName = null;
    static String curSrcSegmentedContent = null;


    public static void main(String[] args) {
        try {
            startWikiConnection();
            CoreNLPSentenceSpliting.loadingCoreNLPModel();
            CoreStopWordDictionary.loadStopWordList();
            IndexData.loadingIndexWriter();

            selectPreparedStatement = conn.prepareStatement("select src_file_name, preprocessed_src_text from srcdataset", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                System.out.println("======================================================================");
                curSrcFileName = selectResultSet.getString("src_file_name");
                curSrcSegmentedContent = selectResultSet.getString("preprocessed_src_text");
                System.out.println(curSrcFileName);
                String docContent = preprocessDoc(curSrcSegmentedContent);
                IndexData.indexDoc(curSrcFileName, docContent);
            }

            IndexData.closingIndexWriter();
            stopWikiConnection();
        } catch (Exception e) {
            System.out.println("Error: " + curSrcFileName + "\t" + e.getMessage());
        }
    }

    private static String preprocessDoc(String docContent) {
        List<String> selectedTokens = null;
        String result = "";
        selectedTokens = CoreNLPSentenceSpliting.tokenizeEnglish(docContent);
        for (String token : selectedTokens) {
            result = result + " " + token;
        }
        return result;
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
