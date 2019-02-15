package concept_based.esa_feature_generator.esa_generator;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;

import java.sql.*;
import java.util.List;

/**
 * Created by Sahelsoft on 6/26/2018.
 */
public class PreprocessingENDEWikiDataForIndexing {
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
    static String deText;
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
            selectPreparedStatement = conn.prepareStatement("select en_id, de_id, en_text, de_text, preprocessed_text from endearticlesnewcleaned", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                System.out.println(++counter);
                enId = selectResultSet.getString("en_id");
                secondId = selectResultSet.getString("de_id");
                enText = selectResultSet.getString("en_text");
                deText = selectResultSet.getString("de_text");
                preprocessed_content=selectResultSet.getString("preprocessed_text");
                if ((!preprocessed_content.equals("")) || enText.length()>(3*deText.length()) || deText.length()>(3*enText.length())) continue;

                content = preprocessEnglishDoc(enText);
                content = content+ " " +preprocessGermanDoc(deText);

                updatePreprocessField();
            }
            selectPreparedStatement.close();
            selectResultSet.close();
        } catch(Exception e) {
            System.out.println("Error: ende---"+enId+"\t"+e.getMessage());
        }
    }

    public static void updatePreprocessField() {
        try {
            sqlUpdate = "update endearticlesnewcleaned set preprocessed_text=? where en_id=? and de_id=?";
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

    private static String preprocessGermanDoc(String deText) {
        String result="";
        selectedTokens = CoreNLPSentenceSpliting.tokenizeGerman(deText);
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
