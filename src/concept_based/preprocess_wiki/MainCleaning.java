package concept_based.preprocess_wiki;

/**
 * Created by Sahelsoft on 4/3/2018.
 */

import concept_based.preprocess_wiki.WikiClean.WikiLanguage;

import java.sql.*;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainCleaning {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/wikipediaextraction";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static Connection conn2 = null;
    static Statement stmt = null;
    static PreparedStatement selectPreparedStatement=null;
    static PreparedStatement updatePreparedStatement=null;
    static ResultSet selectResultSet=null;
    static String sqlUpdate;

    static WikiClean cleaner;
    static  String finalCleanOutput;

    static ArrayList<String> errorID=new ArrayList<String>();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("Start");
        startWikiConnection();
        cleaner= new WikiClean.Builder().withLanguage(WikiLanguage.DE)
                .withTitle(false).withFooter(false).build();
        retrieveTopRecord();
        stopWikiConnection();
        System.out.println("Finished");
        for (String s:errorID) {
            System.out.println("Error ID= "+ s);
        }
    }

    public static void clean(String recId, String page) {
        try {
            String wikiCleanOutput = cleaner.clean(page) + " N";
            Pattern p = Pattern.compile("[0-9]");
            finalCleanOutput = "";
            for (String w : wikiCleanOutput.split("”|’|=|„+|\\?|—|!|;+| +|,+|\\.\\.|\\. |\\. +|\\.\"|\"+|: |:\\n|:\\r\\n|“|\\.\\]|\\.\\[|\\(|\\)|\\[|\\]|\\'|\\.\\n|\\.\\r\\n|[\r\n]+")) {
                if (!(w.length() < 3 || w.length() > 40 || p.matcher((w)).find()))
                    finalCleanOutput = finalCleanOutput + w + " ";
            }
            updateRec(recId, finalCleanOutput);
        } catch (Exception e) {
            errorID.add(recId);
        }
    }

    public static void retrieveTopRecord() {
        try {
            selectPreparedStatement = conn.prepareStatement(
                    "select * from endearticlesnewfinal", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            selectPreparedStatement.setFetchSize(Integer.MIN_VALUE);
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next())
                clean(selectResultSet.getString("de_id"), selectResultSet.getString("de_text"));
            selectPreparedStatement.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void updateRec(String recId, String content) {
        try {
            sqlUpdate = "update " + "endearticlesnewcleaned" + " set " + "de_text=?" + " where de_id=?";
            updatePreparedStatement = conn2.prepareStatement(sqlUpdate);
            updatePreparedStatement.setString(1, content);
            updatePreparedStatement.setInt(2, Integer.parseInt(recId));
            updatePreparedStatement.executeUpdate();
            updatePreparedStatement.close();
            System.out.println(recId);
        } catch (SQLException e) {
            System.out.println(recId + "====" + e.toString());
        }
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
