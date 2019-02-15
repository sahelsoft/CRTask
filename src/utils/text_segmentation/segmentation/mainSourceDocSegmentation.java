package utils.text_segmentation.segmentation;

import utils.text_segmentation.stopwords.GermanStopWords;
import utils.text_segmentation.stopwords.SpanishStopWords;
import utils.text_segmentation.stopwords.StopWords;
import utils.text_segmentation.text_tiling_approach.TextTilingTextSegmenter;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Created by Sahelsoft on 4/19/2018.
 */
public class mainSourceDocSegmentation {
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

    static String curSrcFileName = null;

    static String curSrcFileContent = null;
    static List<String> curSentence = null;
    static List<List<String>> sentences = null;
    static List<Integer> segmentBoundry = null;
    static String curSrcTextWithSegm;

    static Properties propsDE = null;
    static StanfordCoreNLP corenlpDE = null;
    static CoreDocument document = null;

    static Properties propsSP = null;
    static StanfordCoreNLP corenlpSP = null;

    static TextTilingTextSegmenter germanTextTilingTextSegmenter = null;
    static TextTilingTextSegmenter spanishTextTilingTextSegmenter = null;

    static String curSrcFilelanguage;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        System.out.println("Start");
        startWikiConnection();

        loadingStanFordModels();
        StopWords germanStopWords = new GermanStopWords();
        germanTextTilingTextSegmenter = new TextTilingTextSegmenter(germanStopWords);

        StopWords spanishStopWords = new SpanishStopWords();
        spanishTextTilingTextSegmenter = new TextTilingTextSegmenter(spanishStopWords);

        retrieveRecord();

        stopWikiConnection();
        System.out.println("Finished");
    }


    public static void loadingStanFordModels() {
        try {
            propsDE = new Properties();
            propsDE.setProperty("annotators", "tokenize, ssplit, pos, lemma");
            propsDE.setProperty("tokenize.language", "de");
            propsDE.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/german/german-hgc.tagger");
            corenlpDE = new StanfordCoreNLP(propsDE);
            System.out.println("German CoreNLP Model Loaded");

            propsSP = new Properties();
            propsSP.setProperty("annotators", "tokenize, ssplit, pos, lemma");
            propsSP.setProperty("tokenize.language", "es");
            propsSP.setProperty("pos.model", "edu/stanford/nlp/models/pos-tagger/spanish/spanish-ud.tagger");
            corenlpSP = new StanfordCoreNLP(propsSP);
            System.out.println("Spanish CoreNLP Model Loaded");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void retrieveRecord() {
        try {
            selectPreparedStatement = conn.prepareStatement("select src_file_name, src_language, preprocessed_src_text from srcdataset");
            selectResultSet = selectPreparedStatement.executeQuery();
            while (selectResultSet.next()) {
                System.out.println("======================================================================");
                curSrcFileName = selectResultSet.getString("src_file_name");
                curSrcFileContent = selectResultSet.getString("preprocessed_src_text");
                curSrcFilelanguage = selectResultSet.getString("src_language");
                System.out.println(curSrcFileName);
                if (curSrcFilelanguage.equals("de")) {
                    textToSentenceArrayText(corenlpDE);
                    textTillingSegmentation(germanTextTilingTextSegmenter);

                } else {
                    textToSentenceArrayText(corenlpSP);
                    textTillingSegmentation(spanishTextTilingTextSegmenter);
                }

                updateSegmentationField();
            }
            selectPreparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSegmentationField() {
        try {
            sqlUpdate = "update srcdataset set segmented_src_text=? where src_file_name=?";
            updatePreparedStatement = conn2.prepareStatement(sqlUpdate);
            updatePreparedStatement.setString(1, curSrcTextWithSegm);
            updatePreparedStatement.setString(2, curSrcFileName);
            updatePreparedStatement.executeUpdate();
            updatePreparedStatement.close();
            System.out.println("Update: done");
        } catch (SQLException e) {
            System.out.println(curSrcFileName + "====" + e.toString());
        }
    }

    private static void textToSentenceArrayText(StanfordCoreNLP corenlp) {
        curSentence = new ArrayList<String>();
        sentences = new ArrayList<>();

        try {
            document = new CoreDocument(curSrcFileContent);
            corenlp.annotate(document);

            for (CoreSentence coreSen : document.sentences()) {
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
            curSrcTextWithSegm = "";

            segmentBoundry = textTilingTextSegmenter.getSegmentPositions(sentences);
            int sentCounter = 0;
            String curSegm = "";
            int segmCounter = 1;
            for (CoreSentence coreSen : document.sentences()) {
                curSegm = curSegm + coreSen.text() + " ";
                sentCounter++;
                if (segmentBoundry.get(segmCounter) == sentCounter) {
                    segmCounter++;
                    curSrcTextWithSegm = curSrcTextWithSegm + curSegm + "@@@@@@@@@@@@";
                    curSegm = "";
                }
                if (segmCounter == segmentBoundry.size())
                    segmCounter = 0;
            }
            curSrcTextWithSegm = curSrcTextWithSegm + curSegm;
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