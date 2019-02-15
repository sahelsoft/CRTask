package concept_based.concept_based_indexing;

import utils.shared_class.CoreNLPSentenceSpliting;
import utils.shared_class.CoreStopWordDictionary;
import utils.shared_class.TFIDFWithPOSTAG;
import concept_based.esa_feature_generator.esa_reader.MainESAReader;

import java.io.File;
import java.sql.*;
import java.util.Map;

/**
 * Created by SahelSoft on 6/22/2018.
 */
public class MainSegmentConceptsIndexer {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static PreparedStatement selectPreparedStatement=null;
    static ResultSet selectResultSet=null;

    static String curSrcFileName = null;
    static String curSrcLanguage = null;
    static String curSrcSegmentedContent = null;


    public static void main(String[] args) {
        try {
            startWikiConnection();
            CoreNLPSentenceSpliting.loadingCoreNLPModel();
            CoreStopWordDictionary.loadStopWordList();
            MainESAReader.ESAReaderLoader();

            IndexConceptData.loadingIndexWriter();

            System.out.println("start...DE");
            srcConceptIndexing("de");
            System.out.println("start...ES");
            srcConceptIndexing("es");
            System.out.println("Finish Indexing");

            System.out.println("Finish Indexing");

            IndexConceptData.closingIndexWriter();
            stopWikiConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private  static void srcConceptIndexing(String language) {
        try {
            File file;
            TFIDFWithPOSTAG.loadIDFList(language);

            selectPreparedStatement = conn.prepareStatement("select src_file_name, src_language, segmented_src_text from srcdataset where src_language=?");
            selectPreparedStatement.setString(1, language);
            selectResultSet = selectPreparedStatement.executeQuery();

            while (selectResultSet.next()) {
                System.out.println("======================================================================");
                curSrcFileName = selectResultSet.getString("src_file_name");
                curSrcLanguage = selectResultSet.getString("src_language");
                curSrcSegmentedContent = selectResultSet.getString("segmented_src_text");

                System.out.println(curSrcFileName);
                String[] segmentText = curSrcSegmentedContent.split("@@@@@@@@@@@@");
                String docConcepts="";
                int indexNum = 0;
                for (String docSegment : segmentText) {
                    indexNum++;
                    Map<String, Float> curSegmentConcepts = MainESAReader.extractESAConcepts(docSegment, curSrcLanguage);
                    docConcepts = MainESAReader.convertConceptsToText(curSegmentConcepts);
                    IndexConceptData.indexDoc(curSrcFileName + "_" + curSrcLanguage + "_" + indexNum, docConcepts);

                }
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
        }
        catch(SQLException se){
            se.printStackTrace();
        }
    }
}
