package concept_based.esa_feature_generator.esa_generator;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;

import java.sql.*;
import java.util.List;

/**
 * Created by Sahelsoft on 6/26/2018.
 */
public class PreprocessingENESWikiDataForIndexing {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/wikipediaextraction";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static Connection conn2 = null;
    static PreparedStatement selectPreparedStatement=null;
    static PreparedStatement updatePreparedStatement = null;
    static ResultSet selectResultSet=null;
    static String sqlUpdate;

    static String enId;
    static String secondId;
    static String enText;
    static String esText;
    static String content;

    static List<String> selectedTokens=null;

    public static void main(String[] args) throws Exception {
        startWikiConnection();
        CoreNLPSentenceSpliting.loadingCoreNLPModel();
        CoreStopWordDictionary.loadStopWordList();

        preprocessENDEWiki();

        stopWikiConnection();
    }

    private static void preprocessENDEWiki() {
        int counter=0;
        String preprocessed_content;
        try {
            selectPreparedStatement = conn.prepareStatement("select en_id, es_id, en_text, es_text, preprocessed_text from enesarticlesnewcleaned", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                System.out.println(++counter);
                enId = selectResultSet.getString("en_id");
                secondId = selectResultSet.getString("es_id");
                enText = selectResultSet.getString("en_text");
                esText = selectResultSet.getString("es_text");
                preprocessed_content=selectResultSet.getString("preprocessed_text");
                if ((!preprocessed_content.equals("")) || enText.length()>(3*esText.length()) || esText.length()>(3*enText.length())) continue;

                content = preprocessEnglishDoc(enText);
                content = content+ "    " +preprocessSpanishDoc(esText);

                updatePreprocessField();
            }
            selectPreparedStatement.close();
            selectResultSet.close();
        } catch(Exception e) {
            System.out.println("Error: enes---"+enId+"\t"+e.getMessage());
        }
    }

    public static void updatePreprocessField() {
        try {
            sqlUpdate = "update enesarticlesnewcleaned set preprocessed_text=? where en_id=? and es_id=?";
            updatePreparedStatement = conn2.prepareStatement(sqlUpdate);
            updatePreparedStatement.setString(1, content);
            updatePreparedStatement.setString(2, enId);
            updatePreparedStatement.setString(3, secondId);
            updatePreparedStatement.executeUpdate();
            updatePreparedStatement.close();
        } catch (SQLException e) {
            System.out.println(enId+ "=========" + e.toString());
        }
    }

    private static String preprocessEnglishDoc(String enText) {
        String result="";
        selectedTokens = CoreNLPSentenceSpliting.tokenizeEnglish(enText);
        for (String token:selectedTokens) {
            result=result+" "+token;
        }
        return result;
    }

    private static String preprocessSpanishDoc(String esText) {
        String result="";
        selectedTokens = CoreNLPSentenceSpliting.tokenizeSpanish(esText);
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
            conn2 = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void stopWikiConnection() {
        try {
            if (conn != null)
                conn.close();
            if (conn2 != null)
                conn2.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
}
