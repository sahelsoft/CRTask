package utils.initializing_susp_doc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Sahelsoft on 5/23/2018.
 */
public class MainSuspDocInsertion {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/Benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement insertPreparedStatement=null;
    static String sqlInsert=null;

    static String curSuspFileName=null;
    static String curSuspFileContent=null;
    static String preprocessedText=null;

    public static void main(String[] args) throws Exception {
        System.out.println("Start");
        startWikiConnection();
        int counter = 1;
        File suspFilesDir = new File(System.getProperty("user.dir") + "\\Other\\CandidateRetrievalFiles\\CLPD-Dataset\\susp_withCrossPlag\\");
        if (suspFilesDir.isDirectory()) {
            File[] suspListFiles = suspFilesDir.listFiles();
            //iterate on doucments in source folder
            for (File curSuspFile : suspListFiles) {
                System.out.println("Counter=" + counter++);
                curSuspFileName = curSuspFile.getName(); //current source file name
                //reading content of current source file
                System.out.println("\tFileName: " + curSuspFileName);
                curSuspFileContent = FileUtils.readFileToString(curSuspFile, "UTF-8");
                preprocess_text();
                insertData();
                System.out.println("===================================================================");
            }
        }
        stopWikiConnection();
        System.out.println("Finished");
    }

    public static void insertData() {
        try {
            sqlInsert = "Insert Into suspdataset(susp_filename,susp_text,preprocessed_susp_text,segmented_susp_text) "
                    + "Values (?,?,?,?)";
            insertPreparedStatement = conn.prepareStatement(sqlInsert);
            insertPreparedStatement .setString(1, curSuspFileName);
            insertPreparedStatement .setString(2, curSuspFileContent);
            insertPreparedStatement .setString(3, preprocessedText);
            insertPreparedStatement .setString(4, " ");
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
            System.out.println("\tInsert: done");
        } catch (SQLException e) {
            System.out.println("**** "+ curSuspFileName + "====" + e.toString());
            System.exit(0);
        }
    }

    public static void preprocess_text() {
        String[] reg = curSuspFileContent.split("\\r?\\n");
        preprocessedText = "";
        String last = "";
        for (String s : reg) {
            if (s.length() > 0) {
                s = s.replace("»", "");
                s = s.replace("«", "");
                s = s.replace("*", "");
                if (s.trim().length() != 0) {
                    preprocessedText= preprocessedText+ s + " ";
                    last = "";
                }
            } else if (last != "newLine") {
                preprocessedText = preprocessedText.trim() + "\r\n";
                last = "newLine";
            }
        }
        System.out.println("\tPreprocess: done");
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
