package utils.initializing_src_doc;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Created by Sahelsoft on 4/10/2018.
 */
public class MainSrcInitialization {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/Benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement insertPreparedStatement=null;
    static  String sqlInsert=null;

    static String curSrcFileName=null;
    static String curSrcFileContent=null;
    static String preprocessedText=null;
    static String srcLanguage=null;
    static String translatedText=null;

    public static void main(String args[]) throws Exception {
        System.out.println("Start");
        startWikiConnection();

        int counter = 1;
        File srcFilesDir = new File(System.getProperty("user.dir") + "\\Other\\CandidateRetrievalFiles\\CLPD-Dataset\\src\\");
        if (srcFilesDir.isDirectory()) {
            File[] srcListFiles = srcFilesDir.listFiles();
            //iterate on doucments in source folder
            for (File curSrcFile : srcListFiles) {
                System.out.println("Counter=" + counter++);
                curSrcFileName = curSrcFile.getName(); //current source file name
                //reading content of current source file
                System.out.println("\tFileName: "+curSrcFileName);
                curSrcFileContent = FileUtils.readFileToString(curSrcFile, "UTF-8");
                preprocess_text();
                DeterminingSrcLanguage();
                translatingText();
                insertData();
                System.out.println("===================================================================");
            }

            stopWikiConnection();
            System.out.println("Finished");
        }
    }

    public static void insertData() {
        try {
            sqlInsert = "Insert Into srcdataset(src_filename, src_language, src_text, preprocessed_src_text, segmented_src_text, translated_src_text, segmented_translated_src_text) "
                      + "Values (?,?,?,?,?,?,?)";
            insertPreparedStatement = conn.prepareStatement(sqlInsert);
            insertPreparedStatement .setString(1, curSrcFileName);
            insertPreparedStatement .setString(2, srcLanguage);
            insertPreparedStatement .setString(3, curSrcFileContent);
            insertPreparedStatement .setString(4, preprocessedText);
            insertPreparedStatement .setString(5, "empty");
            insertPreparedStatement .setString(6, translatedText);
            insertPreparedStatement .setString(7, "empty");
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();
            System.out.println("\tInsert: done");
        } catch (SQLException e) {
            System.out.println("**** "+ curSrcFileName + "====" + e.toString());
            System.exit(0);
        }
    }

    public static void translatingText() {
        translatedText = "";
        System.out.print("\t");
        for (String s1 : preprocessedText.split("\\r?\\n")) {
            String partTranslated = "";
            try {
                    partTranslated = Translator.translate(srcLanguage, "en", s1);
            } catch (Exception e) {
                    System.out.println("######################## "+e.toString());
            }
            if (!(partTranslated.contains("-------------"))) {
                System.out.print(".");
                translatedText = translatedText + partTranslated + "\r\n";
            }
        }
        System.out.println("\n\tTranslation: done");
    }

    public static void DeterminingSrcLanguage() {
        srcLanguage= LanguageDetector.languageDetector(curSrcFileContent);
        System.out.println("\tLanguage: " + srcLanguage);
    }

    public static void preprocess_text() {
        String[] reg = curSrcFileContent.split("\\r?\\n");
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
