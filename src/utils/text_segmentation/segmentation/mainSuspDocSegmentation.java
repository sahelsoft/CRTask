
package utils.text_segmentation.segmentation;

import utils.text_segmentation.text_tiling_approach.TextTilingTextSegmenter;
import utils.text_segmentation.stopwords.StopWords;
import utils.text_segmentation.stopwords.ChoiStopWords;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Created by Sahelsoft on 4/19/2018.
 */
public class mainSuspDocSegmentation {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/benchmark";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "asdf";

    static Connection conn = null;
    static Connection conn2 = null;
    static PreparedStatement selectPreparedStatement = null;
    static PreparedStatement updatePreparedStatement = null;
    static ResultSet selectResultSet = null;

    static String sqlUpdate;

    static String curSuspFileName = null;

    static String curSuspFileContent = null;
    static List<String> curSentence = null;
    static List<List<String>> sentences = null;
    static List<Integer> segmentBoundry = null;
    static String curSuspTextWithSegm;

    static Properties propsEN = null;
    static StanfordCoreNLP corenlpEN = null;
    static CoreDocument documentEN = null;

    static TextTilingTextSegmenter textTilingTextSegmenter = null;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Start");
        startWikiConnection();

        loadingStanFordModels();
        StopWords ChoiStopWords = new ChoiStopWords();
        textTilingTextSegmenter = new TextTilingTextSegmenter(ChoiStopWords);
        retrieveRecord();
        stopWikiConnection();
        System.out.println("Finished");
    }

    public static void loadingStanFordModels() {
        try {
            propsEN = new Properties();
            propsEN.setProperty("annotators", "tokenize, ssplit, pos, lemma");
            propsEN.setProperty("tokenize.language", "en");
            corenlpEN = new StanfordCoreNLP(propsEN);
            System.out.println("English CoreNLP Model Loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void retrieveRecord() {
        try {
            selectPreparedStatement = conn.prepareStatement("select susp_file_name, preprocessed_susp_text from suspdataset");
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                System.out.println("======================================================================");
                curSuspFileName = selectResultSet.getString("susp_file_name");
                curSuspFileContent = selectResultSet.getString("preprocessed_susp_text");
                System.out.println(curSuspFileName);
                textToSentenceArray();
                textTillingSegmentation(textTilingTextSegmenter);
                updateSegmentationField();
            }
            selectPreparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSegmentationField() {
        try {
            sqlUpdate = "update suspdataset set segmented_susp_text=? where susp_file_name=?";
            updatePreparedStatement = conn2.prepareStatement(sqlUpdate);
            updatePreparedStatement.setString(1, curSuspTextWithSegm);
            updatePreparedStatement.setString(2, curSuspFileName);
            updatePreparedStatement.executeUpdate();
            updatePreparedStatement.close();
            System.out.println("Update: done");
        } catch (SQLException e) {
            System.out.println(curSuspFileName + "====" + e.toString());
        }
    }

    public static void textToSentenceArray() {
        curSentence = new ArrayList<String>();
        sentences = new ArrayList<>();

        try {
            documentEN = new CoreDocument(curSuspFileContent);
            corenlpEN.annotate(documentEN);

            for (CoreSentence coreSen : documentEN.sentences()) {
                curSentence.clear();
                for (CoreLabel token : coreSen.tokens()) {
                    curSentence.add(token.lemma());
                }
                sentences.add(new ArrayList(curSentence));
            }
            System.out.println("Text to Sentence Array: done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void textTillingSegmentation(TextTilingTextSegmenter textTilingTextSegmenter) {
        segmentBoundry = new ArrayList<Integer>();
        try {
            curSuspTextWithSegm = "";
            segmentBoundry = textTilingTextSegmenter.getSegmentPositions(sentences);
            int sentCounter = 0;
            String curSegm = "";
            int segmCounter = 1;
            for (CoreSentence coreSen : documentEN.sentences()) {
                curSegm = curSegm + coreSen.text() + " ";
                sentCounter++;
                if (segmentBoundry.get(segmCounter) == sentCounter) {
                    segmCounter++;
                    curSuspTextWithSegm = curSuspTextWithSegm + curSegm + "@@@@@@@@@@@@";
                    curSegm = "";
                }
                if (segmCounter == segmentBoundry.size())
                    segmCounter = 0;
            }
            curSuspTextWithSegm = curSuspTextWithSegm + curSegm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Segmentation: done");
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
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}