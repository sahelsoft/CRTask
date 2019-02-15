package concept_based.esa_feature_generator.esa_generator;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;

import java.sql.*;

/**
 * Created by Sahelsoft on 6/17/2018.
 */
public class MainESAGenerator {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/wikipediaextraction";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement selectPreparedStatement=null;
    static ResultSet selectResultSet=null;

    static String enId;
    static String secondId;
    static String content;

    public static void main(String[] args) throws Exception {
        startWikiConnection();
        CoreNLPSentenceSpliting.loadingCoreNLPModel();
        CoreStopWordDictionary.loadStopWordList();

        IndexData.loadingIndexWriter(true);
        indexENDEWiki();
        System.out.println("========================================");

        indexENESWiki();
        System.out.println("========================================");

        IndexData.closingIndexWriter();

        stopWikiConnection();
    }

    private static void indexENDEWiki() {
        int counter=0;
        try {
            selectPreparedStatement = conn.prepareStatement("select en_id, de_id, new_preprocessed_text from endearticlesnewcleaned", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                enId = selectResultSet.getString("en_id");
                secondId = selectResultSet.getString("de_id");
                content= selectResultSet.getString("new_preprocessed_text");

                if (content.equals(""))
                    continue;
                else
                    IndexData.indexDoc("ENDE-"+enId, content);

                System.out.println(++counter);
            }
            selectPreparedStatement.close();
            selectResultSet.close();
        } catch(Exception e) {
            System.out.println("Error: ende---"+enId+"\t"+e.getMessage());
        }
    }

    private static void indexENESWiki() {
        int counter=0;
        try {
            selectPreparedStatement = conn.prepareStatement("select en_id, es_id, new_preprocessed_text from enesarticlesnewcleaned", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                enId = selectResultSet.getString("en_id");
                secondId = selectResultSet.getString("es_id");
                content= selectResultSet.getString("new_preprocessed_text");

                if (content.equals(""))
                    continue;
                else
                    IndexData.indexDoc("ENES-"+enId, content);

                System.out.println(++counter);
            }
            selectPreparedStatement.close();
            selectResultSet.close();
        } catch(Exception e) {
            System.out.println("Error: enes---"+enId+"\t"+e.getMessage());
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
